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

import com.elyeproj.loaderviewlibrary.LoaderImageView;
import com.elyeproj.loaderviewlibrary.LoaderTextView;
import com.esprit.bikeit.Model.Defi;
import com.esprit.bikeit.Model.Station;
import com.esprit.bikeit.Model.User;
import com.esprit.bikeit.Model.Velo;
import com.esprit.bikeit.Util.DownloadImageTask;
import com.google.android.gms.maps.MapFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by dell on 04/02/2018.
 */

public class CustomAdapter4 extends ArrayAdapter<Velo> implements View.OnClickListener {

    int ressource;
    List<Velo> velos;




    public CustomAdapter4(Context context, int resource, List<Velo> objects) {
        super(context, resource, objects);

        this.ressource = resource;
        this.velos = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Velo currentContact = velos.get(position);

        convertView = LayoutInflater.from(getContext()).inflate(ressource, parent, false);



        LoaderTextView tvName = (LoaderTextView) convertView.findViewById(R.id.tvvNom);
        LoaderTextView tvEtat= (LoaderTextView) convertView.findViewById(R.id.tvvEtat);
        LoaderTextView tvMarque = (LoaderTextView) convertView.findViewById(R.id.tvvMarque);

        LoaderImageView imgVelo = (LoaderImageView) convertView.findViewById(R.id.imgV);

        tvName.setText(tvName.getText()+ " " + currentContact.getNom());
        tvEtat.setText(tvEtat.getText()+ " " + currentContact.getEtat());
        tvMarque.setText(tvMarque.getText()+ " " + currentContact.getMarque());
        new DownloadImageTask(imgVelo).execute("https://"+NavActivity.url2+"/storage/app/public/"+currentContact.getImage());



        return convertView;

    }

    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Defi dataModel=(Defi) object;

    }
}
