# Material(通用材料系统)

通用材料系统由多个不同模块共同组成,相互解耦,对任意种类的操作会影响该种类下所有物品

## Material
材料本体,只能通过`new Material.Builder("xx")`进行创建

材料具有一定的[MaterialAttribute](#MaterialAttribute),这是材料变体的格式化数据源
## MaterialAttribute
材料属性的定义,是材料基本的属性信息

可重写方法进行可加性验证和格式化生成前置属性
## MaterialVariant
材料变体的定义,每个材料变体都对应一系列的材料实例,包括不限于流体,方块,物品,桶...

材料变体定义了材料实例的基本信息,包括不限于材料物量,堆叠数量
## RegisterCondition
函数式接口,用于注册时检测能否将变体用于特定材料,如果设置了多个[RegisterCondition](#RegisterCondition),仅但满足所有条件时才注册
## RegisterAction
函数式接口,用于具体的注册逻辑,通过[MaterialVariant](#MaterialVariant)和[Material](#Material)来格式化数据