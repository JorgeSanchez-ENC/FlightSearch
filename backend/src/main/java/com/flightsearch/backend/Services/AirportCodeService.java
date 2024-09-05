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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AirportCodeService {
    @Autowired
    AccessTokenService accessTokenService;

    OkHttpClient client = new OkHttpClient();

    String mockResponse = "{\n" +
            "  \"meta\": {\n" +
            "    \"count\": 2,\n" +
            "    \"links\": {\n" +
            "      \"self\": \"https://test.api.amadeus.com/v1/reference-data/locations?subType=CITY,AIRPORT&keyword=MUC&countryCode=DE\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"data\": [\n" +
            "    {\n" +
            "      \"type\": \"location\",\n" +
            "      \"subType\": \"AIRPORT\",\n" +
            "      \"name\": \"MUNICH INTERNATIONAL\",\n" +
            "      \"detailedName\": \"MUNICH/DE:MUNICH INTERNATIONAL\",\n" +
            "      \"id\": \"AMUC\",\n" +
            "      \"self\": {\n" +
            "        \"href\": \"https://test.api.amadeus.com/v1/reference-data/locations/AMUC\",\n" +
            "        \"methods\": [\n" +
            "          \"GET\"\n" +
            "        ]\n" +
            "      },\n" +
            "      \"timeZoneOffset\": \"+02:00\",\n" +
            "      \"iataCode\": \"MUC\",\n" +
            "      \"geoCode\": {\n" +
            "        \"latitude\": 48.35378,\n" +
            "        \"longitude\": 11.78609\n" +
            "      },\n" +
            "      \"address\": {\n" +
            "        \"cityName\": \"MUNICH\",\n" +
            "        \"cityCode\": \"MUC\",\n" +
            "        \"countryName\": \"GERMANY\",\n" +
            "        \"countryCode\": \"DE\",\n" +
            "        \"regionCode\": \"EUROP\"\n" +
            "      },\n" +
            "      \"analytics\": {\n" +
            "        \"travelers\": {\n" +
            "          \"score\": 27\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  ]\n" +
            "}";


    public String commonSearch(String keyword) throws IOException {
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

    public List<Map<String,String>> airportCodeSearchByKeyword(String keyword) throws IOException {
        List<Map<String,String>> airportCodes = new ArrayList<>();

        JSONObject jsonResponse = new JSONObject(commonSearch(keyword));
        JSONArray data = jsonResponse.getJSONArray("data");

        for(int i = 0; i<data.length(); i++){
            JSONObject airport = data.getJSONObject(i);

            String name = airport.getString("name");
            String iataCode = airport.getString("iataCode");

            Map<String,String> airportMap = new HashMap<>();
            airportMap.put("name",name);
            airportMap.put("iataCode",iataCode);

            airportCodes.add(airportMap);

        }
        return airportCodes;
    }

    public String airportNameSearchByKeyword(String keyword) throws IOException {
        JSONObject jsonResponse = new JSONObject(commonSearch(keyword));
        JSONArray data = jsonResponse.getJSONArray("data");
        return data.getJSONObject(0).getString("name");
    }
}
