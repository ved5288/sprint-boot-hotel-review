package com.jar.hotelreview;

import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jar.hotelreview.models.Hotel;
import com.jar.hotelreview.models.Review;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testBasicCrudReview() throws Exception{
        Hotel mHotel1 = mockHotel("shouldCreateRetrieveDelete");
        byte[] mHotel1Json = toJson(mHotel1);

        //CREATE HOTEL
        MvcResult result = mockMvc.perform(post("/hotels/")
                .content(mHotel1Json)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(mHotel1.getName())))
                .andExpect(jsonPath("$.city", is(mHotel1.getCity())))
                .andReturn();

        JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
        Integer hotelId = jsonObject.getInt("id");

        Review mReview1 = mockReview("goodreview");
        byte[] mReview1Json = toJson(mReview1);

        //CREATE REVIEW
        result = mockMvc.perform(post("/hotels/"+hotelId+"/reviews/")
                .content(mReview1Json)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description", is(mReview1.getDescription())))
                .andExpect(jsonPath("$.score", is(mReview1.getScore())))
                .andExpect(jsonPath("$.hotel.id", is(hotelId)))
                .andExpect(jsonPath("$.hotel.name", is(mHotel1.getName())))
                .andExpect(jsonPath("$.hotel.city", is(mHotel1.getCity())))
                .andReturn();

        jsonObject = new JSONObject(result.getResponse().getContentAsString());
        Integer reviewId = jsonObject.getInt("id");

        //RETRIEVE
        mockMvc.perform(get("/hotels/"+hotelId+"/reviews/"+reviewId)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(reviewId)))
            .andExpect(jsonPath("$.description", is(mReview1.getDescription())))
            .andExpect(jsonPath("$.score", is(mReview1.getScore())))
            .andExpect(jsonPath("$.hotel.id", is(hotelId)))
            .andExpect(jsonPath("$.hotel.name", is(mHotel1.getName())))
            .andExpect(jsonPath("$.hotel.city", is(mHotel1.getCity())));

        //UPDATE
        mReview1.setDescription("updatedDescription");;
        mReview1Json = toJson(mReview1);

        mockMvc.perform(put("/hotels/"+hotelId+"/reviews/"+reviewId)
            .content(mReview1Json)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isAccepted())
            .andExpect(jsonPath("$.id", is(reviewId)))
            .andExpect(jsonPath("$.description", is(mReview1.getDescription())))
            .andExpect(jsonPath("$.score", is(mReview1.getScore())))
            .andExpect(jsonPath("$.hotel.id", is(hotelId)))
            .andExpect(jsonPath("$.hotel.name", is(mHotel1.getName())))
            .andExpect(jsonPath("$.hotel.city", is(mHotel1.getCity())));
        
        //DELETE
        mockMvc.perform(delete("/hotels/"+hotelId+"/reviews/"+reviewId))
            .andExpect(status().isNoContent());
    }

    @Test
    void testAddReviewToNonExistentHotel() throws Exception {
        Hotel r1 = mockHotel("shouldCreateRetrieveDelete");
        byte[] r1Json = toJson(r1);

        //CREATE
        MvcResult result = mockMvc.perform(post("/hotels/")
                .content(r1Json)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(r1.getName())))
                .andExpect(jsonPath("$.city", is(r1.getCity())))
                .andReturn();

        JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
        Integer id = jsonObject.getInt("id");

        //DELETE
        mockMvc.perform(delete("/hotels/" + id))
                .andExpect(status().isNoContent());

        //ADD to non-existing hotel must fail.
        Review mReview1 = mockReview("nohotel");
        byte[] mReview1Json = toJson(mReview1); 
        mockMvc.perform(post("/hotels/"+id+"/reviews/") 
                .content(mReview1Json)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private Hotel mockHotel(String prefix) {
        Hotel mockHotel = new Hotel();
        mockHotel.setCity(prefix + "_city");
        mockHotel.setName(prefix + "_name");
        return mockHotel;
    }

    private Review mockReview(String prefix) {
        Review mockReview = new Review();
        mockReview.setDescription(prefix + "_desc");
        mockReview.setScore(new Random().nextInt(10));
        return mockReview;
    }

    private byte[] toJson(Object r) throws Exception {
        ObjectMapper map = new ObjectMapper();
        return map.writeValueAsString(r).getBytes();
    }
}
