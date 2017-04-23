package com.earthhacks.hacks;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;


import org.jpaste.pastebin.account.PastebinAccount;

import java.util.concurrent.TimeUnit;

public class ResultsActivity extends FragmentActivity   {

    final PastebinAccount account = new PastebinAccount("0c74dc3b025e92ece303a8d5ece9a0b9", "alipervaiz3", "M7kLm49M");
    String urlpost = "http://pastebin.com/api/api_post.php";
    String accountVerification = "5b0c7820f90de35c8ef6a361a697edf2";
    String pasteKey = "";
    String data = "";
    int[] amounts = new int[16];
    String[] symptoms = {"Sinus Pressure", "Fatigue","Lightheadedness","Drowsiness","Red Eyes","Eye Irritation","Headache",
                    "Fever","Vomiting","Sneezing","Coughing","Dizziness","Nausea","Chills","Runny Nose","Congestion"};

    private static final int NUMBER_OF_PAGES = 2;
    private ViewPager vp;
    private PagerAdapter pa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // Instantiate a ViewPager and a PagerAdapter.
        vp = (ViewPager) findViewById(R.id.pager);
        pa = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        vp.setAdapter(pa);

    }

    @Override
    public void onBackPressed() {
        if (vp.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            vp.setCurrentItem(vp.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new PieFragment();
            } else if (position == 1) {
                return new ScreenSlidePageFragment();
            }
            else{
                return null;
        }
    }

        @Override
        public int getCount() {
            return NUMBER_OF_PAGES;
        }
        // Returns the fragment to display for that page
    }
}
