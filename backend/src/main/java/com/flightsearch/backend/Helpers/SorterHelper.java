package com.flightsearch.backend.Helpers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.time.Duration;

public class SorterHelper {
    public static void sort(List<JSONObject> list, String key1, String key2){

        list.sort((o1, o2) -> {
            int c1 = 0;
            if(key1.equals("totalPrice")){
                c1 = Float.compare(o1.getFloat("totalPrice"), o2.getFloat("totalPrice") );
            }else if (key1.equals("totalDuration")){
                Duration d1 = Duration.parse(o1.getString("totalDuration"));
                Duration d2 = Duration.parse(o2.getString("totalDuration"));
                c1 = d1.compareTo(d2);
            }

            if(key2 == null || c1 != 0){
                return c1;
            }

            int c2 = 0;
            if(key2.equals("totalPrice")){
                c2 = Float.compare(o1.getFloat("totalPrice"), o2.getFloat("totalPrice") );
            }else if(key2.equals("totalDuration")){
                Duration d1 = Duration.parse(o1.getString("totalDuration"));
                Duration d2 = Duration.parse(o2.getString("totalDuration"));
                c2 = d1.compareTo(d2);
            }

            return c2;
        });
        for (JSONObject jsonObject : list) {
            System.out.println(jsonObject.getString("id"));
        }
    }

}
