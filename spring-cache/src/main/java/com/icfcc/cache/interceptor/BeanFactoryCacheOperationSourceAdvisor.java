package com.icfcc.cache.interceptor;


import org.aopalliance.aop.Advice;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.util.Assert;

/**
 * Advisor driven by a {@link CacheOperationSource}, used to include a
 * cache advice bean for methods that are cacheable.
 *
 * @author Costin Leau
 * @since 3.1
 */
@SuppressWarnings("serial")
public class BeanFactoryCacheOperationSourceAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    private CacheOperationSource cacheOperationSource;

    private final CacheOperationSourcePointcut pointcut = new CacheOperationSourcePointcut() {
        @Override
        protected CacheOperationSource getCacheOperationSource() {
            return cacheOperationSource;
        }
    };

    private String adviceBeanName;

    private BeanFactory beanFactory;

    private transient Advice advice;

    /**
     * Set the cache operation attribute source which is used to find cache
     * attributes. This should usually be identical to the source reference
     * set on the cache interceptor itself.
     */
     //* @see CacheInterceptor#setCacheAttributeSource
    public void setCacheOperationSource(CacheOperationSource cacheOperationSource) {
        this.cacheOperationSource = cacheOperationSource;
    }
    /**
     * Set the {@link ClassFilter} to use for this pointcut.
     * Default is {@link ClassFilter#TRUE}.
     */
    public void setClassFilter(ClassFilter classFilter) {
        this.pointcut.setClassFilter(classFilter);
    }

    public Pointcut getPointcut() {
        return this.pointcut;
    }
    private transient volatile Object adviceMonitor = new Object();
    public void setAdvice(Advice advice) {
        synchronized (this.adviceMonitor) {
            this.advice = advice;
        }
    }

    public Advice getAdvice() {
        synchronized (this.adviceMonitor) {
            if (this.advice == null && this.adviceBeanName != null) {
                Assert.state(this.beanFactory != null, "BeanFactory must be set to resolve 'adviceBeanName'");
                this.advice = (Advice)this.beanFactory.getBean(this.adviceBeanName, Advice.class);
            }
            return this.advice;
        }
    }
}
