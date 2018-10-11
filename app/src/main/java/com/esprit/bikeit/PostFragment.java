package com.esprit.bikeit;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
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
import com.esprit.bikeit.Model.Post;
import com.esprit.bikeit.Model.Station;

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
public class PostFragment extends Fragment {

    private static String TAG = NavActivity.class.getSimpleName();
    public PostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkConnection();
        String urlJsonArry = "http://"+NavActivity.url+"/post";

        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            final List<Post> ls = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject app = (JSONObject) response.get(i);


                                final String body=app.getString("body");
                                final String img=app.getString("image");
                                final String dates=app.getString("created_at");
                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                Date created = format.parse(dates);

                               Post p =new Post(img,body,created);


                                ls.add(p);
                            }

                            ListView lv=(ListView)getView().findViewById(R.id.ListPo);

                            CustomAdapter5 adapter= new CustomAdapter5(getActivity(),R.layout.post_item,ls);

                            lv.setAdapter(adapter);
                        }
                        catch (JSONException e){
                            Toasty.error(getActivity(), "error: "+e.getMessage(), Toast.LENGTH_SHORT, true).show();

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
