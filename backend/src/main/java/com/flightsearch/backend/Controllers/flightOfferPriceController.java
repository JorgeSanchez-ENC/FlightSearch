package com.flightsearch.backend.Controllers;


import com.flightsearch.backend.Services.SearchPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/flightOfferPrice")
public class flightOfferPriceController {
    @Autowired
    SearchPriceService searchPriceService;

    @PostMapping
    public ResponseEntity<?> getFlightOfferPrice(@RequestBody String offerInfo){
        try {
            String response = searchPriceService.flightOfferPriceSearch(offerInfo);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
