package net.phasetranscrystal.breacore.data.lang;

import net.phasetranscrystal.registrylib.datagen.provider.RegistryLibLangProvider;

import net.phasetranscrystal.breacore.api.annotation.TranslationKeyProvider;
import net.phasetranscrystal.breacore.api.lang.CNEN;

import net.neoforged.neoforge.common.data.LanguageProvider;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.Map;

public class LangHandler {

    private static final Map<String, CNEN> LANGS = new Object2ObjectOpenHashMap<>();

    private static void addCNEN(String key, CNEN CNEN) {
        if (LANGS.containsKey(key)) throw new IllegalArgumentException("Duplicate key: " + key);
        LANGS.put(key, CNEN);
    }

    public static void addCNEN(String key, String cn, String en) {
        addCNEN(key, new CNEN(cn, en));
    }

    public static void addCN(String key, String cn) {
        addCNEN(key, cn, null);
    }

    public static void init() {
        TranslationKeyProvider.LANG.forEach(LangHandler::addCNEN);
        addCNEN("key.breacore.nightvision", "夜视开关", "Night Vision Toggle");
    }

    public static void enInitialize(LanguageProvider provider) {
        init();
        MachineLang.init();
        BlockLang.init();
        ItemLang.init();
        LANGS.forEach((k, v) -> {
            if (v.en() == null) return;
            provider.add(k, v.en());
        });
    }

    public static void cnInitialize(RegistryLibLangProvider provider) {
        LANGS.forEach((k, v) -> {
            if (v.cn() == null) return;
            provider.add(k, v.cn());
        });
    }
}
