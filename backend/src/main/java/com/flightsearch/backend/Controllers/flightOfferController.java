package com.flightsearch.backend.Controllers;


import com.flightsearch.backend.Services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/flightOffer")
public class flightOfferController {
    @Autowired
    SearchService searchService;

    @PostMapping()
    public ResponseEntity<?> getFlightOffers(@RequestBody String originLocationCode, @RequestBody String destinationLocationCode,
     @RequestBody String departureDate, @RequestBody String returnDate, @RequestBody Integer adults, @RequestBody String currencyCode, @RequestBody Boolean nonStop){
        try{
            String response = searchService.flightOfferSearch(originLocationCode,destinationLocationCode,departureDate
            ,returnDate,adults,currencyCode,nonStop);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
