package com.github.jsonhelper.json;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;

import java.util.Set;

/**
 * Created by shanfeng on 6/27/17.
 *
 * Bad implements for chained calling
 */
public abstract class CommonBuilder<V> {
	public static Predicate<String> STRING_FILTER = StringUtils::isEmpty;
	public static Predicate<Double> DOUBLE_FILTER = input -> Double.compare(
			input, 0.0d) == 0;
	public static Predicate<Boolean> BOOLEAN_FILTER = input -> !input;

	Set<String> skippingFields = Sets.newHashSet();
	Set<String> removingFields = Sets.newHashSet();

	Predicate<String> stringFilter = STRING_FILTER;
	Predicate<Double> doubleFilter = DOUBLE_FILTER;
	Predicate<Number> numberFilter = input -> doubleFilter.apply(input
			.doubleValue());
	Predicate<Boolean> booleanFilter = BOOLEAN_FILTER;

	int optimizeLevel = CommonOptimizer.OPTIMIZE_LEVEL_OBJECTS;

	public CommonBuilder skipFields(Set<String> skippingFields) {
		this.skippingFields = skippingFields;
		return this;
	}

	public CommonBuilder skipField(String field) {
		skippingFields.add(field);
		return this;
	}

	public CommonBuilder removeFields(Set<String> removingFields) {
		this.removingFields = removingFields;
		return this;
	}

	public CommonBuilder removeField(String field) {
		removingFields.add(field);
		return this;
	}

	public CommonBuilder stringFilter(Predicate<String> stringFilter) {
		this.stringFilter = stringFilter;
		return this;
	}

	public CommonBuilder doubleFilter(Predicate<Double> doubleFilter) {
		this.doubleFilter = doubleFilter;
		return this;
	}

	public CommonBuilder numberFilter(Predicate<Number> numberFilter) {
		this.numberFilter = numberFilter;
		return this;
	}

	public CommonBuilder booleanFilter(Predicate<Boolean> booleanFilter) {
		this.booleanFilter = booleanFilter;
		return this;
	}

	public CommonBuilder optimizeLevel(int optimizeLevel) {
		Preconditions
				.checkArgument(optimizeLevel >= CommonOptimizer.OPTIMIZE_LEVEL_OBJECTS);
		Preconditions
				.checkArgument(optimizeLevel <= CommonOptimizer.OPTIMIZE_LEVEL_OBJECTS_AND_ARRAYS);
		this.optimizeLevel = optimizeLevel;
		return this;
	}

	public abstract V build();
}
