package com.earthhacks.hacks;

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

import org.jpaste.pastebin.account.PastebinAccount;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SubmissionActivity extends AppCompatActivity {

    private String data = "0000000000000000";
    private static final String urlpost = Data.getPostURL();
    private static final String accountVerification = Data.getUserSessionKey();
    private final PastebinAccount account = Data.getAccount();

    private String timeStamp = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /*
    Creates a bitstring based on if symptoms are checked or unchecked
    A 1 means you are experiencing that symptom, and a 0 means you aren't
     */
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

    public void processResults(View v) {
        EditText editText = (EditText) findViewById(R.id.edit_text_zipcode);
        String zipcode = editText.getText().toString();
        timeStamp = new SimpleDateFormat("MM.dd.yyyy", Locale.US).format(new java.util.Date());
        if (isZipcode(zipcode)) {
            if(data.contains("1")) {

                pasteToServer(getNewData(zipcode));
                Toast.makeText(this, "Thank you for your input", Toast.LENGTH_SHORT).show();
                finish();
            }
            else{
                Toast.makeText(this,"Please Enter Symptoms",Toast.LENGTH_SHORT).show();
            }
        } else {
            editText.setText("");
            Toast.makeText(this, "Invalid Zipcode", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isZipcode(String s) {
        return s.matches("[0-9]{5}");
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

    /*
    Returns the full data for the paste
     */
    String getNewData(String zipcode){
        return makePasteEntry(zipcode) + Data.getData();
    }

    /*
    Creates the new paste addition to the previous data
     */
    String makePasteEntry(String zipcode){
        return data + "\n" + zipcode + "\n" + timeStamp + "\n";
    }


    /*
    Deletes the previous paste after the new one has been created
     */
    void deletePaste(){
        StringRequest paste = new StringRequest(Request.Method.POST, urlpost,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DELETE_STATUS", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.toString());
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("api_dev_key", Data.getAccount().getDeveloperKey());
                params.put("api_option", "delete");
                params.put("api_user_key", Data.getUserSessionKey());
                params.put("api_paste_key",Data.getPasteKey());

                return params;
            }
        };
        ApplicationController.getInstance().addToRequestQueue(paste);

    }
}