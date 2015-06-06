package com.icfcc.cache.support;

import com.icfcc.cache.Cache;
import com.icfcc.cache.CacheManager;
import org.springframework.beans.factory.InitializingBean;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Abstract base class implementing the common {@link CacheManager} methods.
 * Useful for 'static' environments where the backing caches do not change.
 *
 * @author Costin Leau
 * @author Juergen Hoeller
 * @since 3.1
 */
public abstract class AbstractCacheManager implements CacheManager, InitializingBean {

	private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>();

	private Set<String> cacheNames = new LinkedHashSet<String>();


	public void afterPropertiesSet() {
		Collection<? extends Cache> caches = loadCaches();

		// preserve the initial order of the cache names
		this.cacheMap.clear();
		this.cacheNames.clear();
		for (Cache cache : caches) {
			this.cacheMap.put(cache.getName(), cache);
			this.cacheNames.add(cache.getName());
		}
	}

	protected final void addCache(Cache cache) {
		this.cacheMap.put(cache.getName(), cache);
		this.cacheNames.add(cache.getName());
	}

	public Cache getCache(String name) {
		return this.cacheMap.get(name);
	}

	public Collection<String> getCacheNames() {
		return Collections.unmodifiableSet(this.cacheNames);
	}


	/**
	 * Load the caches for this cache manager. Occurs at startup.
	 * The returned collection must not be null.
	 */
	protected abstract Collection<? extends Cache> loadCaches();

}
