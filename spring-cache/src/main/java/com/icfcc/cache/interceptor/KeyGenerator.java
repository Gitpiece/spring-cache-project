package com.icfcc.cache.interceptor;


import java.lang.reflect.Method;

/**
 * Cache key generator. Used for creating a key based on the given method
 * (used as context) and its parameters.
 *
 * @author Costin Leau
 * @author Chris Beams
 * @since 3.1
 */
public interface KeyGenerator {

    Object generate(Object target, Method method, Object... params);

}