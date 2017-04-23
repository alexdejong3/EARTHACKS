package com.earthhacks.hacks;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.jpaste.pastebin.account.PastebinAccount;

import java.util.HashMap;
import java.util.Map;

public class Data {
    static private String data = "";
    static final PastebinAccount account = new PastebinAccount("0c74dc3b025e92ece303a8d5ece9a0b9", "alipervaiz3", "M7kLm49M");
    static final String urlpost = "http://pastebin.com/api/api_post.php";
    static final String accountVerification = "5b0c7820f90de35c8ef6a361a697edf2";
    static String pasteKey = "";

    public static String getData() {
        return data;
    }

    public static void retrieveData(){
        getPaste();
    }

    static void getDatafromPaste(){
        StringRequest paste = new StringRequest(Request.Method.GET, "https://pastebin.com/raw/" + pasteKey,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FINAL_DATA",response);
                        setData(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.toString());
            }
        }
        );
        ApplicationController.getInstance().addToRequestQueue(paste);
    }

    static void getKeyFromData(String result){
        int index = result.indexOf("<paste_key>") + "<paste_key>".length();
        String s = "";
        while(index < result.length() && result.charAt(index) != '<'){
            s+=result.charAt(index++);
        }
        pasteKey = s;
        Log.d("PASTE_KEY",pasteKey);
    }

    static void getPaste() {
        StringRequest pasteRequest = new StringRequest(Request.Method.POST, urlpost,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("PASTE_DATA", response);
                        getKeyFromData(response);
                        getDatafromPaste();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("api_dev_key", account.getDeveloperKey());
                params.put("api_option", "list");
                params.put("api_results_limit", "1");
                params.put("api_user_key", accountVerification);

                return params;
            }
        };
        ApplicationController.getInstance().addToRequestQueue(pasteRequest);
    }

    static void setData(String response){
        data = response;
    }
}
