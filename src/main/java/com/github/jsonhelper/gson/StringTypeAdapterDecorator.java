package com.github.jsonhelper.gson;

import com.google.common.base.Predicate;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapters;

public class StringTypeAdapterDecorator extends TypeAdapterDecorator<String> {
	public static TypeAdapterFactory factory(Predicate<String> defaultPredicate) {
		return TypeAdapters.newFactory(String.class, new StringTypeAdapterDecorator(defaultPredicate));
	}

	private StringTypeAdapterDecorator(Predicate<String> defaultPredicate) {
		super(TypeAdapters.STRING, defaultPredicate);
	}
}
