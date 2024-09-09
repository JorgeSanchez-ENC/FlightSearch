package com.flightsearch.backend.Services;

import com.flightsearch.backend.Helpers.SorterHelper;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    List<JSONObject> flightOffers;

    @Autowired
    AccessTokenService accessTokenService;

    @Autowired
    AirportCodeService airportCodeService;

    @Autowired
    AirlineInformationService airlineInformationService;

    OkHttpClient client = new OkHttpClient();


    public SearchService() throws IOException {
    }

    public List<JSONObject> flightOfferSearch(String originLocationCode, String destinationLocationCode,
                                              String departureDate, String returnDate, Integer adults, String currencyCode, Boolean nonStop
    ) throws IOException {
        flightOffers = new ArrayList<>();
        String token = accessTokenService.getAccessToken();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://test.api.amadeus.com/v2/shopping/flight-offers").newBuilder()
                .addQueryParameter("originLocationCode",originLocationCode)
                .addQueryParameter("destinationLocationCode",destinationLocationCode)
                .addQueryParameter("departureDate",departureDate)
                .addQueryParameter("adults",adults.toString())
                .addQueryParameter("currencyCode", currencyCode )
                .addQueryParameter("nonStop", nonStop.toString())
                .addQueryParameter("max","5");
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
            JSONObject modifiedJson = addAirportAndAirlinesCommonNames(response.body().string());
            JSONArray data = modifiedJson.getJSONArray("data");
            for(int i = 0; i<data.length(); i++){
                flightOffers.add(data.getJSONObject(i));
            }
            return flightOffers;
        }


    }

    private JSONObject addAirportAndAirlinesCommonNames(String originalJson) throws IOException {
        JSONObject jsonResponse =new JSONObject(originalJson);
        JSONArray dataArray = jsonResponse.getJSONArray("data");

        for(int i = 0; i < dataArray.length(); i++){
            JSONObject flightOffer = dataArray.getJSONObject(i);
            JSONArray itineraries = flightOffer.getJSONArray("itineraries");
            Duration totalDuration = Duration.ZERO;
            for(int j = 0; j < itineraries.length(); j++){
                JSONObject itinerary = itineraries.getJSONObject(j);
                JSONArray segments = itinerary.getJSONArray("segments");
                Duration duration = Duration.parse(itinerary.getString("duration"));
                totalDuration  = totalDuration.plus(duration);
                for(int k = 0; k < segments.length(); k++){
                    JSONObject segment = segments.getJSONObject(k);

                    String deptIATA = segment.getJSONObject("departure").getString("iataCode");
                    String deptAirportName = airportCodeService.airportNameSearchByKeyword(deptIATA);
                    segment.getJSONObject("departure").put("airportCommonName",deptAirportName);

                    String arrIATA = segment.getJSONObject("arrival").getString("iataCode");
                    String arrAirportName = airportCodeService.airportNameSearchByKeyword(arrIATA);
                    segment.getJSONObject("arrival").put("airportCommonName",arrAirportName);

                    String carrierCode = segment.getString("carrierCode");
                    String airlineName = airlineInformationService.airlineNameLookUp(carrierCode);
                    segment.put("airlineCommonName",airlineName);
                }
            }
            flightOffer.put("totalDuration",totalDuration.toString());
            flightOffer.put("totalPrice",flightOffer.getJSONObject("price").getString("total"));
        }

        return jsonResponse;
    }

    public List<JSONObject> sort(String mode){

        switch (mode) {
            case "price" -> SorterHelper.sort(this.flightOffers, "totalPrice", null);
            case "duration" -> SorterHelper.sort(this.flightOffers, "totalDuration", null);
            case "duration-price" -> SorterHelper.sort(this.flightOffers, "totalDuration", "totalPrice");
            case "price-duration" -> SorterHelper.sort(this.flightOffers, "totalPrice", "totalDuration");
        };

        return this.flightOffers;
    }
}
