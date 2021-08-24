package com.jar.hotelreview.services;

import java.util.ArrayList;
import java.util.List;

import com.jar.hotelreview.repositories.HotelRepository;
import com.jar.hotelreview.repositories.ReviewRepository;
import com.jar.hotelreview.exception.IllegitimateRequestException;
import com.jar.hotelreview.exception.DataConflictException;
import com.jar.hotelreview.exception.ResourceNotFoundException;
import com.jar.hotelreview.models.Hotel;
import com.jar.hotelreview.models.Review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private HotelRepository hotelRepository;

    public List<Review> getAllReviews(Integer hotelId){
        // Check if the hotel with the given hotelId is present
        hotelRepository.findById(hotelId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Could not find hotel with id " + String.valueOf(hotelId))
            );
        
        
        List<Review> reviews = new ArrayList<>();
        reviewRepository.findByHotelId(hotelId).forEach(reviews::add);
        return reviews;
    }

    public Review getReview(Integer hotelId, Integer reviewId){
        // Check if a hotel with the given hotelId is present.
        hotelRepository.findById(hotelId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Could not find hotel with id " + String.valueOf(hotelId))
            );

        // Throw exception if the review corresponding to the id is not present.
        return reviewRepository.findById(reviewId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Could not find review with id " + String.valueOf(reviewId))
            );
    }

    public Review addReview(Integer hotelId, Review review){
        // Check if a hotel with the given hotelId is present.
        Hotel hotel = hotelRepository.findById(hotelId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Could not find hotel with id " + String.valueOf(hotelId))
            );
        
        review.setHotel(hotel);
        return reviewRepository.save(review);
    }

    public Review updateReview(Integer hotelId, Integer reviewId, Review review){
        // Check if a hotel with the given hotelId is present.
        Hotel mappedHotel = hotelRepository.findById(hotelId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Could not find hotel with id " + String.valueOf(hotelId))
            );
        
        // Throw exception if review doesn't exist.
        Review exitingReview = reviewRepository.findById(reviewId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Could not find review with id " + String.valueOf(reviewId))
            );  
        
        // Wrong request exception if the review doesn't correspond to the input hotel id.
        if (exitingReview.getHotel().getId() != hotelId){
            throw new IllegitimateRequestException("Concerned review doesn't correspond to the hotel.");
        }
        
        // Data sanity checks.
        if (review.getId() == null || review.getId() == reviewId){
            review.setId(reviewId);
            review.setHotel(mappedHotel);
            return reviewRepository.save(review);
        }
        
        throw new DataConflictException("Requested id " + String.valueOf(reviewId) + " does not match with id of review resource " + String.valueOf(review.getId()));
    }

    public void deleteReview(Integer hotelId, Integer reviewId){
        // Check if a hotel with the given hotelId is present.
        hotelRepository.findById(hotelId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Could not find hotel with id " + String.valueOf(hotelId))
            );
        
        // Throw exception if review doesn't exist.
        Review exitingReview = reviewRepository.findById(reviewId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Could not find review with id " + String.valueOf(reviewId))
            );  
        
        // Wrong request exception if the review doesn't correspond to the input hotel id.
        if (exitingReview.getHotel().getId() != hotelId){
            throw new IllegitimateRequestException("Concerned review doesn't correspond to the hotel.");
        }

        reviewRepository.deleteById(reviewId);
    }
}
