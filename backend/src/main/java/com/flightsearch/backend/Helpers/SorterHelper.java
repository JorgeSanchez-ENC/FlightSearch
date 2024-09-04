package com.flightsearch.backend.Helpers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class SorterHelper {
    public static JSONArray sort(JSONArray array, String key1, String key2){
        List<JSONObject> list = new ArrayList<>();


        for(int i = 0; i<array.length(); i++){
            list.add(array.getJSONObject(i));
        }

        list.sort((o1, o2) -> {
            int firstComp = o1.getString(key1).compareTo(o2.getString(key1));
            if (key2 == null || firstComp != 0) {
                return firstComp;
            } else {
                return o1.getString(key2).compareTo(o2.getString(key2));
            }
        });
        return new JSONArray(list);
    }

}
