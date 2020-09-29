package com.capgemini.drinksanddelight.exception;

public class RawMaterialOrderNotFoundException extends RuntimeException{
	
	public RawMaterialOrderNotFoundException(String string)
	{
		super(string);
		System.out.println(string);
	}

}
