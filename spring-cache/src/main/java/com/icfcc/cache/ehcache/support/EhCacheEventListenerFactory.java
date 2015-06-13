package com.icfcc.cache.ehcache.support;

import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.event.CacheEventListenerFactory;

import java.util.Properties;

/**
 * 持久化坚挺工程
 * @author wanghuanyu
 *
 */
public class EhCacheEventListenerFactory extends
		CacheEventListenerFactory {

	@Override
	public CacheEventListener createCacheEventListener(Properties properties) {
		return new EhCacheEventListener(properties);
	}

}
