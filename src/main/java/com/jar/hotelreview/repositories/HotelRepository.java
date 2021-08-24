package com.jar.hotelreview.repositories;
import com.jar.hotelreview.models.Hotel;
import org.springframework.data.repository.CrudRepository;

public interface HotelRepository extends CrudRepository<Hotel, Integer> {

}
