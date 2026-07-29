---
sidebar_position: 11
---

# 元素系统

BreakdownKernel 内置了完整的化学元素系统，包含 116+ 种元素及其物理属性。元素通过 `Element` 枚举定义，可与材料绑定以自动生成化学式等元数据。

## 元素属性

每个 `Element` 包含以下属性：

| 属性        | 类型   | 说明                      |
|-------------|--------|---------------------------|
| `protons`   | long   | 质子数（原子序数）        |
| `neutrons`  | long   | 中子数                    |
| `electrons` | long   | 电子数                    |
| `halflife`  | long   | 半衰期（秒），-1 表示稳定 |
| `mass`      | long   | 相对原子质量（*1000）     |
| `name`      | String | 元素英文名                |
| `symbol`    | String | 元素符号                  |

## 使用方式

### 绑定到材料

```java
Material aluminium = new Material.Builder("aluminium")
        .element(Element.AL)
        .formula("Al")
        .build();

Material water = new Material.Builder("water")
        .element(Element.H)
        .element(Element.O)
        .formula("H2O")
        .build();
```

### 查询元素信息

```java
Element iron = Element.FE;
long atomicNumber = iron.protons();  // 26
long mass = iron.mass();             // 55845 (55.845 * 1000)
long halflife = iron.halflife();     // -1 (稳定同位素 Fe-56)
```

### 同位素支持

元素系统支持指定同位素：

```java
Material uranium235 = new Material.Builder("uranium_235")
        .element(Element.U.withNeutrons(143))
        .formula("U-235")
        .build();
```

## 常用元素速查

| 符号 | 名称      | 原子序数 | 质量    |
|------|-----------|----------|---------|
| H    | Hydrogen  | 1        | 1.008   |
| C    | Carbon    | 6        | 12.011  |
| O    | Oxygen    | 8        | 15.999  |
| Al   | Aluminium | 13       | 26.982  |
| Fe   | Iron      | 26       | 55.845  |
| Cu   | Copper    | 29       | 63.546  |
| Ag   | Silver    | 47       | 107.868 |
| Au   | Gold      | 79       | 196.967 |
| U    | Uranium   | 92       | 238.029 |
