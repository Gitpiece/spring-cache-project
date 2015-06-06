package com.icfcc.cache.concurrent;

import com.icfcc.cache.Cache;
import com.icfcc.cache.support.SimpleValueWrapper;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
public class ConcurrentMapCache implements Cache {

	private static final Object NULL_HOLDER = new NullHolder();

	private final String name;

	private final ConcurrentMap<Object, Object> store;

	private final boolean allowNullValues;


	/**
	 * Create a new ConcurrentMapCache with the specified name.
	 * @param name the name of the cache
	 */
	public ConcurrentMapCache(String name) {
		this(name, new ConcurrentHashMap<Object, Object>(), true);
	}

	/**
	 * Create a new ConcurrentMapCache with the specified name.
	 * @param name the name of the cache
	 * @param allowNullValues whether to accept and convert null values for this cache
	 */
	public ConcurrentMapCache(String name, boolean allowNullValues) {
		this(name, new ConcurrentHashMap<Object, Object>(), allowNullValues);
	}

	/**
	 * Create a new ConcurrentMapCache with the specified name and the
	 * given internal ConcurrentMap to use.
	 * @param name the name of the cache
	 * @param store the ConcurrentMap to use as an internal store
	 * @param allowNullValues whether to allow <code>null</code> values
	 * (adapting them to an internal null holder value)
	 */
	public ConcurrentMapCache(String name, ConcurrentMap<Object, Object> store, boolean allowNullValues) {
		this.name = name;
		this.store = store;
		this.allowNullValues = allowNullValues;
	}


	public String getName() {
		return this.name;
	}

	public ConcurrentMap getNativeCache() {
		return this.store;
	}

	public boolean isAllowNullValues() {
		return this.allowNullValues;
	}

	public ValueWrapper get(Object key) {
		Object value = this.store.get(key);
		return (value != null ? new SimpleValueWrapper(fromStoreValue(value)) : null);
	}

	public void put(Object key, Object value) {
		this.store.put(key, toStoreValue(value));
	}

	public void evict(Object key) {
		this.store.remove(key);
	}

	public void clear() {
		this.store.clear();
	}


	/**
	 * Convert the given value from the internal store to a user value
	 * returned from the get method (adapting <code>null</code>).
	 * @param storeValue the store value
	 * @return the value to return to the user
	 */
	protected Object fromStoreValue(Object storeValue) {
		if (this.allowNullValues && storeValue == NULL_HOLDER) {
			return null;
		}
		return storeValue;
	}

	/**
	 * Convert the given user value, as passed into the put method,
	 * to a value in the internal store (adapting <code>null</code>).
	 * @param userValue the given user value
	 * @return the value to store
	 */
	protected Object toStoreValue(Object userValue) {
		if (this.allowNullValues && userValue == null) {
			return NULL_HOLDER;
		}
		return userValue;
	}


	private static class NullHolder implements Serializable {

	}

}
