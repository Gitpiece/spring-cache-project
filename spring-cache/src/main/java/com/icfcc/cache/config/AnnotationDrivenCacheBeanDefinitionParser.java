package com.icfcc.cache.config;


import com.icfcc.cache.annotation.AnnotationCacheOperationSource;
import com.icfcc.cache.interceptor.BeanFactoryCacheOperationSourceAdvisor;
import com.icfcc.cache.interceptor.CacheInterceptor;
import org.springframework.aop.config.AopNamespaceUtils;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import static com.icfcc.cache.annotation.AnnotationConfigUtils.*;

/**
 * {@link org.springframework.beans.factory.xml.BeanDefinitionParser}
 * implementation that allows users to easily configure all the
 * infrastructure beans required to enable annotation-driven cache
 * demarcation.
 * <p/>
 * <p>By default, all proxies are created as JDK proxies. This may cause
 * some problems if you are injecting objects as concrete classes rather
 * than interfaces. To overcome this restriction you can set the
 * '{@code proxy-target-class}' attribute to '{@code true}', which will
 * result in class-based proxies being created.
 *
 * @author Costin Leau
 * @since 3.1
 */
class AnnotationDrivenCacheBeanDefinitionParser implements BeanDefinitionParser {

    /**
     * Parses the '{@code <cache:annotation-driven>}' tag. Will
     * {@link AopNamespaceUtils#registerAutoProxyCreatorIfNecessary
     * register an AutoProxyCreator} with the container as necessary.
     */
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String mode = element.getAttribute("mode");
        if ("aspectj".equals(mode)) {
            // mode="aspectj"
            registerCacheAspect(element, parserContext);
        } else {
            // mode="proxy"
            AopAutoProxyConfigurer.configureAutoProxyCreator(element, parserContext);
        }

        return null;
    }

    private static void parseCacheManagerProperty(Element element, BeanDefinition def) {
        def.getPropertyValues().addPropertyValue(new PropertyValue("cacheManager",
                new RuntimeBeanReference(CacheNamespaceHandler.extractCacheManager(element))));
    }

    /**
     * Registers a
     * <pre>
     * <bean id="cacheAspect" class="org.springframework.cache.aspectj.AnnotationCacheAspect" factory-method="aspectOf">
     *   <property name="cacheManagerBeanName" value="cacheManager"/>
     * </bean>
     *
     * </pre>
     *
     * @param element
     * @param parserContext
     */
    private void registerCacheAspect(Element element, ParserContext parserContext) {
        if (!parserContext.getRegistry().containsBeanDefinition(CACHE_ASPECT_BEAN_NAME)) {
            RootBeanDefinition def = new RootBeanDefinition();
            def.setBeanClassName(CACHE_ASPECT_CLASS_NAME);
            def.setFactoryMethodName("aspectOf");
            parseCacheManagerProperty(element, def);

            BeanComponentDefinition beanComponentDefinition = new BeanComponentDefinition(def, CACHE_ASPECT_BEAN_NAME);
            BeanDefinitionReaderUtils.registerBeanDefinition(beanComponentDefinition, parserContext.getRegistry());
            parserContext.registerComponent(beanComponentDefinition);
            //parserContext.registerBeanComponent(new BeanComponentDefinition(def, CACHE_ASPECT_BEAN_NAME));

            beanComponentDefinition = new BeanComponentDefinition(def, CACHE_ASPECT_BEAN_NAME);
            BeanDefinitionReaderUtils.registerBeanDefinition(beanComponentDefinition, parserContext.getRegistry());
            parserContext.registerComponent(beanComponentDefinition);
        }
    }


    /**
     * Inner class to just introduce an AOP framework dependency when actually in proxy mode.
     */
    private static class AopAutoProxyConfigurer {

        public static void configureAutoProxyCreator(Element element, ParserContext parserContext) {
            AopNamespaceUtils.registerAutoProxyCreatorIfNecessary(parserContext, element);

            if (!parserContext.getRegistry().containsBeanDefinition(CACHE_ADVISOR_BEAN_NAME)) {
                Object eleSource = parserContext.extractSource(element);

                // Create the CacheOperationSource definition.
                RootBeanDefinition sourceDef = new RootBeanDefinition(AnnotationCacheOperationSource.class);
                sourceDef.setSource(eleSource);
                sourceDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
                String sourceName = parserContext.getReaderContext().registerWithGeneratedName(sourceDef);

                // Create the CacheInterceptor definition.
                RootBeanDefinition interceptorDef = new RootBeanDefinition(CacheInterceptor.class);
                interceptorDef.setSource(eleSource);
                interceptorDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
                parseCacheManagerProperty(element, interceptorDef);
                CacheNamespaceHandler.parseKeyGenerator(element, interceptorDef);
                interceptorDef.getPropertyValues().addPropertyValue(new PropertyValue("cacheOperationSources", new RuntimeBeanReference(sourceName)));
                String interceptorName = parserContext.getReaderContext().registerWithGeneratedName(interceptorDef);

                // Create the CacheAdvisor definition.
                RootBeanDefinition advisorDef = new RootBeanDefinition(BeanFactoryCacheOperationSourceAdvisor.class);
                advisorDef.setSource(eleSource);
                advisorDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
                advisorDef.getPropertyValues().addPropertyValue(new PropertyValue("cacheOperationSource", new RuntimeBeanReference(sourceName)));
                advisorDef.getPropertyValues().addPropertyValue("adviceBeanName", interceptorName);
                if (element.hasAttribute("order")) {
                    advisorDef.getPropertyValues().addPropertyValue(new PropertyValue("order", element.getAttribute("order")));
                }
                parserContext.getRegistry().registerBeanDefinition(CACHE_ADVISOR_BEAN_NAME, advisorDef);

                CompositeComponentDefinition compositeDef = new CompositeComponentDefinition(element.getTagName(),
                        eleSource);
                compositeDef.addNestedComponent(new BeanComponentDefinition(sourceDef, sourceName));
                compositeDef.addNestedComponent(new BeanComponentDefinition(interceptorDef, interceptorName));
                compositeDef.addNestedComponent(new BeanComponentDefinition(advisorDef, CACHE_ADVISOR_BEAN_NAME));
                parserContext.registerComponent(compositeDef);
            }
        }
    }
}
