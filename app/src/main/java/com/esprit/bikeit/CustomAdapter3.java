package com.esprit.bikeit;

import android.app.FragmentTransaction;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.elyeproj.loaderviewlibrary.LoaderTextView;
import com.esprit.bikeit.Model.Defi;
import com.esprit.bikeit.Model.Station;
import com.esprit.bikeit.Model.User;
import com.esprit.bikeit.Util.DownloadImageTask;
import com.google.android.gms.maps.MapFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by dell on 04/02/2018.
 */

public class CustomAdapter3 extends ArrayAdapter<Station> implements View.OnClickListener {

    int ressource;
    List<Station> stations;




    public CustomAdapter3(Context context, int resource, List<Station> objects) {
        super(context, resource, objects);

        this.ressource = resource;
        this.stations = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Station currentContact = stations.get(position);

        convertView = LayoutInflater.from(getContext()).inflate(ressource, parent, false);

        LoaderTextView tvName = (LoaderTextView) convertView.findViewById(R.id.tvId);
        LoaderTextView tvVelo = (LoaderTextView) convertView.findViewById(R.id.tvNbVelo);




        tvName.setText(currentContact.getNom());
        if(currentContact.getNbVelo()==1)
       tvVelo.setText(currentContact.getNbVelo() + " BIKE");
        else
            tvVelo.setText(currentContact.getNbVelo() + " BIKES");

        // imgDefi.setImageResource(R.drawable.images);

        //imgContact.setImageResource(R.drawable.img1);

        return convertView;

    }

    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Defi dataModel=(Defi) object;

    }
}
