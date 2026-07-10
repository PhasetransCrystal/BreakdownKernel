---
sidebar_position: 2
---

# 快速开始

本指南帮助你搭建基于 BreakdownKernel 的 DLC 开发环境。

## 环境要求

| 工具        | 版本           |
|-----------|--------------|
| JDK       | Java 25      |
| Gradle    | 8.x（Wrapper） |
| Minecraft | 26.1         |
| Mod 平台    | NeoForge     |

## 添加依赖

在 DLC 模组的 `build.gradle` 中添加：

```groovy
repositories {
    maven { url = 'https://maven.nilarea.cn/releases' }
}

dependencies {
    implementation("net.phasetranscrystal:BreakdownKernal:0.1.0")
}
```

## 项目结构

推荐 DLC 采用以下结构：

```
YourDLC/
├── src/main/java/com/yourname/dlc/
├── src/main/resources/
│   └── data/yourdlc/          # 数据包（配方、结构等）
├── build.gradle
└── settings.gradle
```

## 依赖关系

```
Your DLC
  └── BreakdownKernel（公开 API）
        └── BreaLib（内置，闭源，通过 BreakdownKernel API 间接使用）
```

DLC **只需依赖 BreakdownKernel**，不能直接依赖 BreaLib。所有底层能力通过 BreakdownKernel 的公开 API 访问。

## 注册为 DLC

使用 `@BreaAddon` 注解并实现 `IBreaAddon` 接口：

```java
@BreaAddon("your_mod_id")
public class YourDLC implements IBreaAddon {

    @Override
    public BreaRegistryCore getRegistrate() { /* ... */ }

    @Override
    public void initComplete() { /* 初始化完成回调 */ }

    @Override
    public void addElement() { /* 注册自定义元素 */ }

    @Override
    public void addMaterial() { /* 注册自定义材料 */ }

    @Override
    public void addMaterialVariant() { /* 注册自定义变体 */ }
}
```

系统通过 `AddonFinder` 自动扫描并加载所有 `@BreaAddon` 注解的类。

## 下一步

- [通用材料系统](material/index.md) — 定义你的材料
- [机器系统](multiblock.md) — 创建自定义机器
- [API 参考](api-reference.md) — 完整接口速查
