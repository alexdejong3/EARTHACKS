package com.earthhacks.hacks;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Scanner;

public class ScreenSlidePageFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap map;
    String data;
    String posturl = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    String[] symptoms = {"Sinus Pressure", "Fatigue","Lightheadedness","Drowsiness","Red Eyes","Eye Irritation","Headache",
            "Fever","Vomiting","Sneezing","Coughing","Dizziness","Nausea","Chills","Runny Nose","Congestion"};

    public ScreenSlidePageFragment() {
        // Required empty public constructor
    }
    public static int counter = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.layout_map, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        counter = 0;
        super.onResume();
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map_view);
        mapFragment.getMapAsync(this);
        data = Data.getData();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        final HashMap<String,String> zips = getZipCodes();
        final String[] zipcodes = new String[zips.size()];
        int count = 0;
        for(String s:zips.keySet()){
            zipcodes[count] = s;
            count++;
        }
        for(int i = 0;i<zips.size();i++){
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, posturl + zipcodes[i], null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray array = response.getJSONArray("results");
                                JSONObject results = array.getJSONObject(0);
                                JSONObject geometry = results.getJSONObject("geometry");
                                JSONObject location = geometry.getJSONObject("location");
                                double lat = location.getDouble("lat");
                                double lon = location.getDouble("lng");
                                int i = ScreenSlidePageFragment.counter;
                                map.addMarker(new MarkerOptions().position(new LatLng(lat,lon)).title(zips.get(zipcodes[i])));
                                ScreenSlidePageFragment.counter++;
                            }catch (Exception e){Log.e("ERROR",e.getMessage());};
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
            ApplicationController.getInstance().addToRequestQueue(request);
        }

    }

    HashMap<String, String> getZipCodes(){

        Scanner scan = new Scanner(data);
        HashMap<String, String> zips = new HashMap<>();
        while(scan.hasNextLine()){
            String test = scan.nextLine();
            if(test.equals("end"))
                break;
            String s = "";
            for(int i = 0; i<symptoms.length;i++){
                Log.e("D",test);
                if(test.charAt(i)=='1')
                    s+=symptoms[i] + ", ";
            }
            s = s.substring(0,s.length()-2);
            String zipcode = scan.nextLine();
            if(zips.containsKey(zipcode))
                zips.put(zipcode, zips.get(zipcode) + s);
            else{
                zips.put(zipcode, s);
            }
            scan.nextLine();

        }
        return zips;
    }

}
