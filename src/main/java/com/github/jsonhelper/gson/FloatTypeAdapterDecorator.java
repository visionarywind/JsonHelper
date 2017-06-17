package com.github.jsonhelper.gson;

import com.google.common.base.Predicate;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapters;

public class FloatTypeAdapterDecorator extends TypeAdapterDecorator<Number> {
	public static TypeAdapterFactory factory(Predicate<Number> defaultPredicate) {
		return TypeAdapters.newFactory(float.class, Float.class, new FloatTypeAdapterDecorator(defaultPredicate));
	}

	private FloatTypeAdapterDecorator(Predicate<Number> defaultPredicate) {
		super(TypeAdapters.FLOAT, defaultPredicate);
	}
}
