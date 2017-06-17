package com.github.jsonhelper.gson;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;

import com.google.gson.InstanceCreator;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;

public class CollectionTypeAdapterFactoryDecorator extends TypeAdapterFactoryDecorator {
	private static final ConstructorConstructor constructorConstructor = new ConstructorConstructor(
			Collections.<Type, InstanceCreator<?>>emptyMap()
	);

	private static final CollectionTypeAdapterFactory factories = new CollectionTypeAdapterFactory(
			constructorConstructor
	);

	public static TypeAdapterFactory factory() {
		return new CollectionTypeAdapterFactoryDecorator();
	}

	private CollectionTypeAdapterFactoryDecorator() {
		super(factories);
	}

	@Override
	public <T> boolean isDefaultValue(T value) {
		return (value instanceof Collection) && (((Collection)value).isEmpty());
	}
}
