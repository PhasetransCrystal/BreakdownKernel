package net.phasetranscrystal.breacore.common.event;

import net.phasetranscrystal.breacore.BreakdownCore;
import net.phasetranscrystal.breacore.api.event.EntityAttackEvent;
import net.phasetranscrystal.breacore.api.event.EntityKillEvent;
import net.phasetranscrystal.breacore.api.event.GatherEntityDistributeEvent;
import net.phasetranscrystal.breacore.api.eventdispatch.EventDispatcher;
import net.phasetranscrystal.breacore.api.eventdispatch.IEntityAboutEvent;

import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.*;
import net.neoforged.neoforge.event.entity.item.ItemExpireEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.*;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.phasetranscrystal.breacore.api.perk.event.PerkChangeEvent;
import net.phasetranscrystal.breacore.api.perk.event.StackPerkProviderComponentChangeEvent;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 实体事件发布器。
 *
 * <p>
 * 负责监听全局事件总线并将其转发到相关实体的 {@link net.phasetranscrystal.breacore.api.eventdispatch.EventDistributor}。
 *
 * <p>
 * <b>转发类型：</b>
 * <ul>
 * <li>直接转发：事件直接关联到实体（如 {@link EntityJoinLevelEvent}）</li>
 * <li>实体提取转发：需要从事件中提取相关实体（如 {@link UseItemOnBlockEvent}）</li>
 * <li>自定义事件转发：创建适配事件并转发到多个实体（如攻击、击杀事件）</li>
 * </ul>
 */
@EventBusSubscriber(modid = BreakdownCore.MOD_ID)
public final class EntityEventPublisher {

    private static final Consumer<EntityEvent> DISTRIBUTE_CONSUMER = EventDispatcher::dispatchSelf;

