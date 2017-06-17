package com.github.jsonhelper.gson;

import com.google.common.base.Predicate;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CustomizedFactory {
	public static class CustomizedGsonBuilder {
		private Predicate<Boolean> booleanPredicate = new Predicate<Boolean>() {
			public boolean apply(Boolean aBoolean) {
				return false;
			}
		};

		private Predicate<Number> integerPredicate = new Predicate<Number>() {
			public boolean apply(Number number) {
				return false;
			}
		};

		private Predicate<Number> longPredicate = new Predicate<Number>() {
			public boolean apply(Number number) {
				return false;
			}
		};

		private Predicate<Number> floatPredicate = new Predicate<Number>() {
			public boolean apply(Number number) {
				return false;
			}
		};

		private Predicate<Number> doublePredicate = new Predicate<Number>() {
			public boolean apply(Number number) {
				return false;
			}
		};

		private Predicate<String> stringPredicate = new Predicate<String>() {
			public boolean apply(String s) {
				return false;
			}
		};

		private boolean customizeCollection = false;
		private boolean customizeMap = false;

		public CustomizedGsonBuilder booleanPredicate(Predicate<Boolean> booleanPredicate) {
			this.booleanPredicate = booleanPredicate;
			return this;
		}

		public CustomizedGsonBuilder integerPredicate(Predicate<Number> integerPredicate) {
			this.integerPredicate = integerPredicate;
			return this;
		}

		public CustomizedGsonBuilder longPredicate(Predicate<Number> longPredicate) {
			this.longPredicate = longPredicate;
			return this;
		}

		public CustomizedGsonBuilder floatPredicate(Predicate<Number> floatPredicate) {
			this.floatPredicate = floatPredicate;
			return this;
		}

		public CustomizedGsonBuilder doublePredicate(Predicate<Number> doublePredicate) {
			this.doublePredicate = doublePredicate;
			return this;
		}

		public CustomizedGsonBuilder stringPredicate(Predicate<String> stringPredicate) {
			this.stringPredicate = stringPredicate;
			return this;
		}

		public CustomizedGsonBuilder customizeCollection(boolean customizeCollection) {
			this.customizeCollection = customizeCollection;
			return this;
		}

		public CustomizedGsonBuilder customizeMap(boolean customizeMap) {
			this.customizeMap = customizeMap;
			return this;
		}

		public GsonBuilder toGsonBuilder() {
			GsonBuilder builder =  new GsonBuilder()
					.registerTypeAdapterFactory(BooleanTypeAdapterDecorator.factory(booleanPredicate))
					.registerTypeAdapterFactory(IntegerTypeAdapterDecorator.factory(integerPredicate))
					.registerTypeAdapterFactory(LongTypeAdapterDecorator.factory(longPredicate))
					.registerTypeAdapterFactory(FloatTypeAdapterDecorator.factory(floatPredicate))
					.registerTypeAdapterFactory(DoubleTypeAdapterDecorator.factory(doublePredicate))
					.registerTypeAdapterFactory(StringTypeAdapterDecorator.factory(stringPredicate));

			if (customizeCollection) {
				builder.registerTypeAdapterFactory(CollectionTypeAdapterFactoryDecorator.factory());
			}
			if (customizeMap) {
				builder.registerTypeAdapterFactory(MapTypeAdapterFactoryDecorator.factory());
			};

			return builder;
		}

		public Gson toGson() {
			return toGsonBuilder().create();
		}
	}

	public static GsonBuilder customizeGsonBuilder() {
		return new CustomizedGsonBuilder()
				.booleanPredicate(new Predicate<Boolean>() {
					public boolean apply(Boolean aBoolean) {
						return !aBoolean;
					}
				})
				.integerPredicate(new Predicate<Number>() {
					public boolean apply(Number number) {
						return number.intValue() == 0;
					}
				})
				.longPredicate(new Predicate<Number>() {
					public boolean apply(Number number) {
						return number.longValue() == 0L;
					}
				})
				.floatPredicate(new Predicate<Number>() {
					public boolean apply(Number number) {
						return Floats.compare(number.floatValue(), 0.0f) == 0;
					}
				})
				.doublePredicate(new Predicate<Number>() {
					public boolean apply(Number number) {
						return Doubles.compare(number.doubleValue(), 0.0d) == 0;
					}
				})
				.stringPredicate(new Predicate<String>() {
					public boolean apply(String s) {
						return s.equals("");
					}
				})
				.customizeCollection(true)
				.customizeMap(true)
				.toGsonBuilder();
	}

	public static Gson customizeGson() {
		return customizeGsonBuilder().create();
	}

	public static GsonBuilder customizeGsonBuilderWithEnum() {
		return new CustomizedGsonBuilder().toGsonBuilder().registerTypeAdapterFactory(
				new CustomizedEnumTypeAdapterFactory()
		);
	}

	public static Gson customizeGsonWithEnum() {
		return customizeGsonBuilderWithEnum().create();
	}

	public static GsonBuilder customizeCompatibleGsonBuilder() {
		return new CustomizedGsonBuilder().toGsonBuilder().registerTypeAdapterFactory(
				new CustomizedEnumTypeAdapterFactory()
		);
	}

	public static Gson customizeCompatibleGson() {
		return customizeCompatibleGsonBuilder().create();
	}
}
