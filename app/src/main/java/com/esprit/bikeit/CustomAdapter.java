package com.esprit.bikeit;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.elyeproj.loaderviewlibrary.LoaderTextView;
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
 * Created by dell on 17/10/2017.
 */

public class CustomAdapter extends ArrayAdapter<Defi>  implements View.OnClickListener {
    private static String TAG = NavActivity.class.getSimpleName();
    int ressource;
    List<Defi> defis;




    public CustomAdapter(Context context, int resource, List<Defi> objects) {
        super(context, resource, objects);

        this.ressource = resource;
        this.defis = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Defi currentContact = defis.get(position);



        convertView = LayoutInflater.from(getContext()).inflate(ressource, parent, false);
      //  ShimmerRecyclerView shimmerRecycler =  convertView.findViewById(R.id.card_view);
       // shimmerRecycler.showShimmerAdapter();
        RelativeLayout constraintLayout = (RelativeLayout) convertView.findViewById(R.id.r22);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        LoaderTextView tvName = (LoaderTextView) convertView.findViewById(R.id.name);
        //TextView tvCreated = (TextView) convertView.findViewById(R.id.created);
        LoaderTextView tvPub = (LoaderTextView) convertView.findViewById(R.id.publication);

        NumberProgressBar pb= (NumberProgressBar) convertView.findViewById(R.id.simpleProgressBar);
     final   AppCompatButton reward =(AppCompatButton) convertView.findViewById(R.id.btnReward);

        final Animation myAnim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);
        myAnim.setRepeatCount(150);
        myAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                reward.startAnimation(myAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        reward.startAnimation(myAnim);

        double progress= (currentContact.getDistance()*100)/Double.valueOf(currentContact.getDesc());
        if(currentContact.getDistance()>Double.valueOf(currentContact.getDesc()))
            progress =100;
        System.out.println("custom : "+currentContact.getDesc()+ " "+currentContact.getDistance()+" "+progress);
        if(progress>=100) {
            reward.setEnabled(true);
            reward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences shared = getContext().getSharedPreferences("pref2", Context.MODE_PRIVATE);
                    int idUser=shared.getInt("id_user",0);
                    float free=shared.getFloat("free", 0f);
                    SharedPreferences.Editor editor = shared.edit();
                    float newFree = free+Float.valueOf(currentContact.getPrice());
                    editor.putFloat("free", newFree);
                    editor.commit();
                    String urlJsonArry = "http://"+NavActivity.url+"/api/free/"+idUser+"/"+currentContact.getPrice()+"/"+currentContact.getId();

                    JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {


                                }

                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                        }
                    });
                    AppController.getInstance().addToRequestQueue(req);
                    Toasty.error(getContext(), "Free hours added to your account", Toast.LENGTH_SHORT, true).show();

                   ListFragment firstFragment = new ListFragment();
                    NavActivity n=(NavActivity) v.getContext();
                    n.getFragmentManager().beginTransaction()
                            .replace(R.id.fr1, firstFragment,"ListFragment").addToBackStack(null).commit();
                    notifyDataSetChanged();


                }
            });
        }
        else{
            reward.setVisibility(View.GONE);
            reward.setEnabled(false);}
        pb.setProgress((int)progress);

        //date
        final String OLD_FORMAT = "yyyy-MM-dd hh:mm:ss";
        final String NEW_FORMAT = "yyyy/MM/dd";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date day = currentContact.getCreated();
        String reportDate = df.format(day);
        String newDateString;
        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
        Date d = new Date();
        try {
            d = sdf.parse(reportDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.applyPattern(NEW_FORMAT);
        newDateString = sdf.format(d);


        tvName.setText("Reach "+currentContact.getDesc()+" KM");
        //tvCreated.setText(newDateString.toString());
        tvPub.setText("Price : "+currentContact.getPrice() +" Free Hours");


        ViewHolder holder;
        holder = new ViewHolder();
        final Button txt=(Button) convertView.findViewById(R.id.likeView);
      //  holder.imgch=(ImageView) convertView.findViewById(R.id.likeButton);
        if(currentContact.isSubed())
        {
            txt.setText("Subscribed");
            txt.setTextColor(Color.parseColor("#32C9AE"));
        }
        txt.setTag(position);



        txt.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if(txt.getText().toString().equals("Subscribe")) {
                    int position = (Integer) v.getTag();
                    Object object = getItem(position);
                    Defi dataModel = (Defi) object;
                    // TODO Auto-generated method stub
                    SharedPreferences shared = getContext().getApplicationContext().getSharedPreferences("pref2", Context.MODE_PRIVATE);
                    int idUser = shared.getInt("id_user", 0);
                    final String urlJsonArry = "http://" + NavActivity.url + "/api/sub/defi/" + dataModel.getId() + "/" + idUser;

                    JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {

                                }

                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                        }
                    });
                    AppController.getInstance().addToRequestQueue(req);
                    Toasty.error(getContext(), "Subscribed to challenge", Toast.LENGTH_SHORT, true).show();


                    System.out.println(dataModel.getId());
                    txt.setText("Subscribed");
                    txt.setTextColor(Color.parseColor("#32C9AE"));
                    currentContact.setSubed(true);

                }





            }
        });


        return convertView;

    }


    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Defi dataModel=(Defi) object;

    }
    static class ViewHolder {
        public ImageView imgch;
        public TextView txt;
    }
}
