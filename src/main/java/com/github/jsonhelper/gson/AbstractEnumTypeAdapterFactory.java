package com.github.jsonhelper.gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;


public abstract class AbstractEnumTypeAdapterFactory implements TypeAdapterFactory {
	public abstract <T> Predicate<T> interestedPredicate();
	public abstract <T> Function<T, Object> valueFunction();
	public abstract <T> Function<T, Class<?>> typeFunction();


	@SuppressWarnings("unchecked")
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
		Class<? super T> rawType = typeToken.getRawType();
		if (!Enum.class.isAssignableFrom(rawType) || rawType == Enum.class) {
			return null;
		}
		if (!rawType.isEnum()) {
			rawType = rawType.getSuperclass();
		}

		return (TypeAdapter<T>) new EnumTypeAdapter(rawType, interestedPredicate(),
				valueFunction(), typeFunction());
	}

	private static final class EnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T> {
		Predicate<T> predicate;
		Function<T, Object> function;
		Function<T, Class<?>> typeFunction;

		private final Map<String, T> nameToConstant = new HashMap<String, T>();
		private final Map<T, String> constantToName = new HashMap<T, String>();

		EnumTypeAdapter(Class<T> classOfT, Predicate<T> predicate, Function<T, Object> function, Function<T, Class<?>> typeFunction) {
			try {
				for (T constant : classOfT.getEnumConstants()) {
					String name = constant.name();
					SerializedName annotation = classOfT.getField(name).getAnnotation(SerializedName.class);
					if (annotation != null) {
						name = annotation.value();
					}
					nameToConstant.put(name, constant);
					constantToName.put(constant, name);
				}
			} catch (NoSuchFieldException e) {
				throw new AssertionError();
			}
			this.predicate = predicate;
			this.function = function;
			this.typeFunction = typeFunction;
		}
		public T read(JsonReader in) throws IOException {
			if (in.peek() == JsonToken.NULL) {
				in.nextNull();
				return null;
			}
			return nameToConstant.get(in.nextString());
		}

		public void write(JsonWriter out, T value) throws IOException {
			if (value != null) {
				if (predicate.apply(value)) {
					Class<?> type = typeFunction.apply(value);
					if (type.isAssignableFrom(Number.class)) {
						out.value((Number) function.apply(value));
					} else if (type.isAssignableFrom(int.class) || type.isAssignableFrom(Integer.class)) {
						out.value((Integer) function.apply(value));
					} else if (type.isAssignableFrom(long.class) || type.isAssignableFrom(Long.class)) {
						out.value((Long)function.apply(value));
					} else if (type.isAssignableFrom(String.class)) {
						out.value((String)function.apply(value));
					} else {
						out.nullValue();
					}
				} else {
					out.value(constantToName.get(value));
				}
			} else {
				out.value((String) null);
			}
		}
	}
}
