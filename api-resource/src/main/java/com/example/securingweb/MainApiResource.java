package com.example.securingweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApiResource {

	public static void main(String[] args) throws Throwable {
		try{
			SpringApplication.run(MainApiResource.class, args);
		} catch (Throwable e) {
			if(e.getClass().getName().contains("SilentExitException")) {
				System.err.println("Spring is restarting the main thread - See spring-boot-devtools");
			} else {
				System.err.println("Application crashed!"+ e.getMessage());
			}
		}
	}

}
