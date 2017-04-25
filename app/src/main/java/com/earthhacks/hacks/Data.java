package com.earthhacks.hacks;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.jpaste.pastebin.account.PastebinAccount;

import java.util.HashMap;
import java.util.Map;

class Data {

    private static String data = "";
    private static final PastebinAccount ACCOUNT = new PastebinAccount("0c74dc3b025e92ece303a8d5ece9a0b9", "alipervaiz3", "M7kLm49M");
    private static final String urlpost = "http://pastebin.com/api/api_post.php";
    private static final String accountVerification = "5b0c7820f90de35c8ef6a361a697edf2";

    public static String getData() {
        return data;
    }

    static void retrieveData(){
        getPaste();
    }

    private static void getPaste() {
        StringRequest pasteRequest = new StringRequest(Request.Method.POST, urlpost,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("PASTE_DATA", response);
                        String pasteKey = getKeyFromData(response);
                        getDatafromPaste(pasteKey);
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
                params.put("api_dev_key", ACCOUNT.getDeveloperKey());
                params.put("api_option", "list");
                params.put("api_results_limit", "1");
                params.put("api_user_key", accountVerification);

                return params;
            }
        };
        ApplicationController.getInstance().addToRequestQueue(pasteRequest);
    }

    private static String getKeyFromData(String result){
        int index = result.indexOf("<paste_key>") + "<paste_key>".length();
        String s = "";
        while(index < result.length() && result.charAt(index) != '<'){
            s+=result.charAt(index++);
        }
        Log.d("PASTE_KEY",s);
        return s;
    }

    private static void getDatafromPaste(String pasteKey){
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

    static void setData(String response){
        data = response;
    }
}
