# Damage System（当前实现）

本文档描述 `breacore` 当前伤害系统的**实际执行链路**，重点说明：

- 玩家攻击实体时如何进入 Brea 管线
- `BreaDamageSource` 如何解析与回退
- 预计算（Pre）与运行时上下文（RuntimeContext）如何协作
- 原版 `actuallyHurt` 阶段如何被短路到自定义护盾/护甲逻辑
- 后处理（Post）与清理时机

---

## 1. 入口与总览

主入口位于：

- `LivingEntityDamageMixin#breacore$replaceDamageForActuallyHurt`

它重定向 `LivingEntity.hurtServer -> actuallyHurt(...)` 调用，流程分两条：

1. **Brea 管线命中**（成功得到 `BreaDamageSource`）
2. **原版回退**（无法得到 `BreaDamageSource`，直接走原版）

---

## 2. 伤害源解析流程

方法：`LivingEntityDamageMixin#breacore$tryConvertSource(victim, source)`

顺序：

1. `source` 已经是 `BreaDamageSource` -> 直接返回
2. 若 `victim` 实现 `IBreaDamageSourceProvider`，先调用 provider 获取
3. provider 返回 `null` 时，调用 fallback 构建：
    - `breacore$buildFallbackBreaDamageSource(source)`
4. 若解析得到 `BreaDamageSource`，会在 `tryConvertSource` 内尝试注入暴击决议（来自 `CriticalDecisionRuntime`
   ，consume-once）。
5. 无论当前解析结果是否为 `null`，都抛出：
    - `BreaDamageSourceResolveEvent(victim, source, resolved)`
6. 返回 `event.getDamageSource()`（监听器可覆盖最终结果）

fallback 构建关键点：

- 仅当攻击者为 `LivingEntity` 且拥有
    - `HARD_ARMOR_PENETRATION_VALUE`
    - `SOFT_ARMOR_PENETRATION_VALUE`
      attribute 实例时才构建
- 元素/法术命中率/护甲作用比/无敌帧来自武器组件：
    - `ItemComponentRegistry.WEAPON_DAMAGE_PROFILE`
    - 缺失时使用 `WeaponDamageProfile.GEOGRAPHY_ONLY`

---

## 3. 护甲上下文解析流程

方法：`LivingEntityDamageMixin#breacore$resolveArmorContext(...)`

顺序：

1. 若 `victim` 实现 `IBreaDamageArmorContextProvider`，先取自定义上下文
2. 结果为 `null` 时回退：`new SimpleDamageArmorContext(...)`
3. 抛事件：`BreaDamageArmorContextEvent`
4. 返回 `event.getArmorContext()`（监听器可替换）

---

## 4. 预计算（Pre）阶段

调用：`DamageCalculator.prepareForVanillaApply(...)`

当前职责：

- 只做预计算并发布 `DamageCalculationEvent.Pre`
- 不再写入 RuntimeContext（已迁移到 mixin 中显式 push）

### 4.1 普通伤害分支

`DamageCalculator#calculatePre(...)` 中执行：

1. 原始伤害拆分为法术/物理（按 `spellShieldHitRatio`）
2. 法术部分受元素抗性影响
3. 物理部分暴击判定：
    - 若 `BreaDamageSource` 已携带外部决议（`hasCriticalDecision=true`），直接使用该结果；
    - 否则按本地暴击率随机判定。
4. 暴击倍率在本地暴击伤害基础上叠加外部事件加成倍率（若有）。
5. 法术层护盾吸收
6. 硬甲层计算（穿透判定、吸收、耐久损耗）
7. 软甲层计算（穿透判定、吸收、耐久损耗）
8. 构造并发布 `DamageCalculationEvent.Pre`

### 4.2 真实伤害分支（BYPASSES_ARMOR）

若 `damageSource.is(DamageTypeTags.BYPASSES_ARMOR)`：

- **不做**法术拆分
- **不做**元素抗性
- **不做**暴击
- 全部 `sourceDamage` 直接进入身体层（physical final）
- 护盾/护甲吸收与耐久损耗字段全部为 0
- 发布 `DamageCalculationEvent.Pre`

事件侧可通过：

- `DamageCalculationEvent#isTrueDamage()`

直接判断是否命中真实伤害标签。

---

## 5. RuntimeContext 写入与清理（现状）

写入位置：

- `LivingEntityDamageMixin#breacore$replaceDamageForActuallyHurt`
- 在拿到 `preEvent` 后立即执行：
    - `DamageRuntimeContext.pushCalculation(...)`

写入值来源：

- 来自 **Pre 事件发布完成后** 的 `preEvent` 对象
- 当前 push 使用：
    - `preEvent.getArmorDurabilityLoss()`
    - `preEvent.getHardArmorAbsorbedDamage() + preEvent.getSoftArmorAbsorbedDamage()`
    - `preEvent.getSpellAbsorbedByShield()`

