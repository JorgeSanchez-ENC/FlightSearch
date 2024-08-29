package com.flightsearch.backend.Services;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class SearchService {
    @Autowired
    AccessTokenService accessTokenService;

    @Autowired
    static AirportCodeService airportCodeService;

    @Autowired
    static AirlineInformationService airlineInformationService;

    OkHttpClient client = new OkHttpClient();

    public SearchService() throws IOException {
    }

    public String flightOfferSearch(String originLocationCode, String destinationLocationCode,
        String departureDate, String returnDate, Integer adults, String currencyCode, Boolean nonStop
    ) throws IOException {
        String token = accessTokenService.getAccessToken();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://test.api.amadeus.com/v2/shopping/flight-offers").newBuilder()
                .addQueryParameter("originLocationCode",originLocationCode)
                .addQueryParameter("destinationLocationCode",destinationLocationCode)
                .addQueryParameter("departureDate",departureDate)
                .addQueryParameter("adults",adults.toString())
                .addQueryParameter("currencyCode", currencyCode )
                .addQueryParameter("nonStop", nonStop.toString());
        if(returnDate != null && !returnDate.isEmpty()){
            urlBuilder.addQueryParameter("returnDate", returnDate);
        }

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

    private static String addAirportAndAirlinesCommonNames(String originalJson) throws IOException {
        JSONObject jsonResponse =new JSONObject(originalJson);
        JSONArray dataArray = jsonResponse.getJSONArray("data");

        for(int i = 0; i < dataArray.length(); i++){
            JSONObject flightOffer = dataArray.getJSONObject(i);
            JSONArray itineraries = flightOffer.getJSONArray("itineraries");
            for(int j = 0; j < itineraries.length(); j++){
                JSONObject itinerarie = itineraries.getJSONObject(j);
                JSONArray segments = itinerarie.getJSONArray("segments");
                for(int k = 0; k < segments.length(); k++){
                    JSONObject segment = segments.getJSONObject(k);

                    String deptIATA = segment.getJSONObject("departure").getString("iataCode");
                    String deptAirportName = airportCodeService.airportSearchByKeyword(deptIATA);
                    segment.getJSONObject("departure").put("airportCommonName",deptAirportName);

                    String arrIATA = segment.getJSONObject("arrival").getString("iataCode");
                    String arrAirportName = airportCodeService.airportSearchByKeyword(arrIATA);
                    segment.getJSONObject("arrival").put("airportCommonName",arrAirportName);

                    String carrierCode = segment.getString("carierCode");
                    String airlineName = airlineInformationService.airlineCodeLookUp(carrierCode);
                    segment.put("airlineCommonName",airlineName);

                }
            }
        }

        return jsonResponse.toString();
    }
}
