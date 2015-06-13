package com.icfcc.cache.ehcache.support;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.writer.CacheWriter;
import net.sf.ehcache.writer.CacheWriterFactory;

import java.util.Properties;

public class EhCacheWriterWriteThroughFactory extends CacheWriterFactory {

	@Override
	public CacheWriter createCacheWriter(Ehcache cache, Properties properties) {
		return new EhCacheWriterWriteThrough(cache,properties);
	}

}
