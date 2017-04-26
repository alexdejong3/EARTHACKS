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

    /*
    Credentials that allow access to pastebin.com, the temporary server for this application
     */
    private static final PastebinAccount ACCOUNT = new PastebinAccount("0c74dc3b025e92ece303a8d5ece9a0b9", "alipervaiz3", "M7kLm49M");
    private static final String urlpost = "http://pastebin.com/api/api_post.php";
    private static final String accountVerification = "5b0c7820f90de35c8ef6a361a697edf2";
    private static String pasteKey = "";

    /*
    Returns the data in bit string
                        zipcode
                        date/timestamp format
    else, returns an empty string
     */
    public static String getData() {
        return data;
    }

    /*
    returns the account that contains key, username, and password
     */
    public static PastebinAccount getAccount(){
        return ACCOUNT;
    }

    /*
    Returns unexpiring key
     */
    public static String getUserSessionKey(){
        return accountVerification;
    }

    /*
    returns URL for posting to pastebin.com
     */
    public static String getPostURL(){
        return urlpost;
    }

    /*
    Returns the individual paste key used
     */
    public static String getPasteKey(){
        return pasteKey;
    }

    /*
    Calls to the server/pastebin website to get the data and save it
     */
    static void retrieveData() {
        getPaste();
    }

    /*
    Gets the first paste on the ACCOUNT
     */
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
                })
        {
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

    /*
    Parses paste data, returning the paste id used to get the actual content
     */
    private static String getKeyFromData(String result) {
        int index = result.indexOf("<paste_key>") + "<paste_key>".length();
        String s = "";
        while (index < result.length() && result.charAt(index) != '<') {
            s += result.charAt(index++);
        }
        Log.d("PASTE_KEY", s);
        pasteKey = s;
        return s;
    }

    /*
    Get the content of the paste to be used by other modules
     */
    private static void getDatafromPaste(String pasteKey) {
        StringRequest paste = new StringRequest(Request.Method.GET, "https://pastebin.com/raw/" + pasteKey,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FINAL_DATA", response);
                        setData(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", error.toString());
                    }
                });

        ApplicationController.getInstance().addToRequestQueue(paste);
    }

    /*
    Helper function to set the data from the interior getDatafromPaste function
     */
    static void setData(String response) {
        data = response;
    }
}
