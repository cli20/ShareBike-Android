package com.esprit.bikeit;


import android.animation.Animator;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.esprit.bikeit.Model.Defi;


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
public class ListFragment extends Fragment {
    private static String TAG = MainActivity.class.getSimpleName();

    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkConnection();
        getActivity().setTitle("Challenges");


        SharedPreferences shared = getActivity().getSharedPreferences("pref2", Context.MODE_PRIVATE);
        int idUser=shared.getInt("id_user",0);

        final String urlJsonArry = "http://"+NavActivity.url+"/api/def/"+idUser;

        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            List<Defi> ld = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject app = (JSONObject) response.get(i);

                                int id=app.getInt("idDefis");
                                String name=app.getString("nom");
                                String desc=app.getString("description");
                                String etat=app.getString("et");
                                double distance;
                                try{
                                    distance =app.getDouble("dt");}
                                catch (Exception e){
                                    distance=0;
                                }

                                String price=app.getString("price");
                            //    double distance=app.getDouble("distance_traveled");
                                String string = app.getString("created_at");
                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                Date created = format.parse(string);
                                int ins=app.getInt("ins");



                                Defi d=new Defi(id,name,desc,price,created);
                                d.setEtat(etat);
                                if(ins==0)
                                    d.setSubed(false);
                                else
                                    d.setSubed(true);
                                d.setDistance(distance);
                             //   d.setDistance(distance);
                                if(!(d.getEtat().equals("disabled")))
                                ld.add(d);
                            }

                            ListView lv=(ListView)getView().findViewById(R.id.listT);

                            CustomAdapter adapter= new CustomAdapter(getActivity(),R.layout.list_item,ld);

                            lv.setAdapter(adapter);
                        }
                        catch (JSONException e){
                            Toasty.error(getActivity(), e.getMessage(), Toast.LENGTH_SHORT, true).show();

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());


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
