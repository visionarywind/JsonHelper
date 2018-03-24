package com.github.jsonhelper.json;

import com.google.common.base.Predicate;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Map;
import java.util.Set;

/**
 * Created by mi on 6/26/17.
 *
 * important: 1. Map will be serialize as Object -
 * "'mapName':{'key_name':value}" key -> key string value -> val primitive or
 * object So, if value is a primitive type, need add mapName to skipField, or
 * k-v pair may be removed e.g: map('0:0', '1:1') ---> {"map":{"1":1}}
 *
 * 2. optimizeLevel - OPTIMIZE_LEVEL_OBJECTS_AND_ARRAYS Default value is
 * OPTIMIZE_LEVEL_OBJECTS OPTIMIZE_LEVEL_OBJECTS_AND_ARRAYS is more radical,
 * remove element in the list e.g: (0, null, 1) ---> [1]
 */
public class GsonOptimizer extends
		CommonOptimizer<JsonObject, JsonArray, JsonElement> {
	GsonOptimizer(final Set<String> skippedFields,
			final Set<String> removingFields,
			final Predicate<JsonElement> valueFilter, int optimizeLevel) {
		super(skippedFields, removingFields, valueFilter, optimizeLevel);
	}

	@Override
	protected boolean filterElement(JsonElement jsonElement) {
		if (jsonElement.isJsonNull()) {
			return true;
		} else if (jsonElement.isJsonPrimitive()) {
			JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
			if (valueFilter.apply(jsonPrimitive)) {
				return true;
			}
		} else if (jsonElement.isJsonObject()) {
			JsonObject jsonObj = jsonElement.getAsJsonObject();
			if (jsonObj.entrySet().isEmpty()) {
				return true;
			} else {
				optimizeObject(jsonElement.getAsJsonObject());
			}
		} else { // val.isJsonArray()
			JsonArray jsonArray = jsonElement.getAsJsonArray();
			if (jsonArray.size() == 0) {
				return true;
			} else {
				optimizeArray(jsonArray);
			}
		}
		return false;
	}

	public JsonElement optimizeElement(JsonElement jsonElement) {
		if (jsonElement == null) {
			return null;
		}
		if (jsonElement.isJsonNull()) {
			return jsonElement;
		}

		if (jsonElement.isJsonPrimitive()) {
			if (valueFilter.apply(jsonElement.getAsJsonPrimitive())) {
				return null;
			}
			return jsonElement;
		} else if (jsonElement.isJsonObject()) {
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			if (jsonObject.entrySet().isEmpty()) {
				return jsonObject;
			}
			return optimizeObject(jsonObject);
		} else { // jsonElement.isJsonArray()
			JsonArray jsonArray = jsonElement.getAsJsonArray();
			if (jsonArray.size() == 0) {
				return jsonArray;
			}
			return optimizeArray(jsonArray);
		}
	}

	@Override
	protected Set<Map.Entry<String, JsonElement>> objectEntrySet(
			final JsonObject jsonObject) {
		return jsonObject.entrySet();
	}

	@Override
	public void objectRemove(final JsonObject jsonObject, String key) {
		jsonObject.remove(key);
	}

	@Override
	public void arrayRemove(JsonArray jsonArray, JsonElement object) {
		jsonArray.remove(object);
	}

	public static class Builder extends CommonBuilder<GsonOptimizer> {
		Predicate<JsonElement> valueFilter = input -> {
			if (input == null || input.isJsonNull()) {
				return true;
			}

			if (!input.isJsonPrimitive()) {
				return false;
			}

			JsonPrimitive jsonPrimitive = input.getAsJsonPrimitive();

			if (jsonPrimitive.isString()) {
				return stringFilter.apply(jsonPrimitive.getAsString());
			} else if (jsonPrimitive.isBoolean()) {
				return booleanFilter.apply(jsonPrimitive.getAsBoolean());
			} else if (jsonPrimitive.isNumber()) {
				return numberFilter.apply(jsonPrimitive.getAsNumber());
			}
			return false;
		};

		@Override
		public GsonOptimizer build() {
			return new GsonOptimizer(skippingFields, removingFields,
					valueFilter, optimizeLevel);
		}
	}
}
