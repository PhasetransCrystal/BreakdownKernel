package net.ptcrys.breakdown.api.misc

import net.ptcrys.breakdown.common.data.translation.ComponentSlang
import net.ptcrys.breakdown.common.data.translation.MachineSlang

import kotlin.reflect.KProperty1

open class AutoInitialize<T> {
    fun originInit() {
        ComponentSlang.init()
        MachineSlang.init()
    }
    open fun init() {}
    init {
        // 自动初始化所有非 const 的 val 属性
        this::class.members
            .filterIsInstance<KProperty1<T, *>>()
            .filter { !it.isConst }
            .forEach { property ->
                try {
                    @Suppress("UNCHECKED_CAST")
                    property.get(this as T)
                } catch (ignore: Exception) {
                }
            }
    }
}
object AutoInitializeImpl : AutoInitialize<AutoInitializeImpl>()
