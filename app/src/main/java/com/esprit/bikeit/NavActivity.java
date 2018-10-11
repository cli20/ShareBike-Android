package com.esprit.bikeit;

import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arsy.maps_library.MapRipple;
import com.esprit.bikeit.Model.Station;
import com.esprit.bikeit.Model.User;
import com.esprit.bikeit.Util.DownloadImageTask;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import es.dmoral.toasty.Toasty;

public class NavActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback,GoogleMap.OnMarkerClickListener{

    static double latitude1;
    static double longitude1;
    static String titleMap;

  //  static String url = "192.168.1.100/voyager/public/index.php";
   // static String url2 = "192.168.1.100/voyager/public/";
  //static String url = "192.168.101.1:8000";
  //  static String url2 = "192.168.101.1:8000";
  static String url = "proit.tn/sharebike/public/index.php";
    static String url2 = "proit.tn/sharebike";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);




        setTitle("sharebike");
        setContentView(R.layout.activity_nav);

       PostFragment firstFragment = new PostFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.fr1, firstFragment).commit();
        //get user
        SharedPreferences shared = getSharedPreferences("pref2", getApplicationContext().MODE_PRIVATE);

        String name=shared.getString("name", "");
        String email = (shared.getString("email", ""));
        String avatar = (shared.getString("avatar", ""));

        User u=new User();
        u.setName(name);
        u.setEmail(email);
        u.setAvatar(avatar);

        NavigationView navv=(NavigationView) findViewById(R.id.nav_view);
       View header= navv.getHeaderView(0);

        TextView tvName=(TextView)header.findViewById(R.id.userNameTV);
        TextView tvMail=(TextView)header.findViewById(R.id.userMailTV);
        ImageView imgAvatar=(ImageView) header.findViewById(R.id.userImageTV);
        tvName.setText(name);
        tvMail.setText(email);
        System.out.println("avatar : "+avatar);
        new DownloadImageTask(imgAvatar).execute("http://"+url2+"/storage/app/public/"+avatar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    @Override
    public void onMapReady(GoogleMap map) {

        map.addMarker(new MarkerOptions()
                .position(new LatLng(NavActivity.longitude1, NavActivity.latitude1))
                .title(titleMap));
        MapRipple mapRipple = new MapRipple(map, new LatLng(NavActivity.longitude1, NavActivity.latitude1),getApplicationContext());
        mapRipple.withFillColor(Color.BLUE);
        mapRipple  .withStrokeColor(Color.BLACK);
        mapRipple     .withNumberOfRipples(3);
        mapRipple    .withStrokewidth(10)   ;   // 10dp
        mapRipple   .withDistance(2000)     ; // 2000 metres radius
        mapRipple .withRippleDuration(12000)  ;  //12000ms
        mapRipple .withTransparency(0.5f);
        mapRipple.startRippleMapAnimation();
        System.out.println("lat: " +NavActivity.latitude1);
        MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory
                .fromResource(R.drawable.sport));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(NavActivity.longitude1, NavActivity.latitude1)).zoom(14.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.moveCamera(cameraUpdate);
        map.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        QrFragment firstFragment = new QrFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.fr1, firstFragment,"QrFragment").addToBackStack(null).commit();

        return false;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }

    /*    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            PostFragment firstFragment = new PostFragment();


        getFragmentManager().beginTransaction()
                .replace(R.id.fr1, firstFragment,"PostFragment").addToBackStack(null).commit();
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            LeaderFragment firstFragment = new LeaderFragment();
            getFragmentManager().beginTransaction()
                    .replace(R.id.fr1, firstFragment,"LeaderFragment").addToBackStack(null).commit();

        }else if (id == R.id.goal) {
            ListFragment firstFragment = new ListFragment();


            getFragmentManager().beginTransaction()
                    .replace(R.id.fr1, firstFragment,"ListFragment").addToBackStack(null).commit();

        }
        else if (id == R.id.nav_slideshow) {
            QrFragment firstFragment = new QrFragment();
            getFragmentManager().beginTransaction()
                    .replace(R.id.fr1, firstFragment,"QrFragment").addToBackStack(null).commit();

        } else if (id == R.id.nav_manage) {

            Fragment fragment = getFragmentManager().findFragmentByTag("map");
            if(fragment != null)
                getFragmentManager().beginTransaction().remove(fragment).commit();

            StationFragment firstFragment = new StationFragment();

            getFragmentManager().beginTransaction()
                    .replace(R.id.fr1, firstFragment,"StationFragment").addToBackStack(null).commit();

        } else if (id == R.id.nav_share) {
            VeloFragment firstFragment = new VeloFragment();
            getFragmentManager().beginTransaction()
                    .replace(R.id.fr1, firstFragment,"VeloFragment").addToBackStack(null).commit();


        }
        else if (id == R.id.nav_logout) {

            SharedPreferences sharedpreferences = getSharedPreferences("pref2", getApplicationContext().MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString("email", "");
            editor.putString("password", "");
            editor.commit();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = getFragmentManager().findFragmentByTag("QrFragment");
        fragment.onActivityResult(requestCode, resultCode, data);
    }

}
