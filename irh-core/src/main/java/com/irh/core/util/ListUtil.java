package com.irh.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ListUtil {
    /**
     *
     */
    private ListUtil() {

    }

    /**
     * 将MAP转换为KeyValuePairs的List
     *
     * @param map
     * @return
     */
    public static List<KeyValuePair> mapToKeyValuePairs(Map<String, String> map) {
        if (map == null)
            return null;
        List<KeyValuePair> retval = new ArrayList<KeyValuePair>();
        for (String key : map.keySet()) {
            KeyValuePair kvp = new KeyValuePair(key, map.get(key));
            retval.add(kvp);
        }
        return retval;
    }

    /**
     * 部分执行一些不成功的参数
     *
     * @param params
     * @param keys   键数组，比如{K1,K2}
     * @param values 每个元素是一个值数组
     */
    public static void doParamsPatch(List<List<KeyValuePair>> params, String[] keys, List<String[]> values) {
        for (String[] value : values) {
            List<KeyValuePair> kvps = new ArrayList<KeyValuePair>();
            for (int j = 0; j < keys.length; j++) {
                kvps.add(new KeyValuePair(keys[j], value[j]));
            }
            params.add(kvps);
        }
    }

}
