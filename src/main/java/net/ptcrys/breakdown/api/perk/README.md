# Perk 词条系统

## 概述

Perk 词条系统是一个为实体提供属性修饰和事件监听的机制。词条由装备提供，存储在物品堆的组件中，支持根据装备槽位提供不同的词条列表。

## 文件结构

```
breacore/api/perk/
├── Perk.java                         # Perk 定义类
├── PerkStack.java                    # Perk 堆栈（perk + 等级）
├── PerkStackingType.java             # 叠加类型枚举
├── PerkChangeType.java               # 变化类型枚举
├── PerkInfo.java                     # 上下文信息
├── PerkConsumer.java                 # 词条事件消费者
├── PerkAttributeModifier.java        # 属性修饰器记录
├── IPerkProvider.java               # 物品组件接口
├── PerkAttachment.java               # 实体附件（核心处理类）
├── event/
│   ├── PerkChangeEvent.java          # Perk 变化事件
│   └── StackPerkProviderComponentChangeEvent.java  # 组件变化事件
└── ...

common/registry/
└── AttachmentTypeRegistry.java       # AttachmentType 注册

common/event/
└── LivingEquipmentChangeEventDispatcher.java  # 事件调度器
```

## 核心类

### Perk

词条定义类，包含词条的唯一标识和叠加类型。

```java
public class Perk {
    private final Identifier id;
    private final PerkStackingType stackingType;
}
```

**方法：**

| 方法 | 说明 |
|------|------|
| `getId()` | 获取词条 ID |
| `getStackingType()` | 获取叠加类型 |
| `onAttached(entity, info)` | 词条附加时调用 |
| `onDetached(entity, info)` | 词条移除时调用 |
| `onLevelChanged(entity, oldLevel, newLevel, info)` | 等级变化时调用 |
| `getEventConsumers(info)` | 获取事件消费者列表 |
| `getAttributeModifiers(entity, level)` | 获取属性修饰器 |
| `getAttributeModifierId(operation)` | 获取属性修饰器 ID（带操作类型后缀） |
| `calculateLevel(levels)` | 根据叠加类型计算等级 |

### PerkStack

词条堆栈，记录词条及其等级。

```java
public record PerkStack(Perk perk, float level) {}
```

### PerkStackingType

叠加类型枚举。

| 值 | 说明 |
|----|------|
| `MAX` | 取最大值 |
| `SUM` | 累加求和 |
| `MIN` | 取最小值 |
| `AVERAGE` | 平均值 |

### PerkChangeType

变化类型枚举。

| 值 | 说明 |
|----|------|
| `ADD` | 新增 |
| `CHANGE` | 等级变化 |
| `REMOVE` | 移除 |

### PerkInfo

上下文信息记录。

```java
public record PerkInfo(
    Perk perk,
    float level,
    Map<EquipmentSlot, ItemStack> itemStacks
) {}
```

### PerkConsumer

词条事件消费者记录。

```java
public record PerkConsumer<T extends Event>(
    Class<T> eventType,
    boolean runWhenCancelled,
    TriConsumer<T, EventConsumer<T>, PerkInfo> triConsumer
) {}
```

### PerkAttributeModifier

属性修饰器记录。

```java
public record PerkAttributeModifier(
    Holder<Attribute> attribute,
    AttributeModifier.Operation operation,
    double value
) {
    public AttributeModifier toModifier(Identifier id) {
        return new AttributeModifier(id, value, operation);
    }
}
```

### IPerkProvider

物品组件接口，物品实现此接口以提供词条。

```java
public interface IPerkProvider extends IItemComponent {
    Map<EquipmentSlotGroup, List<PerkStack>> getPerkStacks();
}
```

## PerkAttachment

实体附件类，负责管理实体的所有词条。

### 维护的数据表

| 表名 | 类型 | 用途 |
|------|------|------|
| `perkInfos` | `Map<Perk, PerkInfo>` | perk 的上下文信息 |
| `perkLevels` | `Map<Perk, Float>` | perk 当前等级 |
| `currentEquipment` | `Map<EquipmentSlot, ItemStack>` | 当前装备槽物品 |
| `slotPerkCache` | `Map<EquipmentSlot, Map<Perk, Float>>` | 装备槽的 perk 等级缓存 |
| `perkAttributeModifiers` | `Map<Perk, Map<Identifier, Holder<Attribute>>>` | 已注册的 attribute modifier 缓存 |

### 静态方法

| 方法 | 说明 |
|------|------|
| `TYPE` | AttachmentType |
| `SYSTEM_ID` | 事件系统 ID |
| `getOrCreate(entity)` | 获取或创建附件 |
| `getPerkInfo(entity, perk)` | 获取 perk 上下文信息 |
| `getPerkLevel(entity, perk)` | 获取 perk 等级 |
| `getAllPerkLevels(entity)` | 获取所有 perk 等级 |
| `collectPerkStacks(stack, slot)` | 收集物品堆的 perk 数据 |

