package com.esprit.bikeit;



import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.esprit.bikeit.Model.Station;
import com.esprit.bikeit.Model.Velo;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 */
public class VeloFragment extends Fragment {
    private static String TAG = MainActivity.class.getSimpleName();

    public VeloFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_velo, container, false);
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkConnection();
        SharedPreferences shared = getActivity().getSharedPreferences("pref2", Context.MODE_PRIVATE);
        int idUser=shared.getInt("id_user",0);
        Toasty.info(getActivity(), "swipe left to view bike location.", Toast.LENGTH_SHORT, true).show();
        String urlJsonArry = "http://"+NavActivity.url+"/api/rented/"+idUser;

        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            final List<Velo> lv = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject app = (JSONObject) response.get(i);

                                int id=app.getInt("matricule");
                                final String nom=app.getString("nom");
                                final String etat=app.getString("etat");
                                final String marque=app.getString("marque");
                                final String image=app.getString("image");
                                int id_boitier=app.getInt("id_boitier");
                                int id_proprietaire=app.getInt("id_user");
                                double lat=app.getDouble("lat");
                                double lng=app.getDouble("lng");


                                Velo v=new Velo(id,nom,marque,etat,image,id_boitier,id_proprietaire);
                                v.setLat(lat);
                                v.setLng(lng);
                                lv.add(v);
                            }

                            final SwipeMenuListView lvv=(SwipeMenuListView)getView().findViewById(R.id.listV);

                            SwipeMenuCreator creator = new SwipeMenuCreator() {

                                @Override
                                public void create(SwipeMenu menu) {

                                    SwipeMenuItem deleteItem = new SwipeMenuItem(
                                            getActivity().getApplicationContext());
                                    // set item background
                                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                                            0x3F, 0x25)));
                                    // set item width
                                    deleteItem.setWidth(dp2px(90));
                                    // set a icon
                                    deleteItem.setIcon(R.drawable.marker);
                                    // add to menu
                                    menu.addMenuItem(deleteItem);
                                }
                            };

// set creator
                            lvv.setMenuCreator(creator);
                            lvv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                                    switch (index) {
                                        case 0:
                                            // open


                                            Velo dataModel=lv.get(position);;
                                            NavActivity navActivity=(NavActivity) view.getContext();

                                            System.out.println("click");
                                            NavActivity.latitude1=dataModel.getLat();
                                            NavActivity.longitude1=dataModel.getLng();

                                            MapFragment mMapFragment = MapFragment.newInstance();
                                            getActivity().getFragmentManager().beginTransaction() .replace(R.id.fr1, mMapFragment,"ff").addToBackStack(null).commit();

                                            mMapFragment.getMapAsync(navActivity);
                                            break;

                                    }
                                    // false : close the menu; true : not close the menu
                                    return false;
                                }
                            });


                            System.out.println(lv);
                            CustomAdapter4 adapter= new CustomAdapter4(getActivity(),R.layout.velo_item,lv);

                            lvv.setAdapter(adapter);
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
        SwipeMenuListView lvv=(SwipeMenuListView)getView().findViewById(R.id.listV);



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

    public  int dp2px(float dips)
    {

        return (int) (dips * this.getActivity().getApplicationContext().getResources().getDisplayMetrics().density + 0.5f);
    }
}
