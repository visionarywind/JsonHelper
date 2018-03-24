package com.github.jsonhelper;

import com.github.jsonhelper.json.GsonOptimizer;
import com.google.gson.Gson;
import com.google.gson.JsonElement;


/**
 * , Created by mi on 6/15/17.
 * <p>
 * Note : toJson... may return null
 */
public class GsonHelper {
    public static String toJson(Object object) {
        if (object == null) {
            return null;
        }
        return toJsonWithOptimizer(object, generateGsonOptimizer());
    }


    public static String toJsonForSkipField(Object object, String... skipFields) {
        if (object == null) {
            return null;
        }
        return toJsonWithOptimizer(object,
                skipFieldGsonOptimizer(skipFields));
    }

    private static String toJsonWithOptimizer(Object object, final GsonOptimizer gsonOptimizer) {
        JsonElement jsonTree = new Gson().toJsonTree(object);
        return optimizeJsonTreeWithOptimizer(jsonTree,
                gsonOptimizer);
    }

    private static String toJsonElementWithGsonAndOptimizer(Object object, Gson gson,
                                                            final GsonOptimizer gsonOptimizer) {
        JsonElement jsonTree = gson.toJsonTree(object);
        return optimizeJsonTreeWithOptimizer(jsonTree,
                gsonOptimizer);
    }

    private static String optimizeJsonTreeWithOptimizer(JsonElement jsonElement,
                                                        final GsonOptimizer gsonOptimizer) {
        return gsonOptimizer.optimizeElement(jsonElement).toString();
    }

    private static GsonOptimizer generateGsonOptimizer() {
        return new GsonOptimizer.Builder().build();
    }


    private static GsonOptimizer skipFieldGsonOptimizer(String... fields) {
        GsonOptimizer.Builder builder = new GsonOptimizer.Builder();
        if (fields != null && fields.length > 0) {
            for (String e : fields) {
                builder.skipField(e);
            }
        }
        return builder.build();
    }

    public static String optimize(Object object) {
        if (object == null) {
            return null;
        }
        JsonElement jsonTree = new Gson().toJsonTree(object);
        return generateGsonOptimizer().optimizeElement(jsonTree).toString();
    }
}