### 实例方法

| 方法 | 说明 |
|------|------|
| `updateEquipment(entity, slot, newStack, oldStacks, newStacks)` | 更新装备 |
| `recalculateAllPerks(entity)` | 重新计算所有 perk |

## 事件

### PerkChangeEvent

词条变化事件，通过 `NeoForge.EVENT_BUS` 发布。

```java
public class PerkChangeEvent extends Event {
    private final Entity entity;
    private final Perk perk;
    private final PerkChangeType changeType;
    private final float oldLevel;
    private final float newLevel;
    private final PerkInfo perkInfo;
}
```

### StackPerkProviderComponentChangeEvent

物品堆组件变化事件。

**重要：** 若物品堆组件变化但不触发 `LivingEquipmentChangeEvent` 时，需要手动发布此事件以保证 perk 系统及时更新。

```java
public class StackPerkProviderComponentChangeEvent extends Event {
    private final LivingEntity entity;
    private final EquipmentSlot slot;
    private final ItemStack oldStack;
    private final ItemStack newStack;
}
```

## 处理流程

```
    物品堆装备变化（LivingEquipmentChangeEvent / StackPerkProviderComponentChangeEvent）
            ↓
    LivingEquipmentChangeEventDispatcher（共享逻辑）
            ↓
    获取物品堆的所有 IPerkProvider → 遍历
            ↓
    每个 provider.getPerkStacks() → 遍历 entry
            ↓
    用 EquipmentSlotGroup.test(slot) 匹配
            ↓
    合并所有匹配的 List<PerkStack>
            ↓
    PerkAttachment.updateEquipment(entity, slot, newStack, oldStacks, newStacks)
            ↓
    按 Perk 聚合所有 EquipSlot 的 PerkStack
            ↓
    应用 StackingType 计算最终等级（对比旧等级）
            ↓
    触发 onAttached / onLevelChanged / onDetached
            ↓
    perk.getAttributeModifiers() → 更新 AttributeModifier
            ↓
    注册/注销事件消费者（仅增减时）
            ↓
    NeoForge.EVENT_BUS.post(PerkChangeEvent)
```

## 事件消费者注册

```
Perk.getEventConsumers(PerkInfo) → List<PerkConsumer<?>>
        ↓
遍历创建 EventConsumer<?>
        ↓
包装: (event, consumer) -> perkConsumer.triConsumer()
          .accept(event, consumer, PerkAttachment.getPerkInfo(entity, perk))
        ↓
path: [BreaCore.id("perk_system"), perk.getId()]
        ↓
EventDispatcher.attach(entity, wrappedConsumer)
```

## 使用示例

### 定义一个 Perk

```java
public class ExamplePerk extends Perk {
    public ExamplePerk() {
        super(BreaLib.id("example"), PerkStackingType.SUM);
    }

    @Override
    public void onAttached(LivingEntity entity, PerkInfo info) {
        // 词条附加时的逻辑
    }

    @Override
    public Collection<PerkAttributeModifier> getAttributeModifiers(LivingEntity entity, float level) {
        return List.of(
            new PerkAttributeModifier(
                Attributes.ARMOR,
                AttributeModifier.Operation.ADD_VALUE,
                level * 2.0
            )
        );
    }

    @Override
    public List<PerkConsumer<?>> getEventConsumers(PerkInfo info) {
        return List.of(
            new PerkConsumer<>(
                LivingHurtEvent.class,
                false,
                (event, consumer, perkInfo) -> {
                    // 处理事件
                }
            )
        );
    }
}
```

### 实现 IPerkProvider

```java
public class ExamplePerkProvider implements IPerkProvider {
    @Override
    public Map<EquipmentSlotGroup, List<PerkStack>> getPerkStacks() {
        return Map.of(
            EquipmentSlotGroup.HEAD, List.of(
                new PerkStack(ExamplePerk.INSTANCE, 1.0f)
            )
        );
    }
}
```

## 注意事项

1. **事件发布**：若物品堆组件变化但不触发 `LivingEquipmentChangeEvent`，必须手动发布 `StackPerkProviderComponentChangeEvent`。

2. **性能优化**：尽量少变动物品堆上的 perk 信息组件，以降低更新带来的性能消耗。

3. **属性修饰器**：一个 perk 对同一属性可以使用多个不同 `Operation` 的修饰器，系统会自动处理后缀（`/stage1`、`/stage2`、`/stage3`）。

4. **附件注册**：`PERK_CONTROLLER` 已在 `AttachmentTypeRegistry` 中注册。
