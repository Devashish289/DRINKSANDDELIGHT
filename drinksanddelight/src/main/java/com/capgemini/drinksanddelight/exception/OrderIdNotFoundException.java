package com.capgemini.drinksanddelight.exception;



	@SuppressWarnings("serial")
	public class OrderIdNotFoundException extends Exception{

		public OrderIdNotFoundException() {
			super();
		}
		
		public OrderIdNotFoundException(String msg) {
			super(msg);
		}
	}


