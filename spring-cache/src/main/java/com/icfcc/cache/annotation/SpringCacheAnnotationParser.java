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

import com.icfcc.cache.interceptor.CacheEvictOperation;
import com.icfcc.cache.interceptor.CacheOperation;
import com.icfcc.cache.interceptor.CachePutOperation;
import com.icfcc.cache.interceptor.CacheableOperation;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Collection;


/**
 * Strategy implementation for parsing Spring's {@link Caching}, {@link Cacheable},
 * {@link CacheEvict} and {@link CachePut} annotations.
 *
 * @author Costin Leau
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 3.1
 */
@SuppressWarnings("serial")
public class SpringCacheAnnotationParser implements CacheAnnotationParser, Serializable {

	public Collection<CacheOperation> parseCacheAnnotations(AnnotatedElement ae) {
		Collection<CacheOperation> ops = null;

		Collection<Cacheable> cacheables = getAnnotations(ae, Cacheable.class);
		if (cacheables != null) {
			ops = lazyInit(ops);
			for (Cacheable cacheable : cacheables) {
				ops.add(parseCacheableAnnotation(ae, cacheable));
			}
		}
		Collection<CacheEvict> evicts = getAnnotations(ae, CacheEvict.class);
		if (evicts != null) {
			ops = lazyInit(ops);
			for (CacheEvict e : evicts) {
				ops.add(parseEvictAnnotation(ae, e));
			}
		}
		Collection<CachePut> updates = getAnnotations(ae, CachePut.class);
		if (updates != null) {
			ops = lazyInit(ops);
			for (CachePut p : updates) {
				ops.add(parseUpdateAnnotation(ae, p));
			}
		}
		Collection<Caching> caching = getAnnotations(ae, Caching.class);
		if (caching != null) {
			ops = lazyInit(ops);
			for (Caching c : caching) {
				ops.addAll(parseCachingAnnotation(ae, c));
			}
		}
		return ops;
	}

	private <T extends Annotation> Collection<CacheOperation> lazyInit(Collection<CacheOperation> ops) {
		return (ops != null ? ops : new ArrayList<CacheOperation>(1));
	}

	CacheableOperation parseCacheableAnnotation(AnnotatedElement ae, Cacheable caching) {
		CacheableOperation cuo = new CacheableOperation();
		cuo.setCacheNames(caching.value());
		cuo.setCondition(caching.condition());
		cuo.setKey(caching.key());
		cuo.setName(ae.toString());
		return cuo;
	}

	CacheEvictOperation parseEvictAnnotation(AnnotatedElement ae, CacheEvict caching) {
		CacheEvictOperation ceo = new CacheEvictOperation();
		ceo.setCacheNames(caching.value());
		ceo.setCondition(caching.condition());
		ceo.setKey(caching.key());
		ceo.setCacheWide(caching.allEntries());
		ceo.setBeforeInvocation(caching.beforeInvocation());
		ceo.setName(ae.toString());
		return ceo;
	}

	CacheOperation parseUpdateAnnotation(AnnotatedElement ae, CachePut caching) {
		CachePutOperation cuo = new CachePutOperation();
		cuo.setCacheNames(caching.value());
		cuo.setCondition(caching.condition());
		cuo.setKey(caching.key());
		cuo.setName(ae.toString());
		return cuo;
	}

	Collection<CacheOperation> parseCachingAnnotation(AnnotatedElement ae, Caching caching) {
		Collection<CacheOperation> ops = null;

		Cacheable[] cacheables = caching.cacheable();
		if (!ObjectUtils.isEmpty(cacheables)) {
			ops = lazyInit(ops);
			for (Cacheable cacheable : cacheables) {
				ops.add(parseCacheableAnnotation(ae, cacheable));
			}
		}
		CacheEvict[] evicts = caching.evict();
		if (!ObjectUtils.isEmpty(evicts)) {
			ops = lazyInit(ops);
			for (CacheEvict evict : evicts) {
				ops.add(parseEvictAnnotation(ae, evict));
			}
		}
		CachePut[] updates = caching.put();
		if (!ObjectUtils.isEmpty(updates)) {
			ops = lazyInit(ops);
			for (CachePut update : updates) {
				ops.add(parseUpdateAnnotation(ae, update));
			}
		}

		return ops;
	}

	private static <T extends Annotation> Collection<T> getAnnotations(AnnotatedElement ae, Class<T> annotationType) {
		Collection<T> anns = new ArrayList<T>(2);

		// look at raw annotation
		T ann = ae.getAnnotation(annotationType);
		if (ann != null) {
			anns.add(ann);
		}

		// scan meta-annotations
		for (Annotation metaAnn : ae.getAnnotations()) {
			ann = metaAnn.annotationType().getAnnotation(annotationType);
			if (ann != null) {
				anns.add(ann);
			}
		}

		return (anns.isEmpty() ? null : anns);
	}
}