package com.icfcc.cache.util;

import org.springframework.aop.TargetClassAware;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.target.SingletonTargetSource;
import org.springframework.util.Assert;

/**
 * Created by wanghuanyu on 2015/6/4.
 */
public class CacheAopProxyUtils extends AopProxyUtils {
    /**
     * Determine the ultimate target class of the given bean instance, traversing
     * not only a top-level proxy but any number of nested proxies as well -
     * as long as possible without side effects, that is, just for singleton targets.
     * @param candidate the instance to check (might be an AOP proxy)
     * @return the target class (or the plain class of the given object as fallback;
     * never <code>null</code>)
     * @see org.springframework.aop.TargetClassAware#getTargetClass()
     * @see org.springframework.aop.framework.Advised#getTargetSource()
     */
    public static Class<?> ultimateTargetClass(Object candidate) {
        Assert.notNull(candidate, "Candidate object must not be null");
        Object current = candidate;
        Class<?> result = null;
        while (current instanceof TargetClassAware) {
            result = ((TargetClassAware) current).getTargetClass();
            Object nested = null;
            if (current instanceof Advised) {
                TargetSource targetSource = ((Advised) current).getTargetSource();
                if (targetSource instanceof SingletonTargetSource) {
                    nested = ((SingletonTargetSource) targetSource).getTarget();
                }
            }
            current = nested;
        }
        if (result == null) {
            result = (AopUtils.isCglibProxy(candidate) ? candidate.getClass().getSuperclass() : candidate.getClass());
        }
        return result;
    }
}
