package com.flightsearch.backend.Services;


import java.time.Duration;
import okhttp3.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AccessTokenService {

    private static final Logger log = LoggerFactory.getLogger(AccessTokenService.class);

    @Value("${apiKey}")
    String apiKey;
    @Value("${apiSecret}")
    String apiSecret;

    public String getAccessToken() throws IOException {
        long startTime = System.nanoTime();
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
            String accessToken = jsonObject.getString("access_token");
            long elapsedTime = Duration.ofNanos(System.nanoTime() - startTime).toMillis();
            log.trace("OAuth2 Token API call elapsed time {}ms", String.format("%,d", elapsedTime));
            return accessToken;
        }
    }
}
