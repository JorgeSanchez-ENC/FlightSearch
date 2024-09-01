package com.flightsearch.backend.Controllers;


import com.flightsearch.backend.Services.AirportCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/flightOfferPrice")
public class airportCodeController {
    @Autowired
    AirportCodeService airportCodeService;

    @PostMapping
    public ResponseEntity<?> getMatchingAirportCodes(@RequestBody String keyword){
        try {
            List<Map<String,String>> response = airportCodeService.airportCodeSearchByKeyword(keyword);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}