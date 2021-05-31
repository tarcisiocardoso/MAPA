package com.example.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConvertePdf {

	public static void main(String[] args) {
		
		byte[] inFileBytes;
		try {
			inFileBytes = Files.readAllBytes(Paths.get("/home/tarcisio/Documentos/xx.pdf"));
		
			byte[] encoded = java.util.Base64.getEncoder().encode(inFileBytes);
			
			System.out.println( encoded );
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
