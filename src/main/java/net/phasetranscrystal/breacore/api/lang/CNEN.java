package net.phasetranscrystal.breacore.api.lang;

import net.phasetranscrystal.breacore.api.annotation.DataGeneratorScanned;
import net.phasetranscrystal.breacore.api.annotation.RegisterLanguage;

@DataGeneratorScanned
public record CNEN(String cn, String en) {

    @RegisterLanguage(
                      cn = "拆除模式",
                      en = "Demolition Mode")
    public static final String DEMOLITION = "breacore.auto_build.demolition_mode";
    @RegisterLanguage(
                      cn = "模块搭建",
                      en = "Module Build")
    public static final String MODULE = "breacore.auto_build.module";
    @RegisterLanguage(
                      cn = "镜像搭建",
                      en = "Mirror Build")
    public static final String FLIP = "breacore.auto_build.flip";
    @RegisterLanguage(
                      cn = "替换模式",
                      en = "Replace Mode")
    public static final String REPLACE = "breacore.auto_build.replace";
    @RegisterLanguage(
                      cn = "替换等级方块为设置的等级方块",
                      en = "Replace Tier Block with the set block")
    public static final String REPLACE_A = "breacore.auto_build.replace.a";
    @RegisterLanguage(
                      cn = "等级方块",
                      en = "Tiered Block")
    public static final String TIER = "breacore.auto_build.tier";

    public static CNEN create(String var0, String var1) {
        return new CNEN(var0, var1);
    }
}