清理位置：

- 同方法 `finally` 中：
    - `DamageRuntimeContext.clearCalculation(victim, breaDamageSource)`

这样 `push` / `clear` 在同一入口内可见，链路更直观。

---

## 6. 原版 actuallyHurt 阶段的短路逻辑

位置：`LivingEntityArmorDurabilityMixin#breacore$shortCircuitArmorProtectionForBrea`

当 `source instanceof BreaDamageSource` 时：

1. 从 RuntimeContext 读取/消费本次计划耐久损耗
2. 调用 `hurtArmor(...)` 执行耐久扣减
3. 应用护盾耐久损耗（`applySpellShieldLoss`）
4. 读取预计算 reduction（护甲 + 护盾）
5. 直接返回：`damage - (armorReduction + shieldReduction)`

若不是 `BreaDamageSource`，不介入，走原版。

---

## 7. 后处理（Post）阶段

`actuallyHurt` 返回后，mixin 内执行：

1. 判断 `damageApplied`
    - 当前判据：受击后血量下降或实体死亡
2. 若 `damageApplied` 为 true，写入自定义无敌帧：
    - `victim.invulnerableTime = source.getInvulnerabilityTicks()`
3. 调用：`DamageCalculator.finalizePendingForVanillaApply(victim, source, damageApplied)`
    - 拉取 `preEvent` 与已应用耐久损耗
    - 当前 `!damageApplied` 时仅将 `preEvent.finalDamage` 置 0
    - 构造并发布 `DamageCalculationEvent.Post`

---

## 8. 关键扩展点

- 伤害源扩展：`IBreaDamageSourceProvider` + `BreaDamageSourceResolveEvent`
- 护甲上下文扩展：`IBreaDamageArmorContextProvider` + `BreaDamageArmorContextEvent`
- 预结算/后结算观察：`DamageCalculationEvent.Pre/Post`
- 护盾损耗拦截：`SpellShieldHurtEvent`
- 暴击决议桥接：`CriticalDecisionEventHandler` + `CriticalDecisionRuntime`（static cache + consume-once）

---

## 9. 语义备注（当前版本）

1. `damageApplied` 的用途是“是否实际生效”判定，不等价于“是否发生耐久/护盾副作用”。
2. Runtime push 时机在 Pre 发布之后，因此 push 读取的是监听器修改后的 `preEvent` 当前值。
3. `BYPASSES_ARMOR` 分支在本实现中语义为“直达身体层真实伤害”。

---

## 10. 事件触发顺序（含从 hurtServer 开始的原版事件）

下面按“玩家对实体造成一次命中”的时间顺序给出事件链路。若某分支不命中会跳过。

### 10.1 进入伤害前（NeoForge 原版入口）

1. `CriticalHitEvent`（NeoForge，玩家近战攻击阶段）
    - `CriticalDecisionEventHandler#processCriticalDecision`（NORMAL）：
        - 若原版未暴击，按自定义暴击率补判并可设置 `event.setCriticalHit(true)`。
    - `CriticalDecisionEventHandler#recordCriticalDecision`（LOWEST）：
        - 记录最终暴击布尔与事件额外倍率到 `CriticalDecisionRuntime`。
2. `LivingIncomingDamageEvent`（NeoForge）
    - `EntityEventPublisher#postAttackIncome` 在此事件中转发/发布：
        - `EntityAttackEvent.Income`（root attacker）
        - `EntityAttackEvent.Income`（direct attacker，若存在且非同体）
3. 若 `LivingIncomingDamageEvent` 被取消：
    - `CriticalDecisionEventHandler#clearCriticalDecisionIfCanceled`（LOWEST, receiveCanceled=true）立即清理对应记录。
4. 若未被取消，继续进入 `hurtServer`。

### 10.2 hurtServer -> actuallyHurt 重定向后（Brea 管线）

3. `LivingEntityDamageMixin#breacore$replaceDamageForActuallyHurt` 触发。
4. 伤害源解析阶段触发（含 consume-once 注入暴击决议）：
    - `BreaDamageSourceResolveEvent`
5. 护甲上下文解析阶段触发：
    - `BreaDamageArmorContextEvent`
6. 预计算阶段触发：
    - `DamageCalculationEvent.Pre`
7. 调用原版 `actuallyHurt(...)`（参数可能已替换为 `BreaDamageSource` 和新伤害值）。

### 10.3 actuallyHurt 过程中（原版/NeoForge）

8. `LivingDamageEvent.Pre`（NeoForge）
    - `EntityEventPublisher#postAttackPre` 转发：
        - `EntityAttackEvent.Pre`（root attacker）
        - `EntityAttackEvent.Pre`（direct attacker，若存在且非同体）
