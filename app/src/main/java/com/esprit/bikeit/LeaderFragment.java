package com.esprit.bikeit;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
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

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 */
public class LeaderFragment extends Fragment {

    private static String TAG = MainActivity.class.getSimpleName();
    public LeaderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Leaderboard");
        return inflater.inflate(R.layout.fragment_leader, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checkConnection();
         String urlJsonArry = "http://"+NavActivity.url+"/api/users/order";

        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            List<User> lu = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject app = (JSONObject) response.get(i);
                                int l = i+1;
                                int id=app.getInt("id");
                                String name=app.getString("name");
                                String email=app.getString("email");
                                String password=app.getString("password");
                                String avatar=app.getString("avatar");
                                String score=app.getString("score");
                                User u1 =new User(id,name,email,password);
                                u1.setRank(l);
                                u1.setAvatar(avatar);
                                u1.setScore(score);

                                lu.add(u1);
                            }

                            ListView lv=(ListView)getView().findViewById(R.id.listL);

                            CustomAdapter2 adapter= new CustomAdapter2(getActivity(),R.layout.leader_item,lu);

                            lv.setAdapter(adapter);
                        }
                        catch (JSONException e){
                            Toasty.error(getActivity(), e.getMessage(), Toast.LENGTH_SHORT, true).show();

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
