package com.icfcc.cache;

import com.icfcc.cache.annotation.Cacheable;
import com.icfcc.cache.ehcache.EhCacheCache;
import com.icfcc.cache.support.SimpleCacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.writer.CacheWriter;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wanghuanyu on 2015/6/4.
 */
public class CacheTest {

    @Test
    public void testspringtest() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-cache-test.xml");
        SimpleDateFormat sd = (SimpleDateFormat) applicationContext.getBean("defaultDateFormat");
        System.out.println(sd.toString());
        sd = (SimpleDateFormat) applicationContext.getBean("defaultDateFormat");
        System.out.println(sd.toString());
        System.exit(0);
    }
    @Cacheable(value = "books", key = "#bookname")
    public Book findBook(String bookname) {
        return new Book(bookname);
    }

    /**
     * 开启全局注释驱动缓存 <code><cache:annotation-driven /></code>
     */
    @Test
    public void annotation() {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                new String[]{"spring-annotation-cache.xml"});

        CacheTest CacheTest = (CacheTest) context.getBean("cacheTest");
        Book book = CacheTest.findBook("horn");
        Book book2 = CacheTest.findBook("horn");

        SimpleCacheManager cacheManager = (SimpleCacheManager) context
                .getBean("cacheManager");

        assertTrue(book == book2);
    }

    /**
     * 以切面的方式注册缓存
     */
    @Test
    public void xml() {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                new String[]{"spring-xml-cache.xml"});

        CacheTest CacheTest = (CacheTest) context.getBean("cacheTest");
        for (int i = 0; i < 100; i++) {
            CacheTest.findBook("a  " + i);
        }

        //直接操作  Ehcache
        com.icfcc.cache.CacheManager cacheManager = (com.icfcc.cache.CacheManager) context
                .getBean("cacheManager");
        EhCacheCache cache = (EhCacheCache) cacheManager.getCache("books");
        Ehcache ehcache = cache.getNativeCache();
        ehcache.putWithWriter(new Element("why", CacheTest.findBook("horn")));
        ehcache.removeWithWriter("why");
        ehcache.putWithWriter(new Element("horn", CacheTest.findBook("horn")));
        CacheWriter cachewrite = ehcache.getRegisteredCacheWriter();

        // 创建相同名称对象，缓存生效
        Book book = CacheTest.findBook("horn");
        Book book2 = CacheTest.findBook("horn");
        assertTrue(book == book2);

        // sleep 到缓存过时
        try {
            Thread.sleep(3 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 缓存过时后，对象会重新创建
        Book book3 = CacheTest.findBook("horn");
        assertTrue(book != book3);
    }

    public void testTime() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        System.out.println(sf.format(new Date()));
    }



    private void assertTrue(boolean b) {
        if (b) throw new RuntimeException("not true");
    }
}
