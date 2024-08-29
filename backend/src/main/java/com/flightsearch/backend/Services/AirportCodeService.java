package com.flightsearch.backend.Services;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AirportCodeService {
    @Autowired
    AccessTokenService accessTokenService;

    OkHttpClient client = new OkHttpClient();

    public String airportSearchByKeyword(String keyword) throws IOException {
        String token = accessTokenService.getAccessToken();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://test.api.amadeus.com/v1/reference-data/locations").newBuilder()
                .addQueryParameter("subType","AIRPORT")
                .addQueryParameter("keyword",keyword);

        HttpUrl url = urlBuilder.build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization","Bearer "+ token)
                .build();

        try(Response response = client.newCall(request).execute()){
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            assert response.body() != null;
            return response.body().string();
        }




    }
}
