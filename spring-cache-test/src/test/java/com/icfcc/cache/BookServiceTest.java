package com.icfcc.cache;

import com.icfcc.cache.ehcache.EhCacheCache;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.writer.CacheWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wanghuanyu on 2015/6/13.
 */
public class BookServiceTest {

    /**
     * 开启全局注释驱动缓存 <spring-cache:annotation-driven />
     */
    @Test
    public void testSrpingcacheAnnotation() {
//        BeanFactory beanFactory= new XmlBeanFactory(new ClassPathResource("spring-cache-annotation.xml"));
        ApplicationContext context = new ClassPathXmlApplicationContext(
                new String[]{"spring-cache-annotation.xml"});

        BookService bookService = (BookService) context.getBean("cacheTest");

        System.out.println("获取相同名称的book时会从缓存中获取");
        Book book1 = bookService.findBook("horn");
        Book book2 = bookService.findBook("horn");
        Book book3 = bookService.findBook("horn");
        System.out.println("book1 : " + book1);
        System.out.println("book2 : " + book2);
        System.out.println("book3 : " + book3);
        System.out.println("book1 == book2 : "+(book1 == book2));
        System.out.println("book1 == book3 : " + (book1 == book3));
        Assert.assertTrue(book1 == book2);
        Assert.assertTrue(book1 == book3);

        //名称长度大于3的才会缓存
        System.out.println("名称长度大于3的才会缓存");
        book1 = bookService.findBookWithCondition("abc");
        book2 = bookService.findBookWithCondition("abc");
        System.out.println("book1 : " + book1);
        System.out.println("book2 : " + book2);
        System.out.println("book3 : " + book3);
        System.out.println("book1 != book2 : " + (book1 != book2));
        Assert.assertTrue(book1 != book2);
        //更新时缓存失效
        System.out.println("更新数据时使缓存失效");
        book1 = bookService.findBook("horn");
        book1 = bookService.updateBook(book1);
        book2 = bookService.findBook("horn");
        book3 = bookService.findBook("horn");
        System.out.println("book1 : " + book1);
        System.out.println("book2 : " + book2);
        System.out.println("book3 : " + book3);
        System.out.println("book1 != book2 : "+(book1 != book2));
        System.out.println("book2 == book3 : "+(book2 == book3));
        Assert.assertTrue(book1 != book2);
        Assert.assertTrue(book2 == book3);

        System.out.println("删除缓存");
        bookService.deleteBook(book1);
        book2 = bookService.findBook("horn");
        book3 = bookService.findBook("horn");
        System.out.println("book1 : " + book1);
        System.out.println("book2 : " + book2);
        System.out.println("book3 : " + book3);
        System.out.println("book1 != book2 : " + (book1 != book2));
        System.out.println("book2 == book3 : "+(book2 == book3));
        Assert.assertTrue(book1 != book2);
        Assert.assertTrue(book2 == book3);
    }

    /**
     * 以切面的方式注册缓存
     */
//    @Test
    public void xml() {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                new String[]{"src/main/resources/spring-cache-xml.xml"});
        BookService CacheTest = (BookService) context.getBean("cacheTest");
        for (int i = 0; i < 100; i++) {
            CacheTest.findBook("a  " + i);
        }

        //直接操作  Ehcache
        CacheManager cacheManager = (CacheManager) context
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
        Assert.assertTrue(book == book2);

        // sleep 到缓存过时
        try {
            Thread.sleep(3 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 缓存过时后，对象会重新创建
        Book book3 = CacheTest.findBook("horn");
        Assert.assertTrue(book != book3);
    }

    @Test
    public void testTime() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        System.out.println(sf.format(new Date()));
    }

}