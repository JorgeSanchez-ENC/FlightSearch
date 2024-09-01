package com.flightsearch.backend.Services;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AirlineInformationService {
    @Autowired
    AccessTokenService accessTokenService;

    OkHttpClient client = new OkHttpClient();

    public String airlineNameLookUp(String airlineCode) throws IOException {
        String token = accessTokenService.getAccessToken();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://test.api.amadeus.com/v1/reference-data/airlines").newBuilder()
                .addQueryParameter("airlineCodes",airlineCode);

        HttpUrl url = urlBuilder.build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization","Bearer "+ token)
                .build();

        try(Response response = client.newCall(request).execute()){
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            assert response.body() != null;

            JSONObject jsonResponse = new JSONObject(response.body().string());
            JSONArray data = jsonResponse.getJSONArray("data");

            return data.getJSONObject(0).getString("businessName");
        }
    }
}
