package com.github.jsonhelper.json;

import com.google.common.base.Predicate;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Map;
import java.util.Set;

/**
 * Created by shanfeng on 6/26/17.
 *
 * JSONArray and JSONObject's functional operations : Iterator or set just returns a view
 */
public class JSONOptimizer extends CommonOptimizer<JSONObject, JSONArray, Object> {
	private JSONOptimizer(final Set<String> skippedFields, final Set<String> removingFields,
						  final Predicate<Object> valueFilter, int optimizeLevel) {
		super(skippedFields, removingFields, valueFilter, optimizeLevel);
	}

	@Override
	protected boolean filterElement(Object jsonElement) {
		if (jsonElement == null) {
			return true;
		} else if (jsonElement instanceof JSONObject) {
			JSONObject object = (JSONObject)jsonElement;

			if (object.isNullObject() || object.isEmpty()) {
				return true;
			} else {
				optimizeObject((JSONObject)jsonElement);
			}
		} else if (jsonElement instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray)jsonElement;
			if (jsonArray.isEmpty()) {
				return true;
			} else {
				optimizeArray(jsonArray);
			}
		} else {
			if (valueFilter.apply(jsonElement)) {
				return true;
			}
		}

		return false;
	}


	@Override
	@SuppressWarnings("unchecked")
	protected Set<Map.Entry<String, Object>> objectEntrySet(final JSONObject jsonObject) {
		return jsonObject.entrySet();
	}

	@Override
	public void objectRemove(final JSONObject object, String key) {
		object.remove(key);
	}

	@Override
	public void arrayRemove(JSONArray jsonArray, Object object) {
		jsonArray.remove(object);
	}

	public static class Builder extends CommonBuilder<JSONOptimizer> {
		Predicate<Object> valueFilter = input -> {
				if (input == null) {
					return true;
				}

				if (input instanceof Boolean) {
					return booleanFilter.apply((Boolean)input);
				} else if (input instanceof String) {
					return stringFilter.apply((String)input);
				}
				else if (Number.class.isAssignableFrom(input.getClass())) {
					return numberFilter.apply((Number)input);
				}

				return false;
		};

		@Override
		public JSONOptimizer build() {
			return new JSONOptimizer(skippingFields, removingFields, valueFilter, optimizeLevel);
		}
	}
}
