package br.gov.mapa.seguranca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ch.qos.logback.core.net.SyslogOutputStream;

@SpringBootApplication
public class SecuringWebApplication {

	public static void main(String[] args) throws Throwable {
		try {
			SpringApplication.run(SecuringWebApplication.class, args);
		} catch (Throwable e) {
	        if(e.getClass().getName().contains("SilentExitException")) {
	            System.err.println("Spring is restarting the main thread - See spring-boot-devtools");
	        } else {
	            System.err.println("Application crashed!"+ e.getMessage());
	        }
	    }
	}
}
