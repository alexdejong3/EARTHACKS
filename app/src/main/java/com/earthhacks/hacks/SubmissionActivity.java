package com.earthhacks.hacks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class SubmissionActivity extends AppCompatActivity {

    //@Override
    ArrayList<String> symptoms = new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);
    }

    public void selectItem(View v){
        boolean checked = ((CheckBox) v).isChecked();
        switch(v.getId()){
            case R.id.sy_fatigue:
                if(checked)
                    symptoms.add("Fatigue");
                else
                    symptoms.remove("Fatigue");
                break;
            case R.id.sy_lightheadedness:
                if(checked)
                    symptoms.add("Lightheadedness");
                else
                    symptoms.remove("Lightheadedness");
                break;
            case R.id.sy_drowsiness:
                if(checked)
                    symptoms.add("Drowsiness");
                else
                    symptoms.remove("Drowsiness");
                break;
            case R.id.sy_eye_irritation:
                if(checked)
                    symptoms.add("Eye Irritation");
                else
                    symptoms.remove("Eye Irritation");
                break;
            case R.id.sy_sinus_pressure:
                if(checked)
                    symptoms.add("Sinus Pressure");
                else
                    symptoms.remove("Sinus Pressure");
                break;
            case R.id.sy_red_eyes:
                if(checked)
                    symptoms.add("Red Eyes");
                else
                    symptoms.remove("Red Eyes");
                break;
        }
    }
    public void processResults(View v){

    }
}
