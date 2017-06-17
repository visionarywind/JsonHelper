package com.github.jsonhelper.gson;

import com.google.common.base.Predicate;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapters;

public class LongTypeAdapterDecorator extends TypeAdapterDecorator<Number> {
	public static TypeAdapterFactory factory(Predicate<Number> defaultPredicate) {
		return  TypeAdapters.newFactory(long.class, Long.class, new LongTypeAdapterDecorator(defaultPredicate));
	}

	private LongTypeAdapterDecorator(Predicate<Number> defaultPredicate) {
		super(TypeAdapters.LONG, defaultPredicate);
	}
}
