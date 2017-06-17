package com.github.jsonhelper;

import com.github.jsonhelper.gson.CustomizedFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonHelper {
	public static String toJson(Object object, boolean optimize) {
        return getGson(optimize).toJson(object);
	}

	private static Gson getGson(boolean optimize) {
		if (optimize) {
			return CustomizedFactory.customizeGson();
		}
		return new Gson();
	}

	private static GsonBuilder getGsonBuilder(boolean optimize) {
		if (optimize) {
			return CustomizedFactory.customizeGsonBuilder();
		}
		return new GsonBuilder();
	}

}
