---
sidebar_position: 3
---

# 世界生成

BreakdownKernel 的世界生成系统负责矿石放置、结构生成和生物群系扩展。

> 世界生成 API 正在开发中，以下为设计规划。

## 核心特性

- **异步生成**：不阻塞主线程，确保 TPS ≥ 19.5
- **配置方式**：JSON 数据驱动 + Biome Modifier
- **内容**：矿石生成、地表/地下结构、晶化生物群系变种

## Biome Modifier

通过 NeoForge 的 Biome Modifier 系统向已有群系注入自定义特性（如让矿石在特定群系中生成）。

## 相关页面

- [机器与多方块系统](multiblock.md) — 多方块结构
- [API 参考](2-api-reference.md) — 世界生成 API
