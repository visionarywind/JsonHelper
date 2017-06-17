package com.github.jsonhelper.gson;

import java.io.IOException;

import com.google.common.base.Predicate;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class TypeAdapterDecorator<T> extends TypeAdapter<T> {
	private TypeAdapter<T> typeAdapter;
	private Predicate<T> defaultPredicate;

	TypeAdapterDecorator(TypeAdapter<T> typeAdapter, Predicate<T> defaultPredicate) {
		this.typeAdapter = typeAdapter;
		this.defaultPredicate = defaultPredicate;
	}

	@Override
	public void write(JsonWriter out, T value) throws IOException {
		if (value == null || defaultPredicate.apply(value)) {
			out.nullValue();
		} else {
			typeAdapter.write(out, value);
		}
	}

	@Override
	public T read(JsonReader in) throws IOException {
		return typeAdapter.read(in);
	}
}
