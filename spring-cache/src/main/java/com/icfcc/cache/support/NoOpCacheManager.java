package com.icfcc.cache.support;

import com.icfcc.cache.Cache;
import com.icfcc.cache.CacheManager;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * A basic, no operation {@link CacheManager} implementation suitable
 * for disabling caching, typically used for backing cache declarations
 * without an actual backing store.
 *
 * <p>Will simply accept any items into the cache not actually storing them.
 *
 * @author Costin Leau
 * @since 3.1
 * @see CompositeCacheManager
 */
public class NoOpCacheManager implements CacheManager {

	private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();

	private final Set<String> cacheNames = new LinkedHashSet<String>();


	/**
	 * This implementation always returns a {@link Cache} implementation that will not store items.
	 * Additionally, the request cache will be remembered by the manager for consistency.
	 */
	public Cache getCache(String name) {
		Cache cache = this.caches.get(name);
		if (cache == null) {
			this.caches.putIfAbsent(name, new NoOpCache(name));
			synchronized (this.cacheNames) {
				this.cacheNames.add(name);
			}
		}

		return this.caches.get(name);
	}

	/**
	 * This implementation returns the name of the caches previously requested.
	 */
	public Collection<String> getCacheNames() {
		synchronized (this.cacheNames) {
			return Collections.unmodifiableSet(this.cacheNames);
		}
	}


	private static class NoOpCache implements Cache {

		private final String name;

		public NoOpCache(String name) {
			this.name = name;
		}

		public void clear() {
		}

		public void evict(Object key) {
		}

		public ValueWrapper get(Object key) {
			return null;
		}

		public String getName() {
			return this.name;
		}

		public Object getNativeCache() {
			return null;
		}

		public void put(Object key, Object value) {
		}
	}

}