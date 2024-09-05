package com.flightsearch.backend.Services;

import com.flightsearch.backend.Helpers.SorterHelper;
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
import java.time.Duration;
import java.util.Map;

@Service
public class SearchService {
    @Autowired
    AccessTokenService accessTokenService;

    @Autowired
    AirportCodeService airportCodeService;

    @Autowired
    AirlineInformationService airlineInformationService;

    OkHttpClient client = new OkHttpClient();

    String mockResponse = "{\n" +
            "  \"meta\": {\n" +
            "    \"count\": 2,\n" +
            "    \"links\": {\n" +
            "      \"self\": \"https://test.api.amadeus.com/v2/shopping/flight-offers?originLocationCode=SYD&destinationLocationCode=BKK&departureDate=2021-11-01&adults=1&max=2\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"data\": [\n" +
            "    {\n" +
            "      \"type\": \"flight-offer\",\n" +
            "      \"id\": \"1\",\n" +
            "      \"source\": \"GDS\",\n" +
            "      \"instantTicketingRequired\": false,\n" +
            "      \"nonHomogeneous\": false,\n" +
            "      \"oneWay\": false,\n" +
            "      \"lastTicketingDate\": \"2021-11-01\",\n" +
            "      \"numberOfBookableSeats\": 9,\n" +
            "      \"totalDuration\": \"PT21H35M\",\n" +
            "      \"totalPrice\": \"362.34\",\n" +
            "      \"itineraries\": [\n" +
            "        {\n" +
            "          \"duration\": \"PT14H15M\",\n" +
            "          \"segments\": [\n" +
            "            {\n" +
            "              \"departure\": {\n" +
            "                \"iataCode\": \"SYD\",\n" +
            "                \"terminal\": \"1\",\n" +
            "                \"at\": \"2021-11-01T11:35:00\",\n" +
            "                \"airportCommonName\": \"Example name\"\n" +
            "              },\n" +
            "              \"arrival\": {\n" +
            "                \"iataCode\": \"MNL\",\n" +
            "                \"terminal\": \"2\",\n" +
            "                \"at\": \"2021-11-01T16:50:00\",\n" +
            "                \"airportCommonName\": \"Example name\"\n" +
            "              },\n" +
            "              \"carrierCode\": \"PR\",\n" +
            "              \"airlineCommonName\": \"Example name airline\",\n" +
            "              \"number\": \"212\",\n" +
            "              \"aircraft\": {\n" +
            "                \"code\": \"333\"\n" +
            "              },\n" +
            "              \"operating\": {\n" +
            "                \"carrierCode\": \"PR\"\n" +
            "              },\n" +
            "              \"duration\": \"PT8H15M\",\n" +
            "              \"id\": \"1\",\n" +
            "              \"numberOfStops\": 0,\n" +
            "              \"blacklistedInEU\": false\n" +
            "            },\n" +
            "            {\n" +
            "              \"departure\": {\n" +
            "                \"iataCode\": \"MNL\",\n" +
            "                \"terminal\": \"1\",\n" +
            "                \"at\": \"2021-11-01T19:20:00\",\n" +
            "                \"airportCommonName\": \"Example name\"\n" +
            "              },\n" +
            "              \"arrival\": {\n" +
            "                \"iataCode\": \"BKK\",\n" +
            "                \"at\": \"2021-11-01T21:50:00\",\n" +
            "                \"airportCommonName\": \"Example name\"\n" +
            "              },\n" +
            "              \"carrierCode\": \"PR\",\n" +
            "              \"airlineCommonName\": \"Example name airline\",\n" +
            "              \"number\": \"732\",\n" +
            "              \"aircraft\": {\n" +
            "                \"code\": \"320\"\n" +
            "              },\n" +
            "              \"operating\": {\n" +
            "                \"carrierCode\": \"PR\"\n" +
            "              },\n" +
            "              \"duration\": \"PT3H30M\",\n" +
            "              \"id\": \"2\",\n" +
            "              \"numberOfStops\": 0,\n" +
            "              \"blacklistedInEU\": false\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ],\n" +
            "      \"price\": {\n" +
            "        \"currency\": \"EUR\",\n" +
            "        \"total\": \"355.34\",\n" +
            "        \"base\": \"255.00\",\n" +
            "        \"fees\": [\n" +
            "          {\n" +
            "            \"amount\": \"0.00\",\n" +
            "            \"type\": \"SUPPLIER\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"amount\": \"0.00\",\n" +
            "            \"type\": \"TICKETING\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"grandTotal\": \"355.34\"\n" +
            "      },\n" +
            "      \"pricingOptions\": {\n" +
            "        \"fareType\": [\n" +
            "          \"PUBLISHED\"\n" +
            "        ],\n" +
            "        \"includedCheckedBagsOnly\": true\n" +
            "      },\n" +
            "      \"validatingAirlineCodes\": [\n" +
            "        \"PR\"\n" +
            "      ],\n" +
            "      \"travelerPricings\": [\n" +
            "        {\n" +
            "          \"travelerId\": \"1\",\n" +
            "          \"fareOption\": \"STANDARD\",\n" +
            "          \"travelerType\": \"ADULT\",\n" +
            "          \"price\": {\n" +
            "            \"currency\": \"EUR\",\n" +
            "            \"total\": \"355.34\",\n" +
            "            \"base\": \"255.00\"\n" +
            "          },\n" +
            "          \"fareDetailsBySegment\": [\n" +
            "            {\n" +
            "              \"segmentId\": \"1\",\n" +
            "              \"cabin\": \"ECONOMY\",\n" +
            "              \"fareBasis\": \"EOBAU\",\n" +
            "              \"class\": \"E\",\n" +
            "              \"includedCheckedBags\": {\n" +
            "                \"weight\": 25,\n" +
            "                \"weightUnit\": \"KG\"\n" +
            "              }\n" +
            "            },\n" +
            "            {\n" +
            "              \"segmentId\": \"2\",\n" +
            "              \"cabin\": \"ECONOMY\",\n" +
            "              \"fareBasis\": \"EOBAU\",\n" +
            "              \"class\": \"E\",\n" +
            "              \"includedCheckedBags\": {\n" +
            "                \"weight\": 25,\n" +
            "                \"weightUnit\": \"KG\"\n" +
            "              }\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"type\": \"flight-offer\",\n" +
            "      \"id\": \"2\",\n" +
            "      \"source\": \"GDS\",\n" +
            "      \"instantTicketingRequired\": false,\n" +
            "      \"nonHomogeneous\": false,\n" +
            "      \"oneWay\": false,\n" +
            "      \"lastTicketingDate\": \"2021-11-01\",\n" +
            "      \"numberOfBookableSeats\": 9,\n" +
            "      \"totalDuration\": \"PT20H35M\",\n" +
            "      \"totalPrice\": \"365.34\",\n" +
            "      \"itineraries\": [\n" +
            "        {\n" +
            "          \"duration\": \"PT16H35M\",\n" +
            "          \"segments\": [\n" +
            "            {\n" +
            "              \"departure\": {\n" +
            "                \"iataCode\": \"SYD\",\n" +
            "                \"terminal\": \"1\",\n" +
            "                \"at\": \"2021-11-01T11:35:00\",\n" +
            "                \"airportCommonName\": \"Example name\"\n" +
            "              },\n" +
            "              \"arrival\": {\n" +
            "                \"iataCode\": \"MNL\",\n" +
            "                \"terminal\": \"2\",\n" +
            "                \"at\": \"2021-11-01T16:50:00\",\n" +
            "                \"airportCommonName\": \"Example name\"\n" +
            "              },\n" +
            "              \"carrierCode\": \"PR\",\n" +
            "              \"airlineCommonName\": \"Example name airline\",\n" +
            "              \"number\": \"212\",\n" +
            "              \"aircraft\": {\n" +
            "                \"code\": \"333\"\n" +
            "              },\n" +
            "              \"operating\": {\n" +
            "                \"carrierCode\": \"PR\"\n" +
            "              },\n" +
            "              \"duration\": \"PT8H15M\",\n" +
            "              \"id\": \"3\",\n" +
            "              \"numberOfStops\": 0,\n" +
            "              \"blacklistedInEU\": false\n" +
            "            },\n" +
            "            {\n" +
            "              \"departure\": {\n" +
            "                \"iataCode\": \"MNL\",\n" +
            "                \"terminal\": \"1\",\n" +
            "                \"at\": \"2021-11-01T20:50:00\",\n" +
            "                \"airportCommonName\": \"Example name\"\n" +
            "              },\n" +
            "              \"arrival\": {\n" +
            "                \"iataCode\": \"BKK\",\n" +
            "                \"at\": \"2021-11-01T23:10:00\",\n" +
            "                \"airportCommonName\": \"Example name\"\n" +
            "              },\n" +
            "              \"carrierCode\": \"PR\",\n" +
            "              \"airlineCommonName\": \"Example name airline\",\n" +
            "              \"number\": \"738\",\n" +
            "              \"aircraft\": {\n" +
            "                \"code\": \"321\"\n" +
            "              },\n" +
            "              \"operating\": {\n" +
            "                \"carrierCode\": \"PR\"\n" +
            "              },\n" +
            "              \"duration\": \"PT3H20M\",\n" +
            "              \"id\": \"4\",\n" +
            "              \"numberOfStops\": 0,\n" +
            "              \"blacklistedInEU\": false\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ],\n" +
            "      \"price\": {\n" +
            "        \"currency\": \"EUR\",\n" +
            "        \"total\": \"365.34\",\n" +
            "        \"base\": \"265.00\",\n" +
            "        \"fees\": [\n" +
            "          {\n" +
            "            \"amount\": \"0.00\",\n" +
            "            \"type\": \"SUPPLIER\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"amount\": \"0.00\",\n" +
            "            \"type\": \"TICKETING\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"grandTotal\": \"365.34\"\n" +
            "      },\n" +
            "      \"pricingOptions\": {\n" +
            "        \"fareType\": [\n" +
            "          \"PUBLISHED\"\n" +
            "        ],\n" +
            "        \"includedCheckedBagsOnly\": true\n" +
            "      },\n" +
            "      \"validatingAirlineCodes\": [\n" +
            "        \"PR\"\n" +
            "      ],\n" +
            "      \"travelerPricings\": [\n" +
            "        {\n" +
            "          \"travelerId\": \"1\",\n" +
            "          \"fareOption\": \"STANDARD\",\n" +
            "          \"travelerType\": \"ADULT\",\n" +
            "          \"price\": {\n" +
            "            \"currency\": \"EUR\",\n" +
            "            \"total\": \"365.34\",\n" +
            "            \"base\": \"265.00\"\n" +
            "          },\n" +
            "          \"fareDetailsBySegment\": [\n" +
            "            {\n" +
            "              \"segmentId\": \"3\",\n" +
            "              \"cabin\": \"ECONOMY\",\n" +
            "              \"fareBasis\": \"EOBAU\",\n" +
            "              \"class\": \"E\",\n" +
            "              \"includedCheckedBags\": {\n" +
            "                \"weight\": 25,\n" +
            "                \"weightUnit\": \"KG\"\n" +
            "              }\n" +
            "            },\n" +
            "            {\n" +
            "              \"segmentId\": \"4\",\n" +
            "              \"cabin\": \"ECONOMY\",\n" +
            "              \"fareBasis\": \"EOBAU\",\n" +
            "              \"class\": \"E\",\n" +
            "              \"includedCheckedBags\": {\n" +
            "                \"weight\": 25,\n" +
            "                \"weightUnit\": \"KG\"\n" +
            "              }\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    public SearchService() throws IOException {
    }

    public String flightOfferSearch(String originLocationCode, String destinationLocationCode,
        String departureDate, String returnDate, Integer adults, String currencyCode, Boolean nonStop
    ) throws IOException {
        String token = accessTokenService.getAccessToken();
        System.out.println(originLocationCode);
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
            return addAirportAndAirlinesCommonNames(response.body().string());
        }


    }

    private String addAirportAndAirlinesCommonNames(String originalJson) throws IOException {
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

                    String carrierCode = segment.getJSONObject("operating").getString("carrierCode");
                    String airlineName = airlineInformationService.airlineNameLookUp(carrierCode);
                    segment.put("airlineCommonName",airlineName);
                }
            }
            flightOffer.put("totalDuration",totalDuration.toString());
            flightOffer.put("totalPrice",flightOffer.getJSONObject("price").getString("total"));
        }

        return jsonResponse.toString();
    }

    public JSONArray sort(String json, String mode){
        JSONArray array = new JSONArray(json);

        return switch (mode) {
            case "price" -> SorterHelper.sort(array, "totalPrice", null);
            case "duration" -> SorterHelper.sort(array, "totalDuration", null);
            case "duration-price" -> SorterHelper.sort(array, "totalDuration", "totalPrice");
            case "price-duration" -> SorterHelper.sort(array, "totalPrice", "totalDuration");
            default -> array;
        };
    }
}
