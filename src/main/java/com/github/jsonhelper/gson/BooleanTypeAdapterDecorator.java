package com.github.jsonhelper.gson;

import com.google.common.base.Predicate;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapters;

public class BooleanTypeAdapterDecorator extends TypeAdapterDecorator<Boolean> {
	public static TypeAdapterFactory factory(Predicate<Boolean> defaultPredicate) {
		return TypeAdapters.newFactory(boolean.class, Boolean.class, new BooleanTypeAdapterDecorator(defaultPredicate));
	}

	private BooleanTypeAdapterDecorator(Predicate<Boolean> defaultPredicate) {
		super(TypeAdapters.BOOLEAN, defaultPredicate);
	}
}
