package com.flightsearch.backend.Controllers;

import com.flightsearch.backend.Services.SearchService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(flightOfferController.class)
public class flightOfferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    private List<JSONObject> mockFlightOffers;

    @BeforeEach
    public void setUp() {
        mockFlightOffers = new ArrayList<>();
        JSONObject flightOffer = new JSONObject();
        flightOffer.put("id", "1");
        flightOffer.put("carrierCode", "VA");
        mockFlightOffers.add(flightOffer);
    }

    @Test
    public void testGetFlightOffers() throws Exception {
        when(searchService.flightOfferSearch("SYD", "BKK", "2024-09-16", "2024-09-30", 2, "USD", true))
                .thenReturn(mockFlightOffers);

        // Perform the request and check the response
        mockMvc.perform(get("/flightOffer")
                        .param("originLocationCode", "SYD")
                        .param("destinationLocationCode", "BKK")
                        .param("departureDate", "2024-09-16")
                        .param("returnDate", "2024-09-30")
                        .param("adults", "2")
                        .param("currencyCode", "USD")
                        .param("nonStop", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mockFlightOffers.toString()));
    }


    @Test
    public void testSortFlightOffers() throws Exception {
        when(searchService.sort("price")).thenReturn(mockFlightOffers);

        mockMvc.perform(get("/flightOffer/sort")
                        .param("type", "price")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mockFlightOffers.toString()));
    }

}
