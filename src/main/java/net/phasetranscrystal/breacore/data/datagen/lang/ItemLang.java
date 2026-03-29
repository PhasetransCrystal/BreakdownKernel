package net.phasetranscrystal.breacore.data.datagen.lang;

import net.phasetranscrystal.breacore.api.tag.TagPrefix;

import com.tterrag.registrate.providers.RegistrateLangProvider;

import static net.phasetranscrystal.breacore.data.datagen.lang.LangHandler.replace;

public class ItemLang {

    public static void init(RegistrateLangProvider provider) {
        initGeneratedNames(provider);
        initItemNames(provider);
        initPerkDebugItemLang(provider);
        initPerkTooltipLang(provider);
    }

    private static void initGeneratedNames(RegistrateLangProvider provider) {
        // TagPrefix
        for (TagPrefix tagPrefix : TagPrefix.values()) {
            provider.add(tagPrefix.getUnlocalizedName(), tagPrefix.langValue);
        }
        provider.add("tagprefix.polymer.plate", "%s Sheet");
        provider.add("tagprefix.polymer.foil", "Thin %s Sheet");
        provider.add("tagprefix.polymer.nugget", "%s Chip");
        provider.add("tagprefix.polymer.dense_plate", "Dense %s Sheet");
        provider.add("tagprefix.polymer.double_plate", "Double %s Sheet");
        provider.add("tagprefix.polymer.tiny_dust", "Tiny Pile of %s Pulp");
        provider.add("tagprefix.polymer.small_dust", "Small Pile of %s Pulp");
        provider.add("tagprefix.polymer.dust", "%s Pulp");
        provider.add("tagprefix.polymer.ingot", "%s Ingot");
    }

    private static void initItemNames(RegistrateLangProvider provider) {
        replace(provider, "item.breacore.tungsten_steel_fluid_cell", "%s Tungstensteel Cell");
        provider.add("item.breacore.perk_debug", "Perk Debugger");
    }

    private static void initPerkDebugItemLang(RegistrateLangProvider provider) {
        provider.add("item.breacore.perk_debug.tooltip.entity", "Right-click entity: Shows entity's perk status");
        provider.add("item.breacore.perk_debug.tooltip.player", "Sneak + Right-click: Shows player's perk status");
        provider.add("item.breacore.perk_debug.title", "Perk Debug: %s");
        provider.add("item.breacore.perk_debug.no_perks", "No active perks");
        provider.add("item.breacore.perk_debug.perk_id", "Perk: %s");
        provider.add("item.breacore.perk_debug.level", "  Level: %s");
        provider.add("item.breacore.perk_debug.stacking_type", "  Stacking: %s");
        provider.add("item.breacore.perk_debug.attribute_modifiers", "  Attribute Modifiers:");
        provider.add("item.breacore.perk_debug.attribute_modifier_line", "    - %s %s %s");
        provider.add("item.breacore.perk_debug.event_consumers", "  Event Consumers: %s registered");
    }

    private static void initPerkTooltipLang(RegistrateLangProvider provider) {
        provider.add("breacore.quench.tooltip.equipped_header", "When equipped:");
        provider.add("breacore.quench.tooltip.level_format", "Lv.%s");
    }
}
