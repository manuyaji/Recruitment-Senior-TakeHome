package com.automwrite.assessment.exceptions;

public class AutomException extends RuntimeException{

    private String message = "Something went wrong.";
    private Exception e;

    public AutomException(String message, Exception e){
        this.e = e;
        this.message = message;
    }
}