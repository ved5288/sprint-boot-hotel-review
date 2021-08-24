package com.jar.hotelreview.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ExceptionDetails {

    private final String message;
    private final String details;

    public ExceptionDetails(Exception e, String message){
        this.message = message;
        this.details = e.getLocalizedMessage();
    }

    public ExceptionDetails(String details, String message){
        this.message = message;
        this.details = details;
    }

    public String getMessage(){
        return message;
    }

    public String getDetails(){
        return details;
    }
}
