package com.earthhacks.hacks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.jpaste.exceptions.PasteException;
import org.jpaste.pastebin.PastebinLink;
import org.jpaste.pastebin.account.PastebinAccount;
import org.jpaste.pastebin.exceptions.LoginException;
import org.jpaste.pastebin.exceptions.ParseException;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class SubmissionActivity extends AppCompatActivity {

    String data = "";
    String urlaccount = "http://pastebin.com/api/api_login.php";
    String urlpost = "http://pastebin.com/api/api_post.php";
    String accountVerification = "";
    String pasteKey = "";
    String zipcode = "00000";
    String timeStamp = "";
    final PastebinAccount account = new PastebinAccount("f1f7b3194887c920b6ea2858ce6ac8d3", "alipervaiz", "earthhacks");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);

        // Gets user key
        StringRequest request = new StringRequest(Request.Method.POST, urlaccount,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        setNewString(response);
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

    public void selectItem(View v) {
        data = "";
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.checkbox_layout);
        int children = linearLayout.getChildCount();
        for (int i = 0; i < children; i++) {
            CheckBox c = (CheckBox) linearLayout.getChildAt(i);
            if (c.isChecked())
                data += "1";
            else
                data += "0";
        }
        Log.d("TEST", data);
    }

    public void processResults(View v) throws PasteException, LoginException, ParseException {
        EditText editText = (EditText) findViewById(R.id.edit_text_zipcode);
        String s = editText.getText().toString();
        timeStamp = new SimpleDateFormat("MM.dd.yyyy").format(new java.util.Date());
        if (isZipcode(s)) {
            zipcode = s;

            // Gets the paste data
            StringRequest pasteRequest = new StringRequest(Request.Method.POST, urlpost,
                    new Response.Listener<String>() {
                            @Override
                        public void onResponse(String response) {
                            Log.d("DATA", accountVerification);
                            Log.d("DATA",response);
                                getKeyFromData(response);
                                getDatafromPaste();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ERROR",error.toString());
                }
            }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("api_dev_key", account.getDeveloperKey());
                    params.put("api_option", "list");
                    params.put("api_results_limit","1");
                    params.put("api_user_key", accountVerification);

                    return params;
                }
            };
            ApplicationController.getInstance().addToRequestQueue(pasteRequest);
            Toast.makeText(this, "Thank you for your input", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            editText.setText("");
            Toast.makeText(this, "Invalid Zipcode", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isZipcode(String s) {
        return s.matches("[0-9]{5}");
    }

    void setNewString(String response) {
        accountVerification = response;
    }

    void pasteToServer(final String pasteData){
        StringRequest paste = new StringRequest(Request.Method.POST, urlpost,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("OUTPUT", response);
                        deletePaste();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("OUTPUT", error.toString());
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("api_dev_key", account.getDeveloperKey());
                params.put("api_option", "paste");
                params.put("api_paste_code", pasteData);
                params.put("api_user_key", accountVerification);

                return params;
            }
        };
        ApplicationController.getInstance().addToRequestQueue(paste);
    }

    void getDatafromPaste(){
        StringRequest paste = new StringRequest(Request.Method.GET, "https://pastebin.com/raw/" + pasteKey,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("OUTPUT", response);
                        pasteToServer(getNewData(response));

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
        while(index < result.length() && result.charAt(index) != '<'){
            s+=result.charAt(index++);
        }
        pasteKey = s;
        Log.d("TEST",pasteKey);
    }
    String getNewData(String response){
        return makePasteEntry() + response;
    }

    String makePasteEntry(){
        return data + "\n" + zipcode + "\n" + timeStamp + "\n";
    }

    void deletePaste(){
        StringRequest paste = new StringRequest(Request.Method.POST, urlpost,
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
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("api_dev_key", account.getDeveloperKey());
                params.put("api_option", "delete");
                params.put("api_user_key", accountVerification);
                params.put("api_paste_key",pasteKey);

                return params;
            }
        };
        ApplicationController.getInstance().addToRequestQueue(paste);

    }
}