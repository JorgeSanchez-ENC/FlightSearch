package com.flightsearch.backend.Services;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

public class SearchPriceService {
    @Autowired
    AccessTokenService accessTokenService;

    OkHttpClient client = new OkHttpClient();


    public String flightOfferPriceSearch(String offerInfo) throws IOException {
         String token = accessTokenService.getAccessToken();

        RequestBody body = RequestBody.create(
                offerInfo,
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url("https://test.api.amadeus.com/v1/shopping/flight-offers/pricing")
                .post(body)
                .addHeader("Authorization","Bearer "+ token)
                .addHeader("Content-Type","application/json")
                .build();

        try(Response response = client.newCall(request).execute()){
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            assert response.body() != null;
            return response.body().string();
        }
    }
}
