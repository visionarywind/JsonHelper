package com.github.jsonhelper.json;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by mi on 6/27/17.
 */
public abstract class CommonOptimizer<O, A extends Iterable, E> {
	Set<String> skippedFields;
	Set<String> removingFields;
	Predicate<E> valueFilter;
	int optimizeLevel; // 0, optimize objects; 1, optimize objects and arrays

	public static int OPTIMIZE_LEVEL_OBJECTS = 0;
	public static int OPTIMIZE_LEVEL_OBJECTS_AND_ARRAYS = 1;

	public CommonOptimizer(Set<String> skippedFields,
			Set<String> removingFields, Predicate<E> valueFilter,
			int optimizeLevel) {
		this.skippedFields = skippedFields;
		this.removingFields = removingFields;
		this.valueFilter = valueFilter;
		this.optimizeLevel = optimizeLevel;
	}

	protected abstract boolean filterElement(E input);

	Predicate<Map.Entry<String, E>> objectFilter = new Predicate<Map.Entry<String, E>>() {
		@Override
		public boolean apply(Map.Entry<String, E> input) {
			String key = input.getKey();
			if (removingFields.contains(key)) {
				return true;
			} else if (skippedFields.contains(key)) {
				return false;
			}

			E val = input.getValue();
			return filterElement(val);
		}
	};

	protected abstract Set<Map.Entry<String, E>> objectEntrySet(
			final O jsonObject);

	public abstract void objectRemove(final O jsonObject, String key);

	public O optimizeObject(final O jsonObject) {
		if (jsonObject == null) {
			return null;
		}

		List<String> removingKeys = objectEntrySet(jsonObject).stream()
				.filter(e -> objectFilter.apply(e)).map(Map.Entry::getKey)
				.collect(Collectors.toList());
		removingKeys.forEach(e -> objectRemove(jsonObject, e));

		return jsonObject;
	}

	Predicate<E> arrayFilter = input -> filterElement(input);

	public abstract void arrayRemove(A jsonArray, E object);

	@SuppressWarnings("unchecked")
	public A optimizeArray(final A jsonArray) {
		if (jsonArray == null) {
			return null;
		}

		List<E> removingList = FluentIterable.from(jsonArray)
				.filter(arrayFilter).toList();
		if (optimizeLevel == OPTIMIZE_LEVEL_OBJECTS_AND_ARRAYS) {
			removingList.stream().forEach(e -> arrayRemove(jsonArray, e));
		}

		return jsonArray;
	}
}
