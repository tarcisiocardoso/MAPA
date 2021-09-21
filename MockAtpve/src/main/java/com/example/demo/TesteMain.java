package com.example.demo;

public class TesteMain {

	public static void main(String[] args) {
		TesteMain t = new TesteMain();
		System.out.println( t.formataValor("R$5.000,25"));
	}
	
	private double formataValor(String str) {
		str = str.replace("R", "");
		str = str.replace("$", "");
		str = str.replaceAll("\\.", "");
		str = str.replaceAll(",", ".");
		
		return Double.parseDouble(str.trim());
	}
}
