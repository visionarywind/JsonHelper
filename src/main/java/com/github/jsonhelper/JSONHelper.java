package com.github.jsonhelper;

import com.github.jsonhelper.json.JSONOptimizer;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by mi on 6/16/17.
 */
public class JSONHelper {
    public static JSONObject optimizeObject(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        return optimizeObjectWithOptimizer(jsonObject, generateJsonOptimizer());
    }

    public static JSONArray optimizeArray(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }
        return optimizeArrayWithOptimizer(jsonArray, generateJsonOptimizer());
    }

    private static JSONObject optimizeObjectWithOptimizer(JSONObject jsonObject,
                                                          final JSONOptimizer jsonOptimizer) {
        jsonOptimizer.optimizeObject(jsonObject);
        return jsonObject;
    }

    private static JSONArray optimizeArrayWithOptimizer(JSONArray jsonArray,
                                                        final JSONOptimizer jsonOptimizer) {
        jsonOptimizer.optimizeArray(jsonArray);
        return jsonArray;
    }

    private static JSONOptimizer generateJsonOptimizer() {
        return new JSONOptimizer.Builder().build();
    }
}
