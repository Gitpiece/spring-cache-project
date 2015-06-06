package com.icfcc.cache.interceptor;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * AOP Alliance MethodInterceptor for declarative cache
 * management using the common Spring caching infrastructure
 * ({@link com.icfcc.cache.Cache}).
 *
 * <p>Derives from the {@link CacheAspectSupport} class which
 * contains the integration with Spring's underlying caching API.
 * CacheInterceptor simply calls the relevant superclass methods
 * in the correct order.
 *
 * <p>CacheInterceptors are thread-safe.
 *
 * @author Costin Leau
 * @author Juergen Hoeller
 * @since 3.1
 */
@SuppressWarnings("serial")
public class CacheInterceptor extends CacheAspectSupport implements MethodInterceptor, Serializable {

    private static class ThrowableWrapper extends RuntimeException {
        private final Throwable original;

        ThrowableWrapper(Throwable original) {
            this.original = original;
        }
    }

    public Object invoke(final MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();

        Invoker aopAllianceInvoker = new Invoker() {
            public Object invoke() {
                try {
                    return invocation.proceed();
                } catch (Throwable ex) {
                    throw new ThrowableWrapper(ex);
                }
            }
        };

        try {
            return execute(aopAllianceInvoker, invocation.getThis(), method, invocation.getArguments());
        } catch (ThrowableWrapper th) {
            throw th.original;
        }
    }
}
