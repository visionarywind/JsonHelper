package com.github.jsonhelper;

import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JSONHelper  {
	public static Predicate<Object> optimizePreidict = new Predicate<Object>() {
		public boolean apply(Object input) {
			if (input instanceof Integer) {
				return (Integer)input == 0;
			}
			if (input instanceof Long) {
				return (Long)input == 0;
			}
			if (input instanceof Boolean) {
				return !(Boolean)input;
			}
			if (input instanceof String) {
				return ((String)input).equals("");
			}
			if (input instanceof Float) {
				return Floats.compare((Float)input, 0.0f) == 0;
			}
			if (input instanceof Double) {
				return Doubles.compare((Double)input, 0.0d) == 0;
			}

			return false;
		}
	};

	public static JSONObject optimizeObject(JSONObject jsonObject, boolean optimize) {
        return optimizeObjectWithPredict(jsonObject, optimize, optimizePreidict);
	}

	public static JSONObject optimizeObjectWithPredict(JSONObject jsonObject, boolean optimize, Predicate<Object> predicate) {
		if (optimize) {
			return optimizeJSONObject(jsonObject, predicate);
		}

		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	private static JSONObject optimizeJSONObject(final JSONObject jsonObject, final Predicate<Object> predicate) {
		Set set = FluentIterable.from(jsonObject.keySet()).filter(new Predicate() {
			public boolean apply(Object input) {
				return predicate.apply(jsonObject.get(input));
			}
		}).toSet();

		FluentIterable.from(set).allMatch(new Predicate() {
			public boolean apply(Object input) {
				jsonObject.remove(input);
				return true;
			}
		});

		return jsonObject;
	}

	public static JSONArray optimizeArray(JSONArray jsonArray, boolean optimize) {
        return optimizeArrayWithPredict(jsonArray, optimize, optimizePreidict);
	}

	public static JSONArray optimizeArrayWithPredict(JSONArray jsonArray, boolean
            optimize, Predicate<Object> predicate) {
		if (optimize) {
			return optimizeJSONArray(jsonArray, predicate);
		}

		return jsonArray;
	}

	@SuppressWarnings("unchecked")
	private static JSONArray optimizeJSONArray(JSONArray jsonArray, final Predicate<Object> predicate) {
		FluentIterable.from(jsonArray).allMatch(new Predicate() {
			public boolean apply(Object obj) {
				if (obj instanceof JSONObject) {
					optimizeJSONObject((JSONObject)obj, predicate);
				} else if (obj instanceof JSONArray) {
					optimizeJSONArray((JSONArray)obj, predicate);
				}
				return true;
			}
		});
		return jsonArray;
	}
}
