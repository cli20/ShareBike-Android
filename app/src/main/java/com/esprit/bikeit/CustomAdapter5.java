package com.esprit.bikeit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.elyeproj.loaderviewlibrary.LoaderImageView;
import com.elyeproj.loaderviewlibrary.LoaderTextView;
import com.esprit.bikeit.Model.Post;
import com.esprit.bikeit.Model.Velo;
import com.esprit.bikeit.Util.DownloadImageTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CustomAdapter5  extends ArrayAdapter<Post> {
    int ressource;
    List<Post> velos;

    public CustomAdapter5(Context context, int resource, List<Post> objects) {
        super(context, resource, objects);

        this.ressource = resource;
        this.velos = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Post currentContact = velos.get(position);

        convertView = LayoutInflater.from(getContext()).inflate(ressource, parent, false);



        LoaderTextView tvName = (LoaderTextView) convertView.findViewById(R.id.pod);
        LoaderTextView tvEtat= (LoaderTextView) convertView.findViewById(R.id.pob);


        LoaderImageView imgVelo = (LoaderImageView) convertView.findViewById(R.id.poi);
        //date
        final String OLD_FORMAT = "yyyy-MM-dd hh:mm:ss";
        final String NEW_FORMAT = "yyyy/MM/dd";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date day = currentContact.getDate();
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
        tvName.setText(newDateString.toString());
        tvEtat.setText( currentContact.getNom());

        new DownloadImageTask(imgVelo).execute("https://"+NavActivity.url2+"/storage/app/public/"+currentContact.getImg());



        return convertView;

    }
}
