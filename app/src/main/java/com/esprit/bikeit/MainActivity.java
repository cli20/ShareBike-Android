package com.esprit.bikeit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.esprit.bikeit.Model.Defi;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = MainActivity.class.getSimpleName();
    Button scanBtn;
    TextView formatTxt,contentTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // ListFragment firstFragment = new ListFragment();
       /* QrFragment firstFragment = new QrFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fr1, firstFragment).commit();*/

                scanBtn = (Button)findViewById(R.id.scan_button);

        formatTxt = (TextView)findViewById(R.id.scan_format);

        contentTxt = (TextView)findViewById(R.id.scan_content);

        scanBtn.setOnClickListener(this);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult scanningResult=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(scanningResult!=null)
        {
            String scanContent=scanningResult.getContents();
            String scanFormat=scanningResult.getFormatName();
            formatTxt.setText("Format : "+scanFormat);
            contentTxt.setText("Content : "+scanContent);

            final String urlJsonArry = "http://"+NavActivity.url+"/api/velo/"+scanContent.replaceAll(" ","%20");
            System.out.println("array: "+urlJsonArry);
            JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d(TAG, response.toString());



                            }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });
            AppController.getInstance().addToRequestQueue(req);

            Toast.makeText(getApplicationContext()," scan complete",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"Aucune données de scan recu",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
if(v.getId()==R.id.scan_button)
{
    IntentIntegrator scanIntegrator=new IntentIntegrator(this);
    scanIntegrator.initiateScan();
}
    }
}

