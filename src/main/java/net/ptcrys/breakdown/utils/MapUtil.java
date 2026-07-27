package net.ptcrys.breakdown.utils;

import java.util.Map;
import java.util.function.Function;

public class MapUtil {

    public static <F, S> void mapKey(Map<F, S> map, Function<F, S> function) {
        for (Map.Entry<F, S> entry : map.entrySet()) {
            entry.setValue(function.apply(entry.getKey()));
        }
    }
}
