package edu.sjsu.cmpe275.aop;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	public static void main(String[] args) {
        /***
         * Following is a dummy implementation of App to demonstrate bean creation with Application context.
         * You may make changes to suit your need, but this file is NOT part of the submission.
         */

    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml");
        ProfileService profileService = (ProfileService) ctx.getBean("profileService");

        try {
        	profileService.unshareProfile("Alice", "Alice");
        	profileService.shareProfile("Alice", "Alice", "Bob");
        	profileService.shareProfile("Bob", "Alice", "Alice");
        	//profileService.shareProfile("Carl", "Carl", "Alice");
        	//profileService.shareProfile("Bob", "Bob", "Alice");
        	//profileService.shareProfile("Alice", "Bob", "Carl");
            //profileService.readProfile("Alice", "Bob");
            //profileService.unshareProfile("Alice", "Bob");
            //profileService.unshareProfile("Bob", "Carl");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ctx.close();
    }
}