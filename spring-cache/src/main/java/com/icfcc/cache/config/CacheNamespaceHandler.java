package com.icfcc.cache.config;

import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
/**
 * {@code NamespaceHandler} allowing for the configuration of declarative
 * cache management using either XML or using annotations.
 *
 * <p>This namespace handler is the central piece of functionality in the
 * Spring cache management facilities.
 *
 * @author Costin Leau
 * @since 3.1
 */
public class CacheNamespaceHandler extends NamespaceHandlerSupport {
    static final String CACHE_MANAGER_ATTRIBUTE = "cache-manager";
    static final String DEFAULT_CACHE_MANAGER_BEAN_NAME = "cacheManager";
    static final String KEY_GENERATOR_ATTRIBUTE = "key-generator";
    static final String KEY_GENERATOR_PROPERTY_NAME = "keyGenerator";
    
    static String extractCacheManager(Element element) {
        return (element.hasAttribute(CacheNamespaceHandler.CACHE_MANAGER_ATTRIBUTE) ? element
                .getAttribute(CacheNamespaceHandler.CACHE_MANAGER_ATTRIBUTE)
                : CacheNamespaceHandler.DEFAULT_CACHE_MANAGER_BEAN_NAME);
    }

    static BeanDefinition parseKeyGenerator(Element element, BeanDefinition def) {
        String name = element.getAttribute(KEY_GENERATOR_ATTRIBUTE);
        if (StringUtils.hasText(name)) {
            def.getPropertyValues().addPropertyValue(new PropertyValue(KEY_GENERATOR_PROPERTY_NAME, new RuntimeBeanReference(name.trim())));
        }
        return def;
    }

    public void init() {
        registerBeanDefinitionParser("annotation-driven", new AnnotationDrivenCacheBeanDefinitionParser());
        //registerBeanDefinitionParser("advice", new CacheAdviceParser());
        //registerBeanDefinitionParser("dateformat", new SimpleDateFormatBeanDefinitionParser());
    }
}