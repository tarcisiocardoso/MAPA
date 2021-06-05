package br.gov.mapa.seguranca.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class HashUtil {

	
	public static String getHashMD5(String valor) {
		String myChecksum = valor;
		try {
		    MessageDigest md = MessageDigest.getInstance("MD5");
		    md.update( valor.getBytes() );
		    byte[] digest = md.digest();
		    myChecksum = DatatypeConverter
		      .printHexBinary(digest).toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		return myChecksum;
	}
}