9. 护甲减伤阶段（被 `LivingEntityArmorDurabilityMixin` 短路时，会在此阶段应用 Brea 的耐久/护盾逻辑）。
10. `LivingDamageEvent.Post`（NeoForge）
    - `EntityEventPublisher#postAttackPost` 转发：
        - `EntityAttackEvent.Post`（root attacker）
        - `EntityAttackEvent.Post`（direct attacker，若存在且非同体）

### 10.4 Brea 后处理阶段

11. `DamageCalculator.finalizePendingForVanillaApply(...)` 构造并发布：
    - `DamageCalculationEvent.Post`
12. finally 清理：
    - `DamageRuntimeContext.clearCalculation(...)`

### 10.5 可能伴随触发的其他事件

- 若发生护盾耐久扣减：
    - `SpellShieldHurtEvent`（在 `DamageArmorContext#applySpellShieldLoss` 内触发）
- 若本次命中致死：
    - `LivingDeathEvent`（NeoForge）
    - `EntityEventPublisher` 会继续转发 `EntityKillEvent.Pre/Post`

### 10.6 原版回退分支的差异

当 `BreaDamageSource` 最终解析失败（`null`）时：

- 仍会触发 10.1 与 10.3 中的原版/NeoForge事件；
- 不会触发 Brea 的
    - `BreaDamageSourceResolveEvent` 之后链路（若返回 null 且未被事件补全）
    - `BreaDamageArmorContextEvent`
    - `DamageCalculationEvent.Pre/Post`
    - RuntimeContext push/clear。

---

## 12. Damage 模块 TODO（新增）

1. **增加覆盖上述回退路径的测试**
    - 用例建议：
        - 无攻击者 / 非 living 攻击者
        - 缺穿甲 attribute
        - 缺武器 profile
        - `Element.NONE`
        - `parameters == null`
    - 验证是否符合预期设计，而非仅“能运行”。

---

## 13. `DamageArmorContext` / `BreaDamageSource` 的构建与修改途径（含默认途径）

### 13.1 `BreaDamageSource`

入口：`LivingEntityDamageMixin#breacore$tryConvertSource(victim, source)`

可构建/修改路径（按执行顺序）：

1. **原始即 BreaSource（默认直通）**
   - `source instanceof BreaDamageSource` 时直接使用。

2. **Provider 构建（自定义优先）**
   - `victim` 实现 `IBreaDamageSourceProvider` 时调用：
   - `provideBreaDamageSource(source, victim)`。
   - 返回非空则采用，返回空继续走 fallback。

3. **Fallback 构建（默认途径）**
   - `breacore$buildFallbackBreaDamageSource(source)`。
   - 数据来源：攻击者实体 + 攻击者 attribute + 武器 `WEAPON_DAMAGE_PROFILE`。
   - 默认回退：武器无组件时 `WeaponDamageProfile.GEOGRAPHY_ONLY`。
   - 构建失败（如无有效 living 攻击者/缺关键 attribute）返回 `null`。

4. **事件统一覆盖（最终决策点）**
   - `BreaDamageSourceResolveEvent(victim, source, resolved)`。
   - 即使 `resolved == null` 也会发事件，监听器可补全/替换/清空最终 `BreaDamageSource`。

额外写入点：

- `breacore$applyCriticalDecision(...)` 会在转换流程内对 `BreaDamageSource` 写入暴击决议字段。

### 13.2 `DamageArmorContext`

入口：`LivingEntityDamageMixin#breacore$resolveArmorContext(...)`

可构建/修改路径（按执行顺序）：

1. **Provider 构建（自定义优先）**
   - `victim` 实现 `IBreaDamageArmorContextProvider` 时调用：
   - `provideDamageArmorContext(damageSource, victim)`。
   - 返回非空则采用，返回空继续走默认实现。

2. **默认实现构建（默认途径）**
   - `new SimpleDamageArmorContext(rootAttacker, directAttacker, victim, weapon)`。
   - 当前默认读取：
     - 护盾：`EntityShieldAttachment`
     - 护甲/韧性/暴伤减免：受击实体 attribute
     - 元素抗性：按元素从受击实体 attribute 即时读取

3. **事件统一覆盖（最终决策点）**
   - `BreaDamageArmorContextEvent(victim, damageSource, armorContext)`。
   - 监听器可替换或包装最终上下文。

### 13.3 实践建议

- 需要“实体/职业特化”的构建逻辑：优先 Provider。
- 需要“全局最后一跳修正”：优先事件。
- 只改默认行为：优先在 fallback 或 `SimpleDamageArmorContext` 内做最小改动，并同步文档语义。
