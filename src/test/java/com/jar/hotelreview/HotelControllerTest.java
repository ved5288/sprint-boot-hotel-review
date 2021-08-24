package com.jar.hotelreview;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jar.hotelreview.models.ExceptionDetails;
import com.jar.hotelreview.models.Hotel;

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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
@SpringBootTest
public class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testBasicCrudHotel() throws Exception {
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

        //RETRIEVE
        mockMvc.perform(get("/hotels/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.name", is(r1.getName())))
                .andExpect(jsonPath("$.city", is(r1.getCity())));

        r1.setCity("updatedCity");;
        r1Json = toJson(r1);
               
        //UPDATE
        mockMvc.perform(put("/hotels/" + id)
                .content(r1Json)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.name", is(r1.getName())))
                .andExpect(jsonPath("$.city", is(r1.getCity())));

        //DELETE
        mockMvc.perform(delete("/hotels/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void testHotelNotFound() throws Exception{
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

        //RETRIEVE should fail
        result = mockMvc.perform(get("/hotels/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ExceptionDetails expectedErrorResponse = new ExceptionDetails(
            "Could not find hotel with id " + id,
            "Requested resource is not present.");
        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = new ObjectMapper().writeValueAsString(expectedErrorResponse);
        assertTrue(expectedResponseBody.equals(actualResponseBody));
    }

    private Hotel mockHotel(String prefix) {
        Hotel mockHotel = new Hotel();
        mockHotel.setCity(prefix + "_city");
        mockHotel.setName(prefix + "_name");
        return mockHotel;
    }

    private byte[] toJson(Object r) throws Exception {
        ObjectMapper map = new ObjectMapper();
        return map.writeValueAsString(r).getBytes();
    }
}
