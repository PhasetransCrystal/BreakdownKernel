---
sidebar_position: 1
---

# BreakdownKernel（瓦解核心）

面向 Minecraft 26.1.2 的大型系列 Mod 基础框架。通过**通用材料系统**统一管理材料定义，自动生成对应的物品、方块、流体等游戏内容，大幅减少样板代码。

## 核心特性

- **通用材料系统** — 一次定义材料，自动生成锭、粒、粉、块、流体等全部变体
- **元素系统** — 内置 116+ 化学元素，含半衰期等物理属性
- **Addon 扩展机制** — `@BreaAddon` 注解自动发现子 Mod
- **数据生成** — 自动生成配方、标签、战利品表
- **灵活注册** — `RegisterCondition` / `RegisterAction` 解耦变体与注册逻辑

## 快速开始

```groovy
repositories {
    maven {url = 'https://maven.nilarea.cn/releases'}
}
// build.gradle
dependencies {
    implementation("net.phasetranscrystal:BreakdownKernal:0.1.0")
}
```

```java
// 定义一个材料
Material aluminium = new Material.Builder("aluminium")
    .ingot()          // 标记为锭材料
    .fluid()          // 标记为流体材料
    .color(0x7db9d8)
    .element(Element.AL)
    .build();
```

材料注册后，会自动生成：

- `aluminium_ingot`、`aluminium_nugget`、`aluminium_dust`
- `aluminium_block`
- `aluminium_liquid`、`aluminium_melt`

## 模块

| 模块 | 说明 |
|------|------|
| [通用材料系统](material/index.md) | Material / MaterialAttribute / MaterialVariant 核心 API |
| BreaLib | 日志、环境检测、线程安全等基础工具库 |

## 进行中任务

- [x] [通用材料系统](material/index.md)
- [ ] 配方系统
- [ ] 多方块结构
