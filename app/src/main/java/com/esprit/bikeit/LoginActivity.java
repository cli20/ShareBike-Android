package com.esprit.bikeit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.esprit.bikeit.Model.User;
import com.esprit.bikeit.Util.Password;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    // private String urlJsonArry = "http://10.0.2.2:5000/user";
    //private String urlJsonArry = "http://192.168.43.203:5000/user";
    private String urlJsonArry = "http://"+NavActivity.url+"/api/user";

    private String jsonResponse;
    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
    @Bind(R.id.link_signup)
    TextView _signupLink;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        PackageInfo info;


        SharedPreferences shared = getSharedPreferences("pref2", MODE_PRIVATE);
        int idUser=shared.getInt("id_user", 0);
        String name=shared.getString("name", "");
        String email = (shared.getString("email", ""));
        String password= (shared.getString("password", ""));
        String avatar= (shared.getString("avatar", ""));
        String score= (shared.getString("score", ""));
        double free=shared.getFloat("free", 0f);

        User u=new User(idUser,name,email,password);
        u.setAvatar(avatar);
        u.setScore(score);
        u.setFreeHours(free);
        EditText ed1=(EditText) findViewById(R.id.input_email);
        EditText ed2=(EditText) findViewById(R.id.input_password);
        System.out.println(email);



        if(!email.equals("") && !password.equals(""))
        {

           // Intent intent = new Intent(this, MyLocationUsingHelper.class);

            Intent intent = new Intent(this, NavActivity.class);

            startActivity(intent);


           /* ed1.setText(email);
            ed2.setText(password);*/


        }


        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
             //   overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        System.out.println("1");
        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        final List<User> lu=new ArrayList<User>();
                        final User u=new User();
                        System.out.println("2");
                        try {
                            // Parsing json array response
                            // loop through each json object
                            System.out.println("3");
                            jsonResponse = "";
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response.get(i);

                                int id=person.getInt("id");
                                String name=person.getString("name");
                                String email=person.getString("email");
                                String password=person.getString("password");
                                String avatar=person.getString("avatar");
                               String score=person.getString("score");
                                Double free=person.getDouble("freeHours");
                                User u1 =new User(id,name,email,password);
                                u1.setAvatar(avatar);
                                u1.setScore(score);
                               u1.setFreeHours(free);
                                lu.add(u1);
                            }
                            System.out.println(lu);
                            boolean b=false;
                            for (int j=0;j<lu.size();j++) {
                                System.out.println(j);
                                String ch=lu.get(j).getPassword().replaceFirst("y","a");
                                if ((email.equals(lu.get(j).getEmail())) && ( Password.checkPassword(password,ch))) {
                                    SharedPreferences sharedpreferences = getSharedPreferences("pref2", getApplicationContext().MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putInt("id_user",lu.get(j).getIdUser());
                                    editor.putString("name", lu.get(j).getName());
                                    editor.putString("email", email);
                                    editor.putString("password", password);
                                   editor.putString("avatar", lu.get(j).getAvatar());
                                   editor.putString("score", lu.get(j).getScore());

                                    editor.putFloat("free", (float)lu.get(j).getFreeHours());


                                    editor.commit();
                                    u.setIdUser(lu.get(j).getIdUser());
                                    u.setEmail(lu.get(j).getEmail());

                                    u.setName(lu.get(j).getName());
                                    u.setPassword(lu.get(j).getPassword());
                                    u.setAvatar(lu.get(j).getAvatar());
                                    u.setScore(lu.get(j).getScore());
                                    u.setFreeHours(lu.get(j).getFreeHours());
                                    b = true;
                                    System.out.println("true");
                                }
                            }
                            // On complete call either onLoginSuccess or onLoginFailed
                            if(b)
                            {
                                new android.os.Handler().postDelayed(
                                        new Runnable() {
                                            public void run() {
                                                onLoginSuccess(u);
                                                progressDialog.dismiss();
                                            }
                                        }, 3000);
                            }

                            else{
                                new android.os.Handler().postDelayed(
                                        new Runnable() {
                                            public void run() {
                                                onLoginFailed();
                                                progressDialog.dismiss();
                                            }
                                        }, 3000);
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toasty.error(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        });
        AppController.getInstance().addToRequestQueue(req);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically

                this.finish();

            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(User u) {
        _loginButton.setEnabled(true);



      //  Intent intent = new Intent(this, MyLocationUsingHelper.class);
        Intent intent = new Intent(this, NavActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toasty.error(getApplicationContext(),"login failed", Toast.LENGTH_SHORT, true).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 3 || password.length() > 10) {
            _passwordText.setError("between 3 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
