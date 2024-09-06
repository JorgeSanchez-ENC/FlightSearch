package com.flightsearch.backend.Controllers;


import com.flightsearch.backend.Services.SearchService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/flightOffer")
public class flightOfferController {
    @Autowired
    SearchService searchService;

    @GetMapping()
    public ResponseEntity<?> getFlightOffers(@RequestParam String originLocationCode, @RequestParam String destinationLocationCode,
     @RequestParam String departureDate, @RequestParam(required = false) String returnDate, @RequestParam Integer adults, @RequestParam String currencyCode, @RequestParam Boolean nonStop){

        try{
            List<JSONObject> response = searchService.flightOfferSearch(originLocationCode,destinationLocationCode,departureDate
            ,returnDate,adults,currencyCode,nonStop);
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/sort")
    public ResponseEntity<?> sortFlightOffers(@RequestParam String type, @RequestParam String info ){
        try{
            JSONArray response = searchService.sort(info,type);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
