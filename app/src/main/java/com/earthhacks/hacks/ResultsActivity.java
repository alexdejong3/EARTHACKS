package com.earthhacks.hacks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import org.jpaste.pastebin.account.PastebinAccount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ResultsActivity extends AppCompatActivity {

    final PastebinAccount account = new PastebinAccount("0c74dc3b025e92ece303a8d5ece9a0b9", "alipervaiz3", "M7kLm49M");
    String urlaccount = "http://pastebin.com/api/api_login.php";
    String urlpost = "http://pastebin.com/api/api_post.php";
    String accountVerification = "5b0c7820f90de35c8ef6a361a697edf2";
    String pasteKey = "";
    String data = "";
    int[] amounts = new int[16];
    String[] symptoms = {"Sinus Pressure", "Fatigue","Lightheadedness","Drowsiness","Red Eyes","Eye Irritation","Headache",
                    "Fever","Vomiting","Sneezing","Coughing","Dizziness","Nausea","Chills","Runny Nose","Congestion"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        refresh();


    }

    void getAuthentication(){
        StringRequest request = new StringRequest(Request.Method.POST, urlaccount,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("USER_KEY", response);
                        setNewString(response);
                        //getPaste();
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

    void getDatafromPaste(){
        StringRequest paste = new StringRequest(Request.Method.GET, "https://pastebin.com/raw/" + pasteKey,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FINAL_DATA",response);
                        setData(response);
                        compileData();
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

    void getKeyFromData(String result){
        int index = result.indexOf("<paste_key>") + "<paste_key>".length();
        String s = "";
        while(index < result.length() && result.charAt(index) != '<'){
            s+=result.charAt(index++);
        }
        pasteKey = s;
        Log.d("PASTE_KEY",pasteKey);
    }

    // Run this when you want to update
    void getPaste() {
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
    void setNewString(String response) {
        accountVerification = response;
    }

    void refresh(){
        data = "";
        getPaste();
    }

    void updatePieChart(){
        Log.e("TESTING", Arrays.toString(amounts));
        PieChart pieChart = (PieChart)findViewById(R.id.pie_chart);
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        int[] clone = amounts.clone();
        Arrays.sort(clone);
        int added = 0;
        int index = -1;
        while(added<5)
        {
            index++;
            for(int i = 0; i < amounts.length; i++)
            {
                if(amounts[i]==clone[clone.length-1-index])
                {
                    added++;
                    entries.add(new Entry( amounts[i], i));
                    labels.add(symptoms[i]);
                }
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "Amounts");
        dataSet.setSliceSpace(1);
        dataSet.setHighlightEnabled(false);
        PieData pieData = new PieData(labels, dataSet);
        pieData.setValueTextSize(10);
        pieChart.setData(pieData);
        pieChart.invalidate();
        pieChart.setTouchEnabled(false);
        pieChart.setUsePercentValues(true);
    }
    void compileData(){
        Scanner scan = new Scanner(data);
        Log.e("DATA_TEST",data);
        String sympts = "";
        while(scan.hasNextLine()){
            sympts = scan.nextLine();
            if(sympts.equals("end")){
                break;
            }
            for(int i = 0;i<sympts.length();i++) {
                amounts[i] += Integer.parseInt(sympts.substring(i, i + 1));
            }
            scan.nextLine();
            scan.nextLine();
        }
        updatePieChart();
    }

    void setData(String response){
        data = response;
    }
}
