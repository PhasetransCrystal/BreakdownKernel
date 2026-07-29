package net.ptcrys.breakdown.api.registry;

import net.ptcrys.breakdown.BreaLib;
import net.ptcrys.breakdown.api.annotation.DataGeneratorScanned;
import net.ptcrys.breakdown.api.annotation.RegisterLanguage;
import net.ptcrys.breakdown.api.annotation.Scanned;
import net.ptcrys.breakdown.api.lang.CNEN;

import net.neoforged.fml.ModList;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.objectweb.asm.Type;

import java.util.Map;
import java.util.Objects;

public final class ScanningClass {

    public static final Map<String, CNEN> LANG = BreaLib.isDataGen() ? new Object2ObjectOpenHashMap<>() : null;
    static {
        long millis = System.currentTimeMillis();
        var scannedType = Type.getType(Scanned.class);
        var dataGeneratorScannedType = LANG == null ? null : Type.getType(DataGeneratorScanned.class);

        for (var modFileScanData : ModList.get().getAllScanData()) {
            for (var annotationData : modFileScanData.getAnnotations()) {
                var annotationType = annotationData.annotationType();
                if (Objects.equals(annotationType, scannedType) || dataGeneratorScannedType != null && Objects.equals(annotationType, dataGeneratorScannedType)) {
                    try {
                        var forName = Class.forName(annotationData.memberName());
                        for (var field : forName.getDeclaredFields()) {
                            if (LANG != null && field.isAnnotationPresent(RegisterLanguage.class)) {
                                var registerLanguage = field.getAnnotation(RegisterLanguage.class);
                                try {
                                    assert registerLanguage != null;
                                    var key = registerLanguage.key();
                                    if (key.isEmpty()) {
                                        var prefix = registerLanguage.namePrefix();
                                        if (!prefix.isEmpty()) {
                                            key = prefix + "." + field.getName();
                                        } else {
                                            field.setAccessible(true);
                                            key = (String) field.get((Object) null);
                                            var valuePrefix = registerLanguage.valuePrefix();
                                            if (!valuePrefix.isEmpty()) {
                                                key = valuePrefix + "." + key;
                                            }
                                        }
                                    }

                                    LANG.put(key, new CNEN(registerLanguage.cn(), registerLanguage.en()));
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    } catch (NoClassDefFoundError | ClassNotFoundException _) {}
                }
            }
        }
        BreaLib.LOGGER.info("ScanningClass init time: {}ms", System.currentTimeMillis() - millis);
    }

    public static void init() {}
}
