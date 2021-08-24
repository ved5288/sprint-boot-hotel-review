package com.jar.hotelreview.controllers;

import java.util.List;

import com.jar.hotelreview.models.Hotel;
import com.jar.hotelreview.services.HotelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(tags = {"hotels CRUD"})
@RequestMapping("/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @ApiOperation(value = "Fetch the list of all hotels")
    @GetMapping(value = "/", produces = {"application/json", "application/xml"})
    public List<Hotel> getAllHotels(){
        return hotelService.getAllHotels();
    }

    @ApiOperation(value = "Fetch the hotel for the input id")
    @GetMapping(value = "/{id}", produces = {"application/json", "application/xml"})
    public Hotel getHotel(@ApiParam(value = "The ID of the existing hotel resource", required = true) @PathVariable Integer id){
        return hotelService.getHotel(id);
    }

    @ApiOperation(value = "Add a new hotel to the database")
    @PostMapping(
        value = "/",
        consumes = {"application/json", "application/xml"},
        produces = {"application/json", "application/xml"}
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Hotel addHotel(@ApiParam(value = "The hotel resource to add", required = true) @RequestBody Hotel hotel)
    {
        Hotel createdHotel = hotelService.addHotel(hotel);
        return createdHotel;
    }

    @ApiOperation(value = "Update the hotel information for the given hotel id")
    @PutMapping(
        value = "/{id}",
        consumes = {"application/json", "application/xml"},
        produces = {"application/json", "application/xml"}
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Hotel updateHotel(@ApiParam(value = "The hotel resource to update", required = true) @RequestBody Hotel hotel, 
        @ApiParam(value = "The ID of the existing hotel resource", required = true) @PathVariable Integer id)
    {
        Hotel updatedHotel = hotelService.updateHotel(id, hotel);
        return updatedHotel;
    }

    @ApiOperation(value = "Delete the hotel corresponding to the input id")
    @DeleteMapping(value = "/{id}", produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHotel(@ApiParam(value = "The ID of the existing hotel resource", required = true) @PathVariable Integer id){
        hotelService.deleteHotel(id);
    }
}
