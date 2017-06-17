package com.github.jsonhelper.gson;

import com.google.common.base.Predicate;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

public abstract class TypeAdapterFactoryDecorator implements TypeAdapterFactory {
	private TypeAdapterFactory factory;

	TypeAdapterFactoryDecorator(TypeAdapterFactory factory) {
		this.factory = factory;
	}

	public abstract <T> boolean isDefaultValue(T value);

	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
		TypeAdapter<T> typeAdapter = factory.create(gson, type);
		if (typeAdapter != null) {
			return new TypeAdapterDecorator<T>(typeAdapter, new Predicate<T>() {
				public boolean apply(T t) {
					return isDefaultValue(t);
				}
			});
		}
		return null;
	}
}
