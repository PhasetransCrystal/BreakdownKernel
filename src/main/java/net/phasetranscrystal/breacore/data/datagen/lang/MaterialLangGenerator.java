package net.phasetranscrystal.breacore.data.datagen.lang;

import net.phasetranscrystal.breacore.api.BreaApi;

import com.tterrag.registrate.providers.RegistrateLangProvider;

import static net.phasetranscrystal.brealib.util.FormattingUtil.toEnglishName;

public class MaterialLangGenerator {

    public static void generate(RegistrateLangProvider provider, final String modId) {
        BreaApi.materialManager.stream()
                .filter(mat -> mat.getModid().equals(modId))
                .forEach(material -> {
                    provider.add(material.getUnlocalizedName(), toEnglishName(material.getName()));
                });
    }
}
