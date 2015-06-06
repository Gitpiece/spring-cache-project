package me.ehcache.persistence;

import net.sf.ehcache.CacheEntry;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.writer.CacheWriter;
import net.sf.ehcache.writer.writebehind.operations.SingleOperationType;

import java.util.Collection;
import java.util.Properties;

class WriteThroughTestCacheWriter implements CacheWriter{

	public WriteThroughTestCacheWriter(Ehcache cache, Properties properties) {
		System.out.println("create WriteThroughTestCacheWriter.");
	}

	public CacheWriter clone(Ehcache cache) throws CloneNotSupportedException {
		return null;
	}

	public void init() {
		System.out.println("init..");
	}

	public void dispose() throws CacheException {
		System.out.println("dispose..");
	}

	public void write(Element element) throws CacheException {
		System.out.println("write.."+element.getObjectKey().toString());
	}

	public void writeAll(Collection<Element> elements) throws CacheException {
		for (Element element : elements) {
			write(element);
		}
	}

	public void delete(CacheEntry entry) throws CacheException {
		System.out.println("delete.."+entry.getElement().getObjectKey().toString());
	}

	public void deleteAll(Collection<CacheEntry> entries) throws CacheException {
		for (CacheEntry entry : entries) {
			delete(entry);
		}
	}

	public void throwAway(Element arg0, SingleOperationType arg1,
			RuntimeException arg2) {
		System.out.println("throwAway..");
	}
}
