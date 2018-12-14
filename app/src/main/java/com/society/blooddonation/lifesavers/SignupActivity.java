package com.society.blooddonation.lifesavers;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.society.blooddonation.lifesavers.Fragments.ProfileFragment;
import com.society.blooddonation.lifesavers.utils.CheckInternetConnection;
import com.society.blooddonation.lifesavers.utils.memberShip;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TooManyListenersException;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    EditText name,email,password,bloodgroup,mobile;
    Button register;
    TextView LinkToSignIn;
    double lat, lon;
    //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Pattern email_Pattern = Patterns.EMAIL_ADDRESS;
    String NamePattern = "^[\\p{L} ]+$";
    String SpinnerValue;
    TextInputLayout textInputLayout;
    private CheckInternetConnection checkInternetConnection;
    private boolean internetStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        checkInternetConnection=new  CheckInternetConnection(getApplicationContext());
         name=findViewById(R.id.name);
         password=findViewById(R.id.password);
      ///    bloodgroup=findViewById(R.id.bloodgroup);
         mobile=findViewById(R.id.mobile);
         email=findViewById(R.id.email);
         register=findViewById(R.id.register);
         LinkToSignIn=findViewById(R.id.LinkToSignIn);
        textInputLayout=findViewById(R.id.textInputLayout1);
         final Drawable dr = getResources().getDrawable(R.drawable.ic_close_black_24dp);
         dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());

        getCurrentLocation();
        Spinner spinner= findViewById(R.id.bloodgroup_spinner);
        List<String> list = new ArrayList<String>();
        list.add("A+");
        list.add("A-");
        list.add("B+");
        list.add("B-");
        list.add("O+");
        list.add("O-");
        list.add("AB+");
        list.add("AB-");
        list.add("Please Select Blood Group");
        final int listsize = list.size() - 1;

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list)
        {
            @Override
            public int getCount() {
                return(listsize); // Truncate the list
            }
        };

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(listsize); // Hidden item to appear in the spinner


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                SpinnerValue = parent.getItemAtPosition(position).toString();

               // Toast.makeText(SignupActivity.this, SpinnerValue, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

         register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInternetConnection.checkConnection()) {

                    internetStatus = true;

// android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
                if(name.getText().length()>0 && email.getText().length()>0 &&password.getText().length()>0 && mobile.getText().length()>0 ) {
                    if(SpinnerValue.equals("Please Select Blood Group")){

                        Toast.makeText(SignupActivity.this, "Please Select Any Option", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        String  r,r1;
                     r = Double.toString(lat);
                     r1  = Double.toString(lon);
                   // Toast.makeText(SignupActivity.this, r + r1, Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(SignupActivity.this,DashboardActivity.class));
                    task insertData = new task();
                    insertData.execute(name.getText().toString(), email.getText().toString(), password.getText().toString(), SpinnerValue, mobile.getText().toString(), r, r1);

                }
                }

                else{

                    Toast.makeText(SignupActivity.this, "Fields Are Required", Toast.LENGTH_SHORT).show();
                }
                } else {
                    internetStatus = false;
                    showDialog("Problem with internet connection", "Connection Problem");
                }

            }
        });

         name.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(name.length()>0){

                if (!name.getText().toString().matches(NamePattern)) {
                    name.requestFocus();
                    name.setError("Invalid Character",dr);

                }
            }}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {




            }
        });

         email.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {

             }

             @Override
             public void afterTextChanged(Editable s) {
                      if (!email_Pattern.matcher(email.getText().toString()).matches()) {
                         email.requestFocus();
                          email.setError("Invalid Email",dr);
                         //textInputLayout.setError("Invalid Email");
                     }



             }
         });



        LinkToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this,SigninActivity.class));
            }
        });

    }
                public class task extends AsyncTask<String, Integer, String> {

                    @Override
                    protected String doInBackground(String... params) {
                        try {

                           final String name= params[0];
                           final String email= params[1];
                           final String password = params[2];
                           final String bloodgroup = params[3];
                           final String mobile= params[4];
                           final String lat=params[5];
                           final String lon=params[6];
                          //  double la=Double.parseDouble(lat);
                          //  double lo=Double.parseDouble(lon);
                            Map<String, String> newPost;
                            newPost = new HashMap<>();
                            newPost.put("name",name);
                            newPost.put("email",email);
                            newPost.put("password",password);
                            newPost.put("blood_group",bloodgroup);
                            newPost.put("mobile",mobile);
                            newPost.put("lattitude",lat);
                            newPost.put("longitude",lon);
                            newPost.put("role_id","1");
                            JSONObject mapObj=new JSONObject(newPost);
                            RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);
                            String url ="http://webapi.lifesavers.org.pk/signup.php";

                            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url,mapObj,
                                   new Response.Listener<JSONObject>() {
                                       @Override
                                       public void onResponse(JSONObject response) {
                                        try{
                                               // Printing Response i.e SuccessFull Operation/ already Exist/ Parameter Are Not Allowed.
                                       Toast.makeText(SignupActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                       // Printing Details Entered Here.
                 //                      Toast.makeText(SignupActivity.this, response.getString("user"), Toast.LENGTH_SHORT).show();
                                            JSONObject obj= new JSONObject(response.getString("user"));
                                            Context context= SignupActivity.this;


                                       }catch (JSONException e){
                                               e.printStackTrace();
                                        }
                                           Intent intent=new Intent(SignupActivity.this,SigninActivity.class);
                                           startActivity(intent);
                                       }
                                   }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                   Toast.makeText(SignupActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                  //Log.d("Jee",error.getMessage());
                                }
                            }){
                               @Override
                               protected Map<String, String> getParams() throws AuthFailureError {
                                   Map<String, String> newPost;
                                   newPost = new HashMap<>();
                                   newPost.put("name",name);
                                   newPost.put("email",email);
                                   newPost.put("password",password);
                                   newPost.put("blood_group",bloodgroup);
                                   newPost.put("mobile",mobile);
                                   newPost.put("lattitude",lat);
                                   newPost.put("longitude",lon);
                                   newPost.put("role_id","1");
                                   return newPost;
                               }

                           };


                            queue.add(stringRequest);

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        return null;}

                    @Override
                    protected void onProgressUpdate(Integer... values) {
                        super.onProgressUpdate(values);

                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);


                    }
                }


    private void showDialog(String msg, String title) {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(SignupActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        builder.setMessage(msg);
        builder.setTitle(title);
        builder.show();
    }

    private void getCurrentLocation() {

        GPSTracker gps = new GPSTracker(getApplicationContext());
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(SignupActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(SignupActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(SignupActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION}, 0);


            }
            else {
                 if (gps.canGetLocation()) {

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        lat = latitude;
                        lon = longitude;


                    }
            }




        } else {

            if (gps.canGetLocation()) {

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                lat = latitude;
                lon = longitude;


            }
        }
    }
     class GPSTracker extends Service implements LocationListener {

        private final Context mContext;

        // flag for GPS status
        boolean isGPSEnabled = false;

        // flag for network status
        boolean isNetworkEnabled = false;

        // flag for GPS status
        boolean canGetLocation = false;

        Location location; // location
        double latitude; // latitude
        double longitude; // longitude

        // The minimum distance to change Updates in meters
        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

        // The minimum time between updates in milliseconds
        private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

        // Declaring a Location Manager
        protected LocationManager locationManager;

        public GPSTracker(Context context) {
            this.mContext = context;
            getLocation();
        }
         public Location getLocation() {
             try {
                 locationManager = (LocationManager) mContext
                         .getSystemService(LOCATION_SERVICE);

                 // getting GPS status
                 isGPSEnabled = locationManager
                         .isProviderEnabled(LocationManager.GPS_PROVIDER);

                 // getting network status
                 isNetworkEnabled = locationManager
                         .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                 if (!isGPSEnabled  && !isNetworkEnabled) {
                     //
                 SignupActivity.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                 } else {
                     this.canGetLocation = true;
                     // First get location from Network Provider
                     if (isNetworkEnabled) {
                         try {
                             locationManager.requestLocationUpdates(
                                     LocationManager.NETWORK_PROVIDER,
                                     MIN_TIME_BW_UPDATES,
                                     MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                             Log.e("Network", "Network");
                             if (locationManager != null) {
                                 location = locationManager
                                         .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                 if (location != null) {
                                     latitude = location.getLatitude();
                                     longitude = location.getLongitude();
                                 }
                             }
                         } catch (SecurityException e) {
                             Log.e("SecurityException", " 1 " + e.toString());
                         }
                     }
                     // if GPS Enabled get lat/long using GPS Services
                     if (isGPSEnabled) {
                         if (location == null) {
                             try {
                                 locationManager.requestLocationUpdates(
                                         LocationManager.GPS_PROVIDER,
                                         MIN_TIME_BW_UPDATES,
                                         MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                                 Log.e("GPS Enabled", "GPS Enabled");
                                 if (locationManager != null) {
                                     location = locationManager
                                             .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                     if (location != null) {
                                         latitude = location.getLatitude();
                                         longitude = location.getLongitude();
                                     }
                                 }
                             } catch (SecurityException e) {
                                 Log.e("SecurityException", " 2 " + e.toString());
                             }
                         }
                     }


                 }

             } catch (Exception e) {
                 e.printStackTrace();
             }

             return location;
         }


         public void stopUsingGPS() {
             if (locationManager != null) {
                 try {
                     locationManager.removeUpdates(GPSTracker.this);
                 } catch (SecurityException e) {
                     Log.e("SecurityException", " 3 " + e.toString());
                 }

             }
         }


         public double getLatitude() {
             if (location != null) {
                 latitude = location.getLatitude();
             }

             // return latitude
             return latitude;
         }

         public double getLongitude() {
             if (location != null) {
                 longitude = location.getLongitude();
             }

             // return longitude
             return longitude;
         }


         public boolean canGetLocation() {
             return this.canGetLocation;
         }



         @Override
         public void onLocationChanged(Location location) {

         }

         @Override
         public void onProviderDisabled(String provider) {

         }

         @Override
         public void onProviderEnabled(String provider) {
         }

         @Override
         public void onStatusChanged(String provider, int status, Bundle extras) {
         }

         @Override
         public IBinder onBind(Intent arg0) {
             return null;
         }}

    }










