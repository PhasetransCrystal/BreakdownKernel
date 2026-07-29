# EventDispatch 模块

## 概述

实体事件分发系统，用于将全局事件转发至相关联的实体，并执行附加于实体上的事件消费器。

---

## 核心概念

### EventConsumer (事件消费器)

记录类，表示附加在实体上的事件处理单元。

```java
public record EventConsumer(
    Class<? extends Event> eventType,  // 处理的事件类型
    Identifier[] path,                  // 路径标识（用于批量操作）
    boolean runWhenCancelled,           // 事件取消时是否执行
    BiConsumer<Event, EventConsumer> handler  // 处理逻辑
)
```

---

## 添加消费器

```java
// 方式1：创建消费器后附加
EventConsumer consumer = EventConsumer.of(
    PlayerInteractEvent.RightClickItem.class,
    false,  // 不在事件取消时执行
    (event, self) -> {
        // 处理逻辑
        // event: 事件实例
        // self: 消费器自身引用
    }
);
EventDispatcher.attach(entity, consumer);

// 方式2：使用便捷方法直接附加
EventDispatcher.attach(
    entity,
    PlayerInteractEvent.RightClickItem.class,
    false,
    (event, self) -> { /* ... */ }
);
```

---

## 移除消费器

```java
// 方式1：通过实体移除
EventDistributor distributor = entity.getData(AttachmentTypeRegistry.EVENT_DISTRIBUTOR.get());
distributor.detachConsumer(consumer);

// 方式2：通过消费器自身移除
consumer.removeFrom(entity);
```

---

## 路径树功能

路径用于对消费器进行分组管理，支持批量操作。

### 带路径的添加

```java


Identifier[] path = {
        Identifier.fromNamespaceAndPath("mymod", "combat"),
        Identifier.fromNamespaceAndPath("mymod", "damage")
};

EventConsumer consumer = EventConsumer.of(
        LivingDamageEvent.Post.class,
        path,
        false,
        (event, self) -> { /* ... */ }
);

EventDistributor distributor = entity.getData(AttachmentTypeRegistry.EVENT_DISTRIBUTOR.get());
distributor.

attachConsumer(consumer);

// 或使用便捷方法
EventDistributor distributor = entity.getData(AttachmentTypeRegistry.EVENT_DISTRIBUTOR.get());
distributor.

attachConsumer(
        LivingDamageEvent.Post .class,
    false,
            (event, self) ->{ /* ... */ },
        Identifier.

fromNamespaceAndPath("mymod","combat"),
    Identifier.

fromNamespaceAndPath("mymod","damage")
);

//另外，EventDispatcher内也提供合并写法：
        EventDispatcher.

attach(entity, consumer);

//其余同理，EventDispatcher均存有简便写法
```

### 路径移除操作

```java
EventDistributor distributor = entity.getData(AttachmentTypeRegistry.EVENT_DISTRIBUTOR.get());
// 精确路径移除：移除指定路径下的所有消费器
distributor.detachPath(Identifier.fromNamespaceAndPath("mymod", "combat"));  //方法1

// 子树移除：移除路径及其所有子路径下的消费器
distributor.detachSubtree(Identifier.fromNamespaceAndPath("mymod", "fight"));//方法2

// 移除所有消费器
distributor.detachAll();
```


```
root
├── mymod:combat/
│   ├── CONSUMER1          ← 被方法1移除
│   ├── CONSUMER2          ← 被方法1移除
│   ├── mymod:damage/      ← 不被移除
│   └── mymod:defense/     ← 不被移除
└── mymod:fight/           ← detachSubtree(mymod:fight) 移除整棵子树，包括CONSUMER3与CONSUMER4
    ├── mymod:common/      ← 空目录将被自动清除
    │    └── CONSUMER3     ← 被方法2移除
    └── CONSUMER4          ← 被方法2移除
```

---

## 事件转发

系统默认支持原版大部分事件的自动转发，详细列表请查看 [EntityEventPublisher.java](../../common/eventdispatch/EntityEventPublisher.java)。

如需添加自定义事件转发，可使用以下方法：

### 继承自 EntityEvent 的事件 (addEntityEventListener)

```java
// YourCustomEvent extends EntityEvent
EntityEventPublisher.addEntityEventListener(YourCustomEvent.class);
```

### 实现 IEntityAboutEvent 接口的事件 (addCustomEventListener)

```java
// YourCustomEvent implements IEntityAboutEvent
EntityEventPublisher.addCustomEventListener(YourCustomEvent.class);
```

---

## 完整示例

预期效果：
- 玩家进入游戏后首次对空气右键物品会提示“使用了物品”
- 玩家攻击实体会提示“造成伤害！”
- 玩家进入游戏后首次受到伤害会提示“受到伤害”并显示伤害数值，在这之后造成伤害时不会再显示“造成伤害！”

```java
@TestOnly
@SubscribeEvent
public static void onGather(GatherEntityDistributeEvent event) {
    Entity entity = event.getEntity();

    if (entity instanceof Player player) {
        // 注册右键物品监听
        EventConsumer<PlayerInteractEvent.RightClickItem> useItem = EventConsumer.of(
                PlayerInteractEvent.RightClickItem.class,
                new Identifier[]{
                        Identifier.fromNamespaceAndPath("mymod", "player_interact"),
                        Identifier.fromNamespaceAndPath("mymod", "using")
                },
                false,
                (e, self) -> {
                    player.sendSystemMessage(Component.literal("使用了物品"));
                    //使用完成后快速删除自身的监听
                    self.removeFrom(player);
                }
        );
        EventDispatcher.attach(player, useItem);

        EventDispatcher.attach(
                player,
                AttackEntityEvent.class,
                false,
                (e, self) -> {
                    player.sendSystemMessage(Component.literal("造成伤害!"));
                },
                Identifier.fromNamespaceAndPath("mymod", "combat/damage"),
                Identifier.fromNamespaceAndPath("doing", "damage")
        );

        // 注册伤害监听（使用便捷方法）
        EventDispatcher.attach(
                player,
                LivingDamageEvent.Post.class,
                false,
                (e, self) -> {
                    player.sendSystemMessage(Component.literal("受到伤害: " + e.getNewDamage()));
                    EventDispatcher.get(player).detachSubtree(Identifier.fromNamespaceAndPath("mymod", "combat/damage"));
                },
                Identifier.fromNamespaceAndPath("mymod", "combat/damage")
        );
    }
}
```

```java
// 在 mod 初始化时注册自定义事件
public void init() {
    EntityEventPublisher.bootstrap();
    
    // 添加自定义事件监听（继承自 EntityEvent）
    EntityEventPublisher.addEntityEventListener(YourCustomEvent.class);
    
    // 添加自定义事件监听（实现 IEntityAboutEvent）
    EntityEventPublisher.addCustomEventListener(YourCustomEvent.class);
}
```
