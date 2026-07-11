---
sidebar_position: 6
---

# API 参考

BreakdownKernel 为 DLC 模组提供稳定的核心 API。以下为各模块公开接口速查。

## 模块总览

| 模块      | 包路径                          | 用途                          |
|---------|------------------------------|-----------------------------|
| 材料系统    | `breacore.api.material`      | 材料定义、元素、属性、变体               |
| 机器系统    | `breacore.api.machine`       | 机器定义、MetaMachine、特性         |
| DLC 扩展  | `breacore.api.addon`         | @BreaAddon 注解、IBreaAddon 接口 |
| 注册系统    | `breacore.api.registry`      | BreaRegistry、Registrate 扩展  |
| Perk 系统 | `breacore.api.perk`          | 实体加成、属性修改器                  |
| 事件分发    | `breacore.api.eventdispatch` | 实体事件分发器                     |
| 能量系统    | `breacore.api.energy`        | 相变能量（规划中）                   |
| 世界生成    | `breacore.api.worldgen`      | 矿石/结构生成（规划中）                |

## 核心常量

定义在 `BreaApi` 中：

| 常量  | 值         | 说明            |
|-----|-----------|---------------|
| `M` | 3,628,800 | 一单位材料的标准物质量   |
| `L` | 144       | 每材料单位的流体量（mB） |

## DLC 入口

```java

@BreaAddon("your_mod_id")
public class YourDLC implements IBreaAddon {
    // getRegistrate() / initComplete() / addElement() / addMaterial() / addMaterialVariant()
}
```

## 材料 API

参见 [通用材料系统](material/index.md)。

核心类：`Material`、`Element`、`MaterialAttribute`、`MaterialVariant`、`RegisterCondition`、`RegisterAction`。

## 机器 API

参见 [机器与多方块系统](multiblock.md)。

核心类：`MachineDefinition`、`MetaMachine`、`MachineTrait`、`IMachineFeature`。

## 数据附件

通过 `IMachineBlockEntity` 的 `MultiManagedStorage` 实现方块实体的自定义数据同步，支持 `@DescSynced`（客户端同步）和
`@Persisted`（持久化）注解。

## 相关页面

- [快速开始](quick-start.md)
- [通用材料系统](material/index.md)
- [机器与多方块系统](multiblock.md)
