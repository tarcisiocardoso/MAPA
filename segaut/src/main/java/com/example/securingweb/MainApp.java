package com.example.securingweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApp {

	public static void main(String[] args) throws Throwable {
		try {
			SpringApplication.run(MainApp.class, args);
		} catch (Throwable e) {
	        if(e.getClass().getName().contains("SilentExitException")) {
	            System.err.println("Spring is restarting the main thread - See spring-boot-devtools");
	        } else {
	            System.err.println("Application crashed!"+ e.getMessage());
	        }
	    }
	}

}
