package com.esprit.bikeit;


import android.app.FragmentTransaction;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import com.arsy.maps_library.MapRipple;
import com.esprit.bikeit.Model.Station;
import com.esprit.bikeit.Model.User;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 */
public class StationFragment extends Fragment {
    private static String TAG = NavActivity.class.getSimpleName();
public LatLng ll =null;
    public StationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_station, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkConnection();
        String urlJsonArry = "http://"+NavActivity.url+"/api/stv";

        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            final List<Station> ls = new ArrayList<>();


                            for (int i = 0; i < response.length(); i++) {
                                JSONObject app = (JSONObject) response.get(i);

                                int id=app.getInt("id");
                                final String place=app.getString("description");
                                final double longitude=app.getDouble("longitude");
                                final double latitude=app.getDouble("latitude");
                                int nb=app.getInt("nb");
                                int nbtt=app.getInt("nbtt");
                                System.out.println("long : "+longitude);

                                Station s=new Station(place,nb);
                                s.setNbVelo(nb);
                                s.setLatitude(latitude);
                                s.setLongitude(longitude);
                                s.setNbtt(nbtt);

                                ls.add(s);
                            }

                            ListView lv=(ListView)getView().findViewById(R.id.ListS);

                            System.out.println(ls);
                            CustomAdapter3 adapter= new CustomAdapter3(getActivity(),R.layout.station_item,ls);

                            lv.setAdapter(adapter);
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
        AppController.getInstance().addToRequestQueue(req);

        ListView lv=(ListView)getView().findViewById(R.id.ListS);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                Station dataModel=(Station) item;
                NavActivity navActivity=(NavActivity) view.getContext();

                System.out.println("click");

                NavActivity.latitude1=dataModel.getLatitude();
                NavActivity.longitude1=dataModel.getLongitude();
                NavActivity.titleMap = String.valueOf(dataModel.getNbVelo())+"/"+String.valueOf(dataModel.getNbtt());
              ll = new LatLng(dataModel.getLatitude(),dataModel.getLongitude());



                MapFragment mMapFragment = MapFragment.newInstance();





                getActivity().getFragmentManager().beginTransaction() .replace(R.id.fr1, mMapFragment,"ff").addToBackStack(null).commit();

               /* FragmentTransaction fragmentTransaction =
                        getActivity().getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fr1, mMapFragment);
                fragmentTransaction.commit();*/

                mMapFragment.getMapAsync(navActivity);
            }
        });

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
    private FragmentActivity getActivity1() {
        Context context = getActivity();
        while (context instanceof ContextWrapper) {
            if (context instanceof FragmentActivity) {
                return (FragmentActivity) context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }


}
