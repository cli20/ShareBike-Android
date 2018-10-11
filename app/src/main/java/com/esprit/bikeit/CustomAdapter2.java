package com.esprit.bikeit;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.elyeproj.loaderviewlibrary.LoaderImageView;
import com.elyeproj.loaderviewlibrary.LoaderTextView;
import com.esprit.bikeit.Model.Defi;
import com.esprit.bikeit.Model.User;
import com.esprit.bikeit.Util.DownloadImageTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by dell on 04/02/2018.
 */

public class CustomAdapter2 extends ArrayAdapter<User> implements View.OnClickListener {

    int ressource;
    List<User> users;




    public CustomAdapter2(Context context, int resource, List<User> objects) {
        super(context, resource, objects);

        this.ressource = resource;
        this.users = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User currentContact = users.get(position);

        convertView = LayoutInflater.from(getContext()).inflate(ressource, parent, false);

        LinearLayout ln = (LinearLayout)  convertView.findViewById(R.id.topUsersLeaderboardItem_layout);
        LoaderTextView tvName = (LoaderTextView) convertView.findViewById(R.id.topUsersLeaderboardItem_username);
        LoaderTextView tvScore = (LoaderTextView) convertView.findViewById(R.id.topUsersLeaderboardItem_score);
        LoaderTextView tvNumber = (LoaderTextView) convertView.findViewById(R.id.topUsersLeaderboardItem_position);
        ImageView imgUser = (ImageView) convertView.findViewById(R.id.topUsersLeaderboardItem_userIcon);
        ImageView imgRank = (ImageView) convertView.findViewById(R.id.topUsersLeaderboardItem_starImage);


        new DownloadImageTask(imgUser).execute("https://"+NavActivity.url2+"/storage/app/public/"+currentContact.getAvatar());

        int pos1=position+1;


        tvName.setText(currentContact.getName());
        tvScore.setText(currentContact.getScore());
        tvNumber.setText(pos1+".");
        if(currentContact.getRank() == 1)
        {
            imgRank.setImageResource(R.drawable.gold_medal);
            ln.setBackgroundColor(Color.parseColor("#FFFB00"));
        }
        else   if(currentContact.getRank() == 2)
        {
            imgRank.setImageResource(R.drawable.silver_medal);
            ln.setBackgroundColor(Color.parseColor("#EBEBF0"));
        }
        else   if(currentContact.getRank() == 3)
        {
            imgRank.setImageResource(R.drawable.bronze_medal);
            ln.setBackgroundColor(Color.parseColor("#FF955C"));
        }

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
