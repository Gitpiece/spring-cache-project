package com.icfcc.cache.support;

import com.icfcc.cache.Cache.ValueWrapper;

/**
 * Straightforward implementation of {@link com.icfcc.cache.Cache.ValueWrapper},
 * simply holding the value as given at construction and returning it from {@link #get()}.
 *
 * @author Costin Leau
 * @since 3.1
 */
public class SimpleValueWrapper implements ValueWrapper {

	private final Object value;


	/**
	 * Create a new SimpleValueWrapper instance for exposing the given value.
	 * @param value the value to expose (may be <code>null</code>)
	 */
	public SimpleValueWrapper(Object value) {
		this.value = value;
	}


	/**
	 * Simply returns the value as given at construction time.
	 */
	public Object get() {
		return this.value;
	}

}
