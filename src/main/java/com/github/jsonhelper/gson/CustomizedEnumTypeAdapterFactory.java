package com.github.jsonhelper.gson;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

public class CustomizedEnumTypeAdapterFactory extends AbstractEnumTypeAdapterFactory {
	@Override
	public <T> Predicate<T> interestedPredicate() {
		return new Predicate<T>() {
			public boolean apply(T t) {
				return false;
			}
		};
	}

	@Override
	public <T> Function<T, Object> valueFunction() {
		return new Function<T, Object>() {
			public Object apply(T t) {
				return null;
			}
		};
	}

	@Override
	public <T> Function<T, Class<?>> typeFunction() {
		return new Function<T, Class<?>>() {
			public Class<?> apply(T t) {
				return null;
			}
		};
	}
}
