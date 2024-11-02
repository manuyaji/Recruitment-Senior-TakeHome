package com.automwrite.assessment.exceptions;

public class AutomRuntimeException extends RuntimeException{

    private String message = "Something went wrong.";
    private Exception e;

    public AutomRuntimeException(String message, Exception e){
        this.e = e;
        this.message = message;
    }
}
