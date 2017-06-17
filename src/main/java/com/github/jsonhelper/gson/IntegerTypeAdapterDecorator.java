package com.github.jsonhelper.gson;

import com.google.common.base.Predicate;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapters;

public class IntegerTypeAdapterDecorator extends TypeAdapterDecorator<Number> {
	public static TypeAdapterFactory factory(Predicate<Number> defaultPredicate) {
		return TypeAdapters.newFactory(int.class, Integer.class, new IntegerTypeAdapterDecorator(defaultPredicate));
	}

	private IntegerTypeAdapterDecorator(Predicate<Number> defaultPredicate) {
		super(TypeAdapters.INTEGER, defaultPredicate);
	}
}
