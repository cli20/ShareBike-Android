package com.esprit.bikeit;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.esprit.bikeit.Model.Defi;
import com.esprit.bikeit.Model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 */
public class RentFragment extends Fragment {

    private static String TAG = MainActivity.class.getSimpleName();


    public RentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rent, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkConnection();
        TextView ed1 = (TextView) getView().findViewById(R.id.tvSt1);
        TextView ed2 = (TextView) getView().findViewById(R.id.tvSt2);

        Bundle bundle = this.getArguments();

        String st1 = bundle.getString("st1", "");
        String st2 = bundle.getString("st2", "");
        Double lng1 = bundle.getDouble("st1lng", 0);
        Double lat1 = bundle.getDouble("st1lat", 0);
        Double lng2 = bundle.getDouble("st2lng", 0);
        Double lat2 = bundle.getDouble("st2lat", 0);
        final int idV=bundle.getInt("idV", 0);

        ed1.setText(st1);
        ed2.setText(st2);

       final double free=2;
        //  final String urlJsonArry = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="+lng1+","+lat1+"&destinations="+lng2+","+lat2+"&sensor=false&units=metric&key=AIzaSyA2D7CUdXrcdDOmHvO6Xv_NV-nxT__qhtw";
        final String urlJsonArry = "http://"+NavActivity.url+"/directions/" + lat1 + "/" + lng1 + "/" + lat2 + "/" + lng2;
        // final String urlJsonArry="http://192.168.43.203:8000/api/users";
        System.out.println(urlJsonArry);


        JsonObjectRequest req = new JsonObjectRequest(urlJsonArry, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {


                    JSONArray rows = response.getJSONArray("rows");
                    JSONObject ele1 = rows.getJSONObject(0);
                    JSONArray ele = ele1.getJSONArray("elements");
                    JSONObject obj = ele.getJSONObject(0);
                    JSONObject obj1 = obj.getJSONObject("distance");

                    final int value = obj1.getInt("value");
                    System.out.println(value);

                    final TextView tvRent = (TextView) getView().findViewById(R.id.tvRentHours);
                    final TextView tvPrix = (TextView) getView().findViewById(R.id.tvPrix);
                    final TextView tvFree = (TextView) getView().findViewById(R.id.tvF);

                   /* SharedPreferences shared = getActivity().getSharedPreferences("pref2", Context.MODE_PRIVATE);
                    double free=shared.getFloat("free", 0f);*/

                   ////////////////////
                    SharedPreferences shared = getActivity().getSharedPreferences("pref2", Context.MODE_PRIVATE);
                    int idUser=shared.getInt("id_user",0);
                    String urlJsonArry5 = "http://"+NavActivity.url+"/api/fre/"+idUser;

                    JsonArrayRequest req5 = new JsonArrayRequest(urlJsonArry5,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    Log.d(TAG, response.toString());
                                    try {

                                            JSONObject app = (JSONObject) response.get(0);

                                           // free =app.getDouble("freeHours");





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
                    AppController.getInstance().addToRequestQueue(req5);


                    /////////////////////
                    tvFree.setText(String.valueOf(free)+ "H");
                    double nb=value/8000.0;
                   final double nbHeure = Math.ceil(nb) /2;
                    final double prix;
                   final double fr;

                    if(free >nbHeure)
                    {
                        prix=0;
                        fr= free -nbHeure;
                    }
                    else{
                        prix=(nbHeure- free)*1.5;
                        fr=0;
                    }


                    tvRent.setText(String.valueOf(nbHeure)+" H");
                    tvPrix.setText("Price : "+String.valueOf(prix)+" DT");

                    Button btnPay=(Button) getView().findViewById(R.id.btn_payment);
                    btnPay.setEnabled(true);
                    btnPay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences shared = getActivity().getSharedPreferences("pref2", Context.MODE_PRIVATE);
                            int idUser=shared.getInt("id_user",0);
                            SharedPreferences.Editor editor = shared.edit();
                            editor.putFloat("free", (float)fr);
                            editor.commit();
                            System.out.println("velo iddd :"+idV);
                            System.out.println("user id :"+idUser);
                            System.out.println("fr:"+fr);
                            double dt=(double)value/1000;

                            final String urlJsonArry = "http://"+NavActivity.url+"/api/pay/"+idUser+"/"+prix+"/"+idV+"/"+nbHeure+"/"+fr+"/"+dt;
                            System.out.println(urlJsonArry);

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

                            Toasty.success(getActivity(), "your payment is successful!", Toast.LENGTH_SHORT, true).show();
                            VeloFragment fragment = new VeloFragment();
                            getActivity().getFragmentManager().beginTransaction()
                                    .replace(R.id.fr1, fragment).addToBackStack(null).commit();
                        }
                    });



                } catch (JSONException e) {
                    Toasty.error(getActivity(), "error: "+e.getMessage(), Toast.LENGTH_SHORT, true).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        AppController.getInstance().addToRequestQueue(req);




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
