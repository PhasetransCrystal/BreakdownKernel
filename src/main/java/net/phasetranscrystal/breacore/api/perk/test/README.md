# Perk 系统测试文档

## 测试环境

### 测试物品获取
```
/give @s breacore:query_perk_item
/give @s breacore:clear_perk_item
```

### 测试装备（通过 ModifyDefaultComponentsEvent 绑定）

| 物品 | 装备槽位 | 词条 | 等级 | 测试内容 |
|------|---------|------|------|---------|
| 钻石头盔 | HEAD | test_sum | 1 | SUM叠加 |
| 钻石胸甲 | CHEST | test_sum | 3 | SUM叠加 (1+3=4) |
| 钻石护腿 | LEGS | test_max | 5 | MAX叠加 |
| 钻石鞋子 | FEET | test_max | 8 | MAX叠加 (max(5,8)=8) |
| 盾牌 | OFFHAND | test_average | 2 | AVERAGE叠加 |
| 金头盔 | HEAD | test_armor | 5 | 属性修饰器 (ADD_VALUE) |
| 金鞋子 | FEET | test_speed | 3 | 属性修饰器 (ADD_MULTIPLIED_BASE) |
| 金胸甲 | CHEST | test_health | 2 | 属性修饰器 (ADD_MULTIPLIED_TOTAL) |
| 铁头盔 | HEAD | test_event | 1 | 事件消费者 |
| 铁胸甲 | CHEST | test_combo | 2 | 属性+事件 |
| 铁护腿 | LEGS | test_combo_2 | 1 | 属性+事件 |
| 下界合金头盔 | HEAD | test_combined_a | 2 | SUM+属性+事件 |
| 下界合金胸甲 | CHEST | test_combined_b | 3 | MAX+属性+事件 |
| 钻石剑 | MAINHAND | test_sum | 1 | 多词条 |
| 钻石剑 | MAINHAND | test_combo | 1 | 多词条 |
| 钻石剑 | MAINHAND | test_combined_a | 1 | 多词条 |

## 测试用例

### 测试 1：基础附着与移除

**目的**：验证 perk 在物品被穿戴/脱下时正确触发回调

**操作步骤**：
1. 穿戴钻石头盔
2. 右键点击查询物品，验证 `attachedCalled: true`
3. 脱下钻石头盔
4. 右键点击查询物品，验证 `detachedCalled: true`

**预期结果**：
- `attachedCalled = true`（穿戴时）
- `detachedCalled = true`（脱下时）

---

### 测试 2：等级叠加 (SUM)

**目的**：验证 SUM 叠加类型正确计算等级

**操作步骤**：
1. 穿戴钻石头盔 (test_sum Lv1)
2. 穿戴钻石胸甲 (test_sum Lv3)
3. 右键点击查询物品

**预期结果**：
- `levelChangedCalled = true`
- `oldLevelValue = 1.0`
- `newLevelValue = 4.0`（1 + 3）

---

### 测试 3：等级叠加 (MAX)

**目的**：验证 MAX 叠加类型正确计算等级

**操作步骤**：
1. 穿戴钻石护腿 (test_max Lv5)
2. 穿戴钻石鞋子 (test_max Lv8)
3. 右键点击查询物品

**预期结果**：
- 等级应为 `8.0`（取最大值）

---

### 测试 4：属性修饰器

**目的**：验证三种属性操作类型的正确应用

**操作步骤**：
1. 穿戴金头盔 (test_armor, ADD_VALUE)
2. 打开属性面板，查看护甲值增加
3. 穿戴金鞋子 (test_speed, ADD_MULTIPLIED_BASE)
4. 查看移动速度变化
5. 穿戴金胸甲 (test_health, ADD_MULTIPLIED_TOTAL)
6. 查看最大生命变化

**预期结果**：
- 护甲：+10（5 * 2.0）
- 移动速度：+30%（3 * 0.1）
- 最大生命：+40%（2 * 0.2）

---

### 测试 5：事件消费者

**目的**：验证事件触发时回调执行

**操作步骤**：
1. 穿戴铁头盔 (test_event)
2. 让自己受伤（被攻击/岩浆/仙人掌）
3. 右键点击查询物品

**预期结果**：
- `eventTriggerCount > 0`

---

### 测试 6：复合功能（属性+事件）

**目的**：验证单个 perk 同时拥有属性修饰器和事件消费者

**操作步骤**：
1. 穿戴铁胸甲 (test_combo Lv2)
2. 查看属性面板，护甲韧性应 +3
3. 让自己受伤
4. 右键点击查询物品

**预期结果**：
- 护甲韧性增加
- `eventTriggerCount > 0`
- `eventTriggerPerkLevel = 2.0`

---

### 测试 7：等级变化后事件获取的等级

**目的**：验证事件触发时 `PerkInfo.level()` 返回正确的当前等级

**操作步骤**：
1. 穿戴铁胸甲 (test_combo Lv2)
2. 受伤，记录 `eventTriggerPerkLevel = 2.0`
3. 穿戴钻石剑 (test_combo Lv1)，等级变为 3
4. 再次受伤，记录 `eventTriggerPerkLevel = 3.0`

**预期结果**：
- 第一次受伤：`eventTriggerPerkLevel = 2.0`
- 第二次受伤：`eventTriggerPerkLevel = 3.0`

---

### 测试 8：多词条同时存在

**目的**：验证单个物品携带多个词条时互不影响

**操作步骤**：
1. 穿戴钻石剑（同时带有 test_sum、test_combo、test_combined_a）
2. 右键点击查询物品

**预期结果**：
- 显示三个不同的 perk 及各自的等级

---

### 测试 9：重置测试

**目的**：使用清空物品重置所有测试标志

**操作步骤**：
1. 穿戴多个装备并多次受伤
2. 右键点击清空物品
3. 右键点击查询物品

**预期结果**：
- 所有标志归零：`false`、`0`、`0.0`

## 测试 Perk 定义

### 静态变量

```java
public static boolean attachedCalled;
public static boolean detachedCalled;
public static boolean levelChangedCalled;
public static float oldLevelValue;
public static float newLevelValue;
public static int eventTriggerCount;
public static float eventTriggerPerkLevel;
```

### 词条列表

| 变量名 | ID | 叠加类型 | 功能 |
|--------|-----|---------|------|
| SUM | test_sum | SUM | 回调测试 |
| MAX | test_max | MAX | 叠加测试 |
| MIN | test_min | MIN | 叠加测试 |
| AVERAGE | test_average | AVERAGE | 叠加测试 |
| ARMOR_PERK | test_armor | SUM | ADD_VALUE |
| SPEED_PERK | test_speed | SUM | ADD_MULTIPLIED_BASE |
| HEALTH_PERK | test_health | SUM | ADD_MULTIPLIED_TOTAL |
| EVENT_PERK | test_event | SUM | 事件消费者 |
| COMBO_PERK | test_combo | SUM | 属性+事件 |
| COMBO_PERK_2 | test_combo_2 | SUM | 属性+事件 |
| COMBINED_A | test_combined_a | SUM | 属性+事件 |
| COMBINED_B | test_combined_b | MAX | 属性+事件 |

## 技术实现要点

### 属性修饰器

使用 `addTransientModifier` 而非 `addPermanentModifier`，确保属性修饰器不会被序列化。

### 装备槽位绑定

通过 `EquipmentSlotGroup` 指定 perk 生效的装备槽位：
- `HEAD` - 头盔
- `CHEST` - 胸甲
- `LEGS` - 护腿
- `FEET` - 鞋子
- `OFFHAND` - 副手
- `MAINHAND` - 主手

### 事件注册

使用 `@SubscribeEvent` + `ModifyDefaultComponentsEvent` 在游戏初始化时修改原版物品的默认组件。

### 回调执行时机

- `onAttached`：perk 首次添加到实体时
- `onDetached`：perk 从实体完全移除时
- `onLevelChanged`：perk 等级发生变化时

## 测试结果汇总

| 测试项 | 状态 |
|--------|------|
| 附着/移除回调 | 通过 |
| SUM 叠加 | 通过 |
| MAX 叠加 | 通过 |
| MIN 叠加 | 通过 |
| AVERAGE 叠加 | 通过 |
| 属性修饰器 (ADD_VALUE) | 通过 |
| 属性修饰器 (ADD_MULTIPLIED_BASE) | 通过 |
| 属性修饰器 (ADD_MULTIPLIED_TOTAL) | 通过 |
| 事件消费者 | 通过 |
| 复合功能 (属性+事件) | 通过 |
| 等级变化后 PerkInfo.level() | 通过 |
| 多词条同时存在 | 通过 |
| transientModifier 不序列化 | 通过 |
