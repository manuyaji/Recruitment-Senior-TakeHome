package com.automwrite.assessment.exceptions;

public class AutomLLMException extends RuntimeException{
    public AutomLLMException(){}

    private String message = "Something went wrong.";
    private Exception e;

    public AutomLLMException(String message, Exception e){
        this.e = e;
        this.message = message;
    }
}
