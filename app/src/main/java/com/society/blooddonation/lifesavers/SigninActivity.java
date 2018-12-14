package com.society.blooddonation.lifesavers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.society.blooddonation.lifesavers.utils.SessionManagement;
import com.society.blooddonation.lifesavers.utils.memberShip;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SigninActivity extends AppCompatActivity {

    EditText email,password;
    TextView LinkToSignup;
    Button login;
    SessionManagement sessionManagement;
    @Override
    public void onBackPressed() {
     Toast.makeText(this, "Please Login In Order To Continue, App will now exit", Toast.LENGTH_SHORT).show();
        ActivityCompat.finishAffinity(SigninActivity.this);
            }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        LinkToSignup=findViewById(R.id.LinkToSignup);
        login=findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> newPost;
                newPost = new HashMap<>();
                newPost.put("email",email.getText().toString());
                newPost.put("password",password.getText().toString());
                JSONObject mapObj=new JSONObject(newPost);
                RequestQueue queue = Volley.newRequestQueue(SigninActivity.this);
                String url ="http://webapi.lifesavers.org.pk/login.php";
             //For JSON ARRAY REQUEST

                /*
              RequestQueue queue = Volley.newRequestQueue(this);

    final  String url ="http://affordablesstore.pk/login_api.php";

    // prepare the Request
    JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONArray>()
            {
                @Override
                public void onResponse(JSONArray response) {
                    // display response
                    Log.d("Response", response.toString());
                }
            },
            new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error.Response", error.toString());
                }
            }
    );



    // add it to the RequestQueue
    queue.add(getRequest);*/
                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url,mapObj,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                             //   Toast.makeText(SigninActivity.this, "Okay!", Toast.LENGTH_SHORT).show();

                                try{

                                    Toast.makeText(SigninActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                 //   Toast.makeText(SigninActivity.this, response.getString("user"), Toast.LENGTH_SHORT).show();
                                //   Value {"id":40,"email":"NabeelEjaz@gmail.com","name":"NabeelEjaz","mobile":"O","blood_group":"658"}


                                    JSONObject obj= new JSONObject(response.getString("user"));
                                    Context context= SigninActivity.this;
                                   /* HashMap<String, String> data = new HashMap<>();
                                    data.put("id", obj.getString("id"));
                                    data.put("Name", obj.getString("name"));
                                    data.put("Email", obj.getString("email"));
                                    data.put("Mobile", obj.getString("mobile"));
                                    data.put("Blood_Group", obj.getString("blood_group"));

                                    Gson gson= new Gson();
                                    String hashMapString = gson.toJson(data);

                                    SharedPreferences sharedPref = context.getSharedPreferences(
                                            "com.example.nabeel.lifesavers", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("Data",  hashMapString);

                                    editor.apply();*/

                                    SessionManagement sessionManager= new SessionManagement(context);
                                    sessionManager.createLoginSession(obj.getString("id"),obj.getString("name"));

                                    new memberShip( context,obj.getString("id"),"1",obj.getString("name"),
                                            obj.getString("email"),obj.getString("mobile"),obj.getString("blood_group"),null, null, null, null,
                                            null, null, null);

                                }catch (JSONException e){
                                    Log.d("JEE", "onResponse: "+e );
                                }

                                startActivity( new Intent(SigninActivity.this, Dashboard.class));

                              //  startActivity(new Intent(SigninActivity.this, DashboardActivity.class));


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SigninActivity.this, "Error", Toast.LENGTH_SHORT).show();

                        Log.d("JEEE", "onErrorResponse: "+ error.getMessage());
                    }
                });


                queue.add(stringRequest);

            }

        });


        LinkToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SigninActivity.this, SignupActivity.class));
            }
        });
    }
}
