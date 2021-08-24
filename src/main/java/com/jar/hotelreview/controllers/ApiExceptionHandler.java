package com.jar.hotelreview.controllers;

import javax.servlet.http.HttpServletResponse;

import com.jar.hotelreview.exception.IllegitimateRequestException;
import com.jar.hotelreview.exception.DataConflictException;
import com.jar.hotelreview.exception.ResourceNotFoundException;
import com.jar.hotelreview.models.ExceptionDetails;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {IllegitimateRequestException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public @ResponseBody ExceptionDetails handleApiException(IllegitimateRequestException e){
        return new ExceptionDetails(e, "The request is not valid.");
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ExceptionDetails handleApiException(
        ResourceNotFoundException e,
        WebRequest request,
        HttpServletResponse response)
    {
        return new ExceptionDetails(e, "Requested resource is not present.");
    }
    
    // Handle a system exception.
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ExceptionDetails handleApiException(
        HttpMessageNotReadableException e,
        WebRequest request,
        HttpServletResponse response)
    {
        return new ExceptionDetails(e, "Error in parsing the input.");
    }

    @ExceptionHandler(value = {DataConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody ExceptionDetails handleApiException(
        DataConflictException e,
        WebRequest request,
        HttpServletResponse response)
    {
        return new ExceptionDetails(e, "Conflict in data.");
    }
    
}
