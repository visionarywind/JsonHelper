package com.github.jsonhelper.gson;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

import com.google.gson.InstanceCreator;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.bind.MapTypeAdapterFactory;

public class MapTypeAdapterFactoryDecorator extends TypeAdapterFactoryDecorator {
	private static final ConstructorConstructor constructorConstructor = new ConstructorConstructor(
			Collections.<Type, InstanceCreator<?>>emptyMap()
	);

	private static final MapTypeAdapterFactory factories = new MapTypeAdapterFactory(
			constructorConstructor, false
	);

	public static TypeAdapterFactory factory() {
		return new MapTypeAdapterFactoryDecorator();
	}

	private MapTypeAdapterFactoryDecorator() {
		super(factories);
	}

	@Override
	public <T> boolean isDefaultValue(T value) {
		return (value instanceof Map) && (((Map)value).isEmpty());
	}
}
