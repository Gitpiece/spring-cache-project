package com.icfcc.cache.interceptor;

import java.lang.reflect.Method;

/**
 * Default key generator. Returns {@value #NO_PARAM_KEY} if no
 * parameters are provided, the parameter itself if only one is given or
 * a hash code computed from all given parameters' hash code values.
 * Uses the constant value {@value #NULL_PARAM_KEY} for any
 * {@code null} parameters given.
 *
 * @author Costin Leau
 * @author Chris Beams
 * @since 3.1
 */
public class DefaultKeyGenerator implements KeyGenerator {

    public static final int NO_PARAM_KEY = 0;
    public static final int NULL_PARAM_KEY = 53;

    public Object generate(Object target, Method method, Object... params) {
        if (params.length == 1) {
            return (params[0] == null ? NULL_PARAM_KEY : params[0]);
        }
        if (params.length == 0) {
            return NO_PARAM_KEY;
        }
        int hashCode = 17;
        for (Object object : params) {
            hashCode = 31 * hashCode + (object == null ? NULL_PARAM_KEY : object.hashCode());
        }
        return Integer.valueOf(hashCode);
    }

}
