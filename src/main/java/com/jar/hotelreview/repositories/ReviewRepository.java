package com.jar.hotelreview.repositories;

import org.springframework.data.repository.CrudRepository;
import com.jar.hotelreview.models.Review;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Integer> {

    public List<Review> findByHotelId(Integer hotelId);
}
