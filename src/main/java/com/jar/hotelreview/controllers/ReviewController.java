package com.jar.hotelreview.controllers;

import java.util.List;

import com.jar.hotelreview.models.Hotel;
import com.jar.hotelreview.models.Review;
import com.jar.hotelreview.services.ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(tags = {"reviews CRUD"})
@RequestMapping("/hotels")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @ApiOperation(value = "Fetch all the reviews for the hotel")
    @GetMapping("/{hotelId}/reviews")
    public List<Review> getAllReviews(@ApiParam(value = "The ID of the existing hotel resource", required = true) @PathVariable Integer hotelId){
        return reviewService.getAllReviews(hotelId);
    }

    @ApiOperation(value = "Fetch the review corresponding to the id for the hotel")
    @GetMapping("/{hotelId}/reviews/{reviewId}")
    public Review getReview(@ApiParam(value = "The ID of the existing hotel resource", required = true) @PathVariable Integer hotelId,
        @ApiParam(value = "The ID of the existing review resource", required = true) @PathVariable Integer reviewId){
        return reviewService.getReview(hotelId, reviewId);
    }

    @ApiOperation(value = "Submit a review for the hotel.")
    @PostMapping("/{hotelId}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public Review addReview(@ApiParam(value = "The review to add for the hotel", required = true) @RequestBody Review review, 
        @ApiParam(value = "The ID of the existing hotel resource", required = true) @PathVariable Integer hotelId){
        review.setHotel(new Hotel(hotelId));
        Review createdReview = reviewService.addReview(hotelId, review);
        return createdReview;
    }

    @ApiOperation(value = "Update the review provided to the hotel")
    @PutMapping("/{hotelId}/reviews/{reviewId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Review updateReview(@ApiParam(value = "The review to update for the hotel", required = true) @RequestBody Review review,
        @ApiParam(value = "The ID of the existing hotel resource", required = true) @PathVariable Integer hotelId,  
        @ApiParam(value = "The ID of the existing review resource", required = true) @PathVariable Integer reviewId){
        Review updatedReview = reviewService.updateReview(hotelId, reviewId, review);
        return updatedReview;
    }

    @ApiOperation(value = "Delete the review")
    @DeleteMapping("/{hotelId}/reviews/{reviewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(@ApiParam(value = "The ID of the existing hotel resource", required = true) @PathVariable Integer hotelId,
        @ApiParam(value = "The ID of the existing review resource", required = true) @PathVariable Integer reviewId){
        reviewService.deleteReview(hotelId, reviewId);
    }
}