    /**
     * 初始化事件发布器。
     *
     * <p>
     * 应在 mod 初始化时调用。
     */
    public static void bootstrap() {
        addEntityEventListener(EntityJoinLevelEvent.class);
        addEntityEventListener(EntityTickEvent.Post.class);
        addEntityEventListener(LivingIncomingDamageEvent.class);
        addEntityEventListener(LivingDamageEvent.Pre.class);
        addEntityEventListener(LivingDamageEvent.Post.class);

        addEntityEventListener(ItemTossEvent.class);
        addEntityEventListener(ItemExpireEvent.class);
        addEntityEventListener(AnimalTameEvent.class);
        addEntityEventListener(ArmorHurtEvent.class);
        addEntityEventListener(LivingChangeTargetEvent.class);
        addEntityEventListener(LivingDestroyBlockEvent.class);
        addEntityEventListener(LivingEntityUseItemEvent.Start.class);
        addEntityEventListener(LivingEntityUseItemEvent.Stop.class);
        addEntityEventListener(LivingEntityUseItemEvent.Finish.class);
        addEntityEventListener(LivingEntityUseItemEvent.Tick.class);
        addEntityEventListener(LivingEquipmentChangeEvent.class);
        addEntityEventListener(LivingGetProjectileEvent.class);
        addEntityEventListener(LivingHealEvent.class);
        addEntityEventListener(MobEffectEvent.Applicable.class);
        addEntityEventListener(MobEffectEvent.Added.class);
        addEntityEventListener(MobEffectEvent.Expired.class);
        addEntityEventListener(MobEffectEvent.Remove.class);

        addEntityExtractedListener(AttackEntityEvent.class, e -> List.of(e.getEntity(), e.getTarget()));
        addEntityExtractedListener(ItemEntityPickupEvent.Pre.class, e -> List.of(e.getPlayer(), e.getItemEntity()));
        addEntityExtractedListener(ItemEntityPickupEvent.Post.class, e -> List.of(e.getPlayer(), e.getItemEntity()));
        addEntityExtractedListener(PlayerInteractEvent.EntityInteractSpecific.class, e -> List.of(e.getEntity(), e.getTarget()));
        addEntityExtractedListener(PlayerInteractEvent.RightClickItem.class, e -> List.of(e.getEntity()));
        addEntityExtractedListener(UseItemOnBlockEvent.class, e -> e.getPlayer() != null ? List.of(e.getPlayer()) : List.of());

        addEntityEventListener(PlayerEvent.BreakSpeed.class);
        addEntityEventListener(PlayerEvent.HarvestCheck.class);
        addEntityEventListener(PlayerEvent.Clone.class);
        addEntityEventListener(PlayerEvent.PlayerLoggedInEvent.class);
        addEntityEventListener(PlayerEvent.PlayerLoggedOutEvent.class);
        addEntityEventListener(PlayerInteractEvent.LeftClickBlock.class);
        addEntityEventListener(PlayerInteractEvent.LeftClickEmpty.class);
        addEntityEventListener(PlayerInteractEvent.RightClickBlock.class);
        addEntityEventListener(PlayerInteractEvent.RightClickEmpty.class);
        addEntityEventListener(PlayerWakeUpEvent.class);
        addEntityEventListener(PlayerXpEvent.LevelChange.class);
        addEntityEventListener(PlayerXpEvent.PickupXp.class);
        addEntityEventListener(PlayerXpEvent.XpChange.class);
        addEntityEventListener(EntityInvulnerabilityCheckEvent.class);
        addEntityEventListener(EntityMountEvent.class);
        addEntityEventListener(EntityStruckByLightningEvent.class);
        addEntityEventListener(EntityTeleportEvent.class);
        addEntityEventListener(ProjectileImpactEvent.class);

        //perk system
        addEntityEventListener(PerkChangeEvent.class);
        addEntityEventListener(StackPerkProviderComponentChangeEvent.class);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide()) {
            NeoForge.EVENT_BUS.post(new GatherEntityDistributeEvent(event.getEntity()));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void postAttackIncome(LivingIncomingDamageEvent event) {
        boolean cancelFlag = false;
        if (event.getSource().getEntity() != null) {
            cancelFlag = NeoForge.EVENT_BUS.post(
                    new EntityAttackEvent.Income(event.getSource().getEntity(), event, false)).isCanceled();
        }
        if (!event.getSource().isDirect() && event.getSource().getDirectEntity() != null) {
            cancelFlag = NeoForge.EVENT_BUS.post(
                    new EntityAttackEvent.Income(event.getSource().getDirectEntity(), event, true)).isCanceled() || cancelFlag;
        }
        event.setCanceled(cancelFlag);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void postAttackPre(LivingDamageEvent.Pre event) {
        if (event.getSource().getEntity() != null) {
            NeoForge.EVENT_BUS.post(new EntityAttackEvent.Pre(event.getSource().getEntity(), event, false));
        }
        if (!event.getSource().isDirect() && event.getSource().getDirectEntity() != null) {
            NeoForge.EVENT_BUS.post(new EntityAttackEvent.Pre(event.getSource().getDirectEntity(), event, true));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void postAttackPost(LivingDamageEvent.Post event) {
        if (event.getSource().getEntity() != null) {
            NeoForge.EVENT_BUS.post(new EntityAttackEvent.Post(event.getSource().getEntity(), event, false));
        }
        if (!event.getSource().isDirect() && event.getSource().getDirectEntity() != null) {
            NeoForge.EVENT_BUS.post(new EntityAttackEvent.Post(event.getSource().getDirectEntity(), event, true));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void preKill(LivingDeathEvent event) {
        if (event.getSource().getEntity() != null) {
            NeoForge.EVENT_BUS.post(new EntityKillEvent.Pre(event.getSource().getEntity(), event, false));
        }
        if (!event.getSource().isDirect() && event.getSource().getDirectEntity() != null) {
            NeoForge.EVENT_BUS.post(new EntityKillEvent.Pre(event.getSource().getDirectEntity(), event, true));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void postKill(LivingDeathEvent event) {
        if (event.getSource().getEntity() != null) {
            NeoForge.EVENT_BUS.post(new EntityKillEvent.Post(event.getSource().getEntity(), event, false));
        }
        if (!event.getSource().isDirect() && event.getSource().getDirectEntity() != null) {
            NeoForge.EVENT_BUS.post(new EntityKillEvent.Post(event.getSource().getDirectEntity(), event, true));
        }
        event.setCanceled(false);
    }

    @SuppressWarnings("unchecked")
    public static <T extends EntityEvent> void addEntityEventListener(Class<T> eventType) {
        NeoForge.EVENT_BUS.addListener(eventType, (Consumer<T>) DISTRIBUTE_CONSUMER);
    }

    public static <T extends Event & IEntityAboutEvent> void addCustomEventListener(Class<T> eventType) {
        NeoForge.EVENT_BUS.addListener(eventType, EventDispatcher::dispatchSelfCustom);
    }

    public static <T extends Event> void addEntityExtractedListener(
                                                                    Class<T> eventType,
                                                                    Function<T, java.util.List<Entity>> extractor) {
        NeoForge.EVENT_BUS.addListener(eventType, event -> {
            for (Entity entity : extractor.apply(event)) {
                EventDispatcher.dispatch(entity, event);
            }
        });
    }

    // @TestOnly
    // @SubscribeEvent
    // public static void onGather(GatherEntityDistributeEvent event) {
    // Entity entity = event.getEntity();
    //
    // if (entity instanceof Player player) {
    // // 注册右键物品监听
    // EventConsumer<PlayerInteractEvent.RightClickItem> useItem = EventConsumer.of(
    // PlayerInteractEvent.RightClickItem.class,
    // new Identifier[]{
    // Identifier.fromNamespaceAndPath("mymod", "player_interact"),
    // Identifier.fromNamespaceAndPath("mymod", "using")
    // },
    // false,
    // (e, self) -> {
    // player.sendSystemMessage(Component.literal("使用了物品"));
    // self.removeFrom(player);
    // }
    // );
    // EventDispatcher.attach(player, useItem);
    //
    // EventDispatcher.attach(
    // player,
    // AttackEntityEvent.class,
    // false,
    // (e, self) -> {
    // player.sendSystemMessage(Component.literal("造成伤害!"));
    // },
    // Identifier.fromNamespaceAndPath("mymod", "combat/damage"),
    // Identifier.fromNamespaceAndPath("doing", "damage")
    // );
    //
    // // 注册伤害监听（使用便捷方法）
    // EventDispatcher.attach(
    // player,
    // LivingDamageEvent.Post.class,
    // false,
    // (e, self) -> {
    // player.sendSystemMessage(Component.literal("受到伤害: " + e.getNewDamage()));
    // EventDispatcher.get(player).detachSubtree(Identifier.fromNamespaceAndPath("mymod", "combat/damage"));
    // },
    // Identifier.fromNamespaceAndPath("mymod", "combat/damage")
    // );
    // }
    // }
}
