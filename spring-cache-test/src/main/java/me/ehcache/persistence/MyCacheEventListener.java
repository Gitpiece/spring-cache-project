package me.ehcache.persistence;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

import java.util.Properties;

/**
 * 持久化坚挺类当缓存数据时，可以进行持久化存储。
 * @author wanghuanyu
 *
 */
public class MyCacheEventListener implements CacheEventListener {


	public MyCacheEventListener(Properties properties) {

	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return null;
	}

	public void notifyElementRemoved(Ehcache cache, Element element)
			throws CacheException {
		System.out.println("notifyElementRemoved	-	"+element.getObjectKey().toString());
	}

	public void notifyElementPut(Ehcache cache, Element element)
			throws CacheException {
		System.out.println("notifyElementPut	-	"+element.getObjectKey().toString());
	}

	public void notifyElementUpdated(Ehcache cache, Element element)
			throws CacheException {
		System.out.println("notifyElementUpdated	-	"+element.getObjectKey().toString());
	}

	public void notifyElementExpired(Ehcache cache, Element element) {
		System.out.println("notifyElementExpired	-	"+element.getObjectKey().toString());
	}

	public void notifyElementEvicted(Ehcache cache, Element element) {
		System.out.println("notifyElementEvicted	-	"+element.getObjectKey().toString());
	}

	public void notifyRemoveAll(Ehcache cache) {
		System.out.println("notifyRemoveAll");
		
	}

	public void dispose() {
		System.out.println("dispose");
	}

}
