package com.icfcc.cache;


/**
 * Interface that defines the common cache operations.
 *
 * <b>Note:</b> Due to the generic use of caching, it is recommended that
 * implementations allow storage of <tt>null</tt> values (for example to
 * cache methods that return {@code null}).
 *
 * @author Costin Leau
 * @since 3.1
 */
public interface Cache {

    /**
     * Return the cache name.
     */
    String getName();

    /**
     * Return the the underlying native cache provider.
     */
    Object getNativeCache();

    /**
     * Return the value to which this cache maps the specified key. Returns
     * <code>null</code> if the cache contains no mapping for this key.
     * @param key key whose associated value is to be returned.
     * @return the value to which this cache maps the specified key,
     * or <code>null</code> if the cache contains no mapping for this key
     */
    ValueWrapper get(Object key);

    /**
     * Associate the specified value with the specified key in this cache.
     * <p>If the cache previously contained a mapping for this key, the old
     * value is replaced by the specified value.
     * @param key the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     */
    void put(Object key, Object value);

    /**
     * Evict the mapping for this key from this cache if it is present.
     * @param key the key whose mapping is to be removed from the cache
     */
    void evict(Object key);

    /**
     * Remove all mappings from the cache.
     */
    void clear();


    /**
     * A (wrapper) object representing a cache value.
     */
    interface ValueWrapper {

        /**
         * Return the actual value in the cache.
         */
        Object get();
    }

}
