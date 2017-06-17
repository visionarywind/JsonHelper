package com.github.jsonhelper.gson;

import com.google.common.base.Predicate;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapters;

public class DoubleTypeAdapterDecorator extends TypeAdapterDecorator<Number> {
	public static TypeAdapterFactory factory(Predicate<Number> defaultPredicate) {
		return TypeAdapters.newFactory(double.class, Double.class, new DoubleTypeAdapterDecorator(defaultPredicate));
	}

	private DoubleTypeAdapterDecorator(Predicate<Number> defaultPredicate) {
		super(TypeAdapters.DOUBLE, defaultPredicate);
	}
}
