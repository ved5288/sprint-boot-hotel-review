package com.jar.hotelreview.services;

import java.util.ArrayList;
import java.util.List;

import com.jar.hotelreview.exception.DataConflictException;
import com.jar.hotelreview.exception.ResourceNotFoundException;
import com.jar.hotelreview.models.Hotel;
import com.jar.hotelreview.repositories.HotelRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    public List<Hotel> getAllHotels(){
        List<Hotel> hotels = new ArrayList<>();
        hotelRepository.findAll().forEach(hotels::add);
        return hotels;
    }

    public Hotel getHotel(Integer id){
        return hotelRepository.findById(id)
            .orElseThrow(
                () -> new ResourceNotFoundException("Could not find hotel with id " + String.valueOf(id))
            );
    }

    public Hotel addHotel(Hotel hotel){
        if (hotel == null){
            
        }
        return hotelRepository.save(hotel);
    }

    public Hotel updateHotel(Integer id, Hotel hotel){
        hotelRepository.findById(id)
            .orElseThrow(
                () -> new ResourceNotFoundException("Could not find hotel with id " + String.valueOf(id))
            );
        if (hotel.getId() == null || hotel.getId() == id){
            hotel.setId(id);
            return hotelRepository.save(hotel);
        } else {
            throw new DataConflictException("Requested id " + String.valueOf(id) + " does not match with id of hotel resource " + String.valueOf(hotel.getId()));
        }
    }

    public void deleteHotel(Integer id){
        hotelRepository.findById(id)
            .orElseThrow(
                () -> new ResourceNotFoundException("Could not find hotel with id " + String.valueOf(id))
            );
        hotelRepository.deleteById(id);
    }
}
