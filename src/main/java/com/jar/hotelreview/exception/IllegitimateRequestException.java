package com.jar.hotelreview.exception;

public class IllegitimateRequestException extends RuntimeException{
    
    public IllegitimateRequestException(String message){
        super(message);
    }

    public IllegitimateRequestException(String message, Throwable cause){
        super(message, cause);
    }
}
