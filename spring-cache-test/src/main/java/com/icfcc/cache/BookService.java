package com.icfcc.cache;

import com.icfcc.cache.annotation.Cacheable;
import com.icfcc.cache.annotation.CacheEvict;

/**
 * book service class.
 * Created by wanghuanyu on 2015/6/4.
 */
public class BookService {

    @Cacheable(value = "books", key = "#bookname")
    public Book findBook(String bookname) {
        return new Book(bookname);
    }

    @Cacheable(value = "books", key = "#bookname", condition="#bookname.bytes.length > 3 ")
    public Book findBookWithCondition(String bookname) {
        return new Book(bookname);
    }


//
//    allEntries = true
    @CacheEvict(value = "books", key = "#book.bookname")
    public Book updateBook(Book book) {
        return book;
    }

    @CacheEvict(value = "books", key = "#book.bookname" )
    public void deleteBook(Book book){

    }

}
