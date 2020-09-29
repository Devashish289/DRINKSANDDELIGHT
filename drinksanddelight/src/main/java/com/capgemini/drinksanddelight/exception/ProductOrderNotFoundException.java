package com.capgemini.drinksanddelight.exception;

public class ProductOrderNotFoundException extends RuntimeException{

    public ProductOrderNotFoundException(String msg){
        super(msg);
    }

}
