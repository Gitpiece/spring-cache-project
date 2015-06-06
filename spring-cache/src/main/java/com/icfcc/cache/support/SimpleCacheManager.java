package com.icfcc.cache.support;


import com.icfcc.cache.Cache;

import java.util.Collection;

/**
 * Simple cache manager working against a given collection of caches.
 * Useful for testing or simple caching declarations.
 *
 * @author Costin Leau
 * @since 3.1
 */
public class SimpleCacheManager extends AbstractCacheManager {

    private Collection<? extends Cache> caches;

    /**
     * Specify the collection of Cache instances to use for this CacheManager.
     */
    public void setCaches(Collection<? extends Cache> caches) {
        this.caches = caches;
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return this.caches;
    }

}