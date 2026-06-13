package net.phasetranscrystal.breacore.api.registry.registrate.builder;

import net.phasetranscrystal.breacore.api.registry.registrate.BreaRegistryCore;
import net.phasetranscrystal.registrylib.RegistryCore;
import net.phasetranscrystal.registrylib.builders.ItemBuilder;

import net.minecraft.world.item.Item;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Extended {@link ItemBuilder} that exposes a {@code langCn(String)} convenience method.
 *
 * <p>
 * Returned by {@link BreaRegistryCore#item(Object, String, Function, boolean)} so that every
 * {@code .item(...)} call on a {@code BreaRegistryCore} instance automatically has access to {@code
 * .langCn("中文名")}.
 */
public class ModItemBuilder<T extends Item, P> extends ItemBuilder<T, P> {

    public static <T extends Item, P> ModItemBuilder<T, P> create(
                                                                  RegistryCore owner,
                                                                  P parent,
                                                                  String name,
                                                                  Function<Item.Properties, T> factory,
                                                                  boolean isComponentItem) {
        var builder = new ModItemBuilder<>(owner, parent, name, factory, isComponentItem);
        return (ModItemBuilder<T, P>) builder.defaultModel().defaultLang();
    }

    protected ModItemBuilder(
                             RegistryCore owner,
                             P parent,
                             String name,
                             Function<Item.Properties, T> factory,
                             boolean isComponentItem) {
        super(owner, parent, name, factory, isComponentItem);
    }

    /**
     * Adds a Simplified-Chinese translation to {@code zh_cn.json}. Sugar for {@code
     * lang(ModRegistryCore.LANG_ZH_CN, name)}.
     */
    public ModItemBuilder<T, P> langCn(@NotNull String name) {
        lang(BreaRegistryCore.LANG_ZH_CN, name);
        return this;
    }
}
