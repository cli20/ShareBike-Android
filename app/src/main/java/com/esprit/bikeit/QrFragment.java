package com.esprit.bikeit;


import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.esprit.bikeit.Model.Defi;
import com.esprit.bikeit.Model.Station;
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

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 */
public class QrFragment extends Fragment {
    private static String TAG = MainActivity.class.getSimpleName();
    Button scanBtn;
    TextView formatTxt,contentTxt;
    public QrFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qr, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checkConnection();
       RelativeLayout constraintLayout = (RelativeLayout) getView().findViewById(R.id.r1);

        Drawable background = constraintLayout.getBackground();
        background.setAlpha(80);

       scanBtn = (Button)getView().findViewById(R.id.scan_button);

      formatTxt = (TextView)getView().findViewById(R.id.scan_format);

        contentTxt = (TextView)getView().findViewById(R.id.scan_content);

        String urlJsonArry3 = "http://"+NavActivity.url+"/locations";
        JsonArrayRequest req3 = new JsonArrayRequest(urlJsonArry3,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            final List<String> ls = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject app = (JSONObject) response.get(i);
                                int id=app.getInt("id");
                                final String place=app.getString("description");
                                final double longitude=app.getDouble("longitude");
                                final double latitude=app.getDouble("latitude");
                                Station s=new Station(place,0);
                                s.setLatitude(latitude);
                                s.setLongitude(longitude);
                                ls.add(place);
                            }

                            Spinner stt=(Spinner) getView().findViewById(R.id.st);



                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                                    android.R.layout.simple_spinner_item, ls);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            stt.setAdapter(adapter);
                        }
                        catch (JSONException e){
                            Toasty.error(getActivity(), "error: "+e.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(req3);

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator scanIntegrator=new IntentIntegrator(getActivity());
                scanIntegrator.initiateScan();
             //  startActivityForResult(getActivity().getIntent(),getTargetRequestCode());
              /*  Intent intent = new Intent(getContext(), NavActivity.class);
                startActivityForResult(intent, 12345);*/
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("activityresult");

        IntentResult scanningResult=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(scanningResult!=null)
        {
            String scanContent=scanningResult.getContents();
            String scanFormat=scanningResult.getFormatName();
         //   formatTxt.setText("Format : "+scanFormat);
            contentTxt.setText("Content : "+scanContent);
           // String nomVelo=scanContent.replace(" ","%20");

            Button btnNext=(Button)getView().findViewById(R.id.button2);
            btnNext.setEnabled(true);
            final String urlJsonArry = "http://"+NavActivity.url+"/api/vs/"+scanContent;
            JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d(TAG, response.toString());
                            try {
                               final Station sta = new Station();
                                JSONObject app = (JSONObject) response.get(0);
                                int id=app.getInt("id");
                                String name=app.getString("description");
                               final int idV=app.getInt("v_id");
                                System.out.println("velo id :"+idV);
                                Double lat=app.getDouble("latitude");
                                Double lng=app.getDouble("longitude");
                                String et=app.getString("etat");

                                sta.setNom(name);
                                sta.setId(id);
                                sta.setLatitude(lat);
                                sta.setLongitude(lng);
                                if(et.equals("unlocked"))
                                {
                                    Toasty.error(getActivity(), "Bike not available", Toast.LENGTH_SHORT, true).show();
                                }
                                else{
                                    Button btnNext=(Button)getView().findViewById(R.id.button2);
                                    btnNext.setActivated(true);
                                    btnNext.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            System.out.println("click");
                                            Spinner ed1 = (Spinner) getView().findViewById(R.id.st);
                                            String st2 = ed1.getSelectedItem().toString();
                                            if (st2.equals(sta.getNom())) {
                                                Toasty.error(getActivity(), "Enter a diffrent station", Toast.LENGTH_SHORT, true).show();
                                            } else {
                                                final String urlJsonArry2 = "http://" + NavActivity.url + "/api/st/" + st2;
                                                JsonArrayRequest req2 = new JsonArrayRequest(urlJsonArry2,
                                                        new Response.Listener<JSONArray>() {
                                                            @Override
                                                            public void onResponse(JSONArray response) {
                                                                System.out.println("14");
                                                                Log.d(TAG, response.toString());
                                                                try {
                                                                    Station sta2 = new Station();
                                                                    JSONObject app = (JSONObject) response.get(0);
                                                                    int id = app.getInt("id");
                                                                    String name = app.getString("description");
                                                                    Double lat = app.getDouble("latitude");
                                                                    Double lng = app.getDouble("longitude");
                                                                    sta2.setNom(name);
                                                                    sta2.setId(id);
                                                                    sta2.setLatitude(lat);
                                                                    sta2.setLongitude(lng);

                                                                    RentFragment fragment = new RentFragment();
                                                                    Bundle bundle = new Bundle();
                                                                    System.out.println("velo idd :" + idV);
                                                                    bundle.putInt("idV", idV);
                                                                    bundle.putString("st1", sta.getNom());
                                                                    bundle.putString("st2", sta2.getNom());
                                                                    bundle.putDouble("st1lng", sta.getLongitude());
                                                                    bundle.putDouble("st1lat", sta.getLatitude());
                                                                    bundle.putDouble("st2lng", sta2.getLongitude());
                                                                    bundle.putDouble("st2lat", sta2.getLatitude());
                                                                    fragment.setArguments(bundle);

                                                                    getActivity().getFragmentManager().beginTransaction()
                                                                            .replace(R.id.fr1, fragment).addToBackStack(null).commit();

                                                                } catch (JSONException e) {
                                                                    Toasty.error(getActivity(), e.getMessage(), Toast.LENGTH_SHORT, true).show();

                                                                }

                                                            }

                                                        }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        VolleyLog.d(TAG, "Error: " + error.getMessage());


                                                    }
                                                });
                                                AppController.getInstance().addToRequestQueue(req2);
                                            }


                                        }
                                    });

                                }

                            }
                            catch (JSONException e){
                                Toasty.error(getActivity(), e.getMessage(), Toast.LENGTH_SHORT, true).show();

                            }

                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                                              /*  Toast.makeText(getActivity().getApplicationContext(),
                                                        error.getMessage(), Toast.LENGTH_SHORT).show();*/

              }
            });
            AppController.getInstance().addToRequestQueue(req);

        }
        else{
            Toasty.error(getActivity(), "Aucune données de scan recu", Toast.LENGTH_SHORT, true).show();
        }
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    public   void checkConnection(){
        if(isOnline()){

        }else{
            Toasty.error(getActivity(), "check your internet connection", Toast.LENGTH_SHORT, true).show();
        }
    }
}
