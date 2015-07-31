package com.icfcc.cache;

/**
 * pojo object
 */
public class Book {

	private String bookname;

	public String getBookname() {
		return bookname;
	}

	public void setBookname(String bookname) {
		this.bookname = bookname;
	}

	public Book(String bookname) {
		this.bookname = bookname;
	}


	public String toString(){
		return this.bookname + " : " +super.toString();
	}

}