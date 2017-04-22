package com.earthhacks.hacks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class SubmissionActivity extends AppCompatActivity {

    //@Override
    ArrayList<String> symptoms = new ArrayList<>();
    String data = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);
    }

    public void selectItem(View v){
        data = "";
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.checkbox_layout);
        int children = linearLayout.getChildCount();
        for(int i = 0;i<children;i++){
            CheckBox c = (CheckBox)linearLayout.getChildAt(i);
            if(c.isChecked())
                data+="1";
            else
                data+="0";
        }
        Log.d("TEST",data);
    }
    public void processResults(View v){

    }
}
