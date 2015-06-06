package com.icfcc.cache.interceptor;


import org.springframework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Composite {@link CacheOperationSource} implementation that iterates
 * over a given array of {@code CacheOperationSource} instances.
 *
 * @author Costin Leau
 * @since 3.1
 */
@SuppressWarnings("serial")
public class CompositeCacheOperationSource implements CacheOperationSource, Serializable {

    private final CacheOperationSource[] cacheOperationSources;

    /**
     * Create a new CompositeCacheOperationSource for the given sources.
     * @param cacheOperationSources the CacheOperationSource instances to combine
     */
    public CompositeCacheOperationSource(CacheOperationSource... cacheOperationSources) {
        Assert.notEmpty(cacheOperationSources, "cacheOperationSources array must not be empty");
        this.cacheOperationSources = cacheOperationSources;
    }

    /**
     * Return the {@code CacheOperationSource} instances that this
     * {@code CompositeCacheOperationSource} combines.
     */
    public final CacheOperationSource[] getCacheOperationSources() {
        return this.cacheOperationSources;
    }

    public Collection<CacheOperation> getCacheOperations(Method method, Class<?> targetClass) {
        Collection<CacheOperation> ops = null;

        for (CacheOperationSource source : this.cacheOperationSources) {
            Collection<CacheOperation> cacheOperations = source.getCacheOperations(method, targetClass);
            if (cacheOperations != null) {
                if (ops == null) {
                    ops = new ArrayList<CacheOperation>();
                }

                ops.addAll(cacheOperations);
            }
        }
        return ops;
    }
}
