package com.flightsearch.backend.Services;


import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AccessTokenService {
    @Value("${apiKey}")
    String apiKey;
    @Value("${apiSecret}")
    String apiSecret;

    public String getAccessToken() throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .add("client_id",apiKey)
                .add("client_secret",apiSecret)
                .build();

        Request request = new Request.Builder()
                .url("https://test.api.amadeus.com/v1/security/oauth2/token")
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            assert response.body() != null;
            String responseBody = response.body().string();
            JSONObject jsonObject = new JSONObject(responseBody);
            return jsonObject.getString("access_token");
        }
    }
}
