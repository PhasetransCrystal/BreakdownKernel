package net.phasetranscrystal.breacore.mixins;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.damagesource.DamageContainer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Stack;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {

    @Invoker("actuallyHurt")
    void breacore$invokeActuallyHurt(ServerLevel level, DamageSource source, float damage);

    @Accessor("damageContainers")
    Stack<DamageContainer> breacore$getDamageContainers();

    @Accessor("damageContainers")
    void breacore$setDamageContainers(Stack<DamageContainer> damageContainers);

    @Accessor("lastHurt")
    float breacore$getLastHurt();

    @Accessor("lastHurt")
    void breacore$setLastHurt(float lastHurt);

    @Accessor("lastDamageSource")
    void breacore$setLastDamageSource(DamageSource source);

    @Accessor("lastDamageStamp")
    void breacore$setLastDamageStamp(long gameTime);
}
