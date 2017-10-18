package edu.sjsu.cmpe275.aop;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	public static void main(String[] args) {
        /***
         * Following is a dummy implementation of App to demonstrate bean creation with Application context.
         * You may make changes to suit your need, but this file is NOT part of the submission.
         */

    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml");
        BlogService blogService = (BlogService) ctx.getBean("blogService");

        try {
        	blogService.shareBlog("Alice", "Alice", "Bob");
            blogService.shareBlog("Bob", "Alice", "Tom");
//            try {
//                blogService.shareBlog("Tom", "Alice", "alex");
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
            blogService.shareBlog("Tom", "Alice", "alex");
            blogService.readBlog("alex", "Alice");
            blogService.commentOnBlog("Bob", "Alice", "I");
            blogService.unshareBlog("Alice", "Bob");
            blogService.readBlog("Bob", "Alice");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ctx.close();
    }
}