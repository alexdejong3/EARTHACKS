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

import org.jpaste.exceptions.PasteException;
import org.jpaste.pastebin.account.PastebinAccount;
import org.jpaste.pastebin.exceptions.LoginException;
import org.jpaste.pastebin.exceptions.ParseException;

import java.util.HashMap;
import java.util.Map;

public class SubmissionActivity extends AppCompatActivity {

    //@Override
    String data = "";
    String urlaccount = "http://pastebin.com/api/api_login.php";
    String urlpaste = "http://pastebin.com/api/api_post.php";
    String accountVerification = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);
        PastebinAccount account = new PastebinAccount("f1f7b3194887c920b6ea2858ce6ac8d3", "alipervaiz", "earthhacks");
        StringRequest request = new StringRequest(Request.Method.POST, urlaccount,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        setNewString(response);
                        Log.d("FUCCKER",response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("FUCKER", error.toString());
            }
        }
        ){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("api_dev_key", "f1f7b3194887c920b6ea2858ce6ac8d3");
                //params.put("api_option","paste");
                //params.put("api_paste_code","FUCK YOU");
                params.put("api_user_name","alipervaiz");
                params.put("api_user_password","earthhacks");

                return params;
            }
        };
        ApplicationController.getInstance().addToRequestQueue(request);
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

    public void processResults(View v) throws PasteException, LoginException,ParseException {
        EditText editText = (EditText) findViewById(R.id.edit_text_zipcode);
        String s = editText.getText().toString();
        if(isZipcode(s)){

            StringRequest paste = new StringRequest(Request.Method.POST, urlpaste,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("FUCKER", response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("FUCKER", error.toString());
                }
            }
            ){
                @Override
                protected Map<String,String> getParams(){
                    Map<String, String> params = new HashMap<>();
                    params.put("api_dev_key", "f1f7b3194887c920b6ea2858ce6ac8d3");
                    params.put("api_option","paste");
                    params.put("api_paste_code","FUCK YOU");
                    params.put("api_user_key", accountVerification);
                    //params.put("api_user_name","alipervaiz");
                    //params.put("api_user_password","earthhacks");

                    return params;
                }
            };
            ApplicationController.getInstance().addToRequestQueue(paste);

            /*PastebinLink[] pastes = account.getPastes();
            PastebinLink recent = pastes[0];
            recent.fetchContent();
            contents = recent.getPaste().getContents();
            //edit this
            String bits = "";
            String zip = "";
            // create paste
            PastebinPaste paste = new PastebinPaste(account);
            paste.setContents(bits + "\n" + zip + "\n" + contents);
            paste.setPasteTitle("private info");

            // push paste
            PastebinLink link = paste.paste();
            System.out.println(link.getLink());

            //Deleting old data
            for(int i = 0; i < pastes.length; i++)
            {
                PastebinLink bin = pastes[i];
                bin.delete();
            }*/

            finish();
        }
        else {
            editText.setText("");
            Toast.makeText(this, "Invalid Zipcode", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isZipcode(String s){
        return s.matches("[0-9]{5}");
    }

    void setNewString(String response){
        accountVerification = response;
    }
}
