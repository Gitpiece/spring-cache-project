/*
 * Copyright 2002-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.icfcc.cache.annotation;

import com.icfcc.cache.interceptor.BeanFactoryCacheOperationSourceAdvisor;
import com.icfcc.cache.interceptor.CacheInterceptor;
import com.icfcc.cache.interceptor.CacheOperationSource;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Role;

/**
 * {@code @Configuration} class that registers the Spring infrastructure beans necessary
 * to enable proxy-based annotation-driven cache management.
 *
 * @author Chris Beams
// * @see EnableCaching
 * @see CachingConfigurationSelector
 * @since 3.1
 */
//@Configuration
public class ProxyCachingConfiguration extends AbstractCachingConfiguration {

    // @Bean(name = AnnotationConfigUtils.CACHE_ADVISOR_BEAN_NAME)
    // @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public BeanFactoryCacheOperationSourceAdvisor cacheAdvisor() {
        BeanFactoryCacheOperationSourceAdvisor advisor =
                new BeanFactoryCacheOperationSourceAdvisor();
        advisor.setCacheOperationSource(cacheOperationSource());
        //advisor.setAdvice(cacheInterceptor());
        advisor.setOrder(((Integer) this.enableCaching.get("order")));
        return advisor;
    }

    // @Bean
    // @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public CacheOperationSource cacheOperationSource() {
        return new AnnotationCacheOperationSource();
    }

    // @Bean
    // @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public CacheInterceptor cacheInterceptor() {
        CacheInterceptor interceptor = new CacheInterceptor();
        interceptor.setCacheOperationSources(cacheOperationSource());
        if (this.cacheManager != null) {
            interceptor.setCacheManager(this.cacheManager);
        }
        if (this.keyGenerator != null) {
            interceptor.setKeyGenerator(this.keyGenerator);
        }
        return interceptor;
    }

}
