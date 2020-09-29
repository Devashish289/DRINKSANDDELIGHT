package com.capgemini.drinksanddelight.exception;

public class StockNotFoundException extends RuntimeException{
	public StockNotFoundException(String message) {
		super(message);
	}
}
