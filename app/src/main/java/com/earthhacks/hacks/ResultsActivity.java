package com.earthhacks.hacks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.jpaste.pastebin.account.PastebinAccount;

import java.util.HashMap;
import java.util.Map;

public class ResultsActivity extends AppCompatActivity {

    final PastebinAccount account = new PastebinAccount("f1f7b3194887c920b6ea2858ce6ac8d3", "alipervaiz", "earthhacks");
    String urlaccount = "http://pastebin.com/api/api_login.php";
    String urlpost = "http://pastebin.com/api/api_post.php";
    String accountVerification = "";
    String pasteKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        // Gets user key
        StringRequest request = new StringRequest(Request.Method.POST, urlaccount,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //setNewString(response);
                        Log.d("OUTPUT", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.toString());
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("api_dev_key", account.getDeveloperKey());
                params.put("api_user_name", account.getUsername());
                params.put("api_user_password", account.getPassword());
                return params;
            }
        };
        ApplicationController.getInstance().addToRequestQueue(request);
    }
    /*void getDatafromPaste(){
        StringRequest paste = new StringRequest(Request.Method.GET, "https://pastebin.com/raw/" + pasteKey,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("OUTPUT", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("OUTPUT", error.toString());
            }
        }
        );
        ApplicationController.getInstance().addToRequestQueue(paste);
    }

    void getKeyFromData(String result){
        int index = result.indexOf("<paste_key>") + "<paste_key>".length();
        String s = "";
        while(result.charAt(index) != '<'){
            s+=result.charAt(index++);
        }
        pasteKey = s;
        Log.d("TEST",pasteKey);
    }

    // Run this when you want to update
    void getStuff() {
        StringRequest pasteRequest = new StringRequest(Request.Method.POST, urlpost,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DATA", accountVerification);
                        Log.d("DATA", response);
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
    void setNewString(String response) {
        accountVerification = response;
    }*/
}
