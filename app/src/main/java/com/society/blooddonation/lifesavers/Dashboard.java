package com.society.blooddonation.lifesavers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.ShareActionProvider;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessTokenSource;
import com.society.blooddonation.lifesavers.Fragments.AddUserFragment;
import com.society.blooddonation.lifesavers.Fragments.EventFragment;
import com.society.blooddonation.lifesavers.Fragments.MainFragment;
import com.society.blooddonation.lifesavers.Fragments.ProfileFragment;
import com.society.blooddonation.lifesavers.Fragments.ReminderFragment;
import com.society.blooddonation.lifesavers.utils.BackgroundService;
import com.society.blooddonation.lifesavers.utils.DownloadImageTask;
import com.society.blooddonation.lifesavers.utils.SessionManagement;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import junit.runner.Version;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;


/***************************************************************************************
 *
 *
 * This is my usefull dashboard class, will discard other dashboard class later.!!!!
 *
 * ***************************************************************************************/

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainFragment.OnMainActivityListener {
    SessionManagement sessionManagement;
    TextView emailTextView, nameTextView;
    private ShareActionProvider mShareActionProvider;
    private PendingIntent pendingIntent;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;
    private Calendar myCalendar;
    CallbackManager callbackManager;
    private static final String TAG="Dashboard";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sessionManagement = new SessionManagement(getApplicationContext());
        sessionManagement.checkLogin();
        // get user data from session
        HashMap<String, String> user = sessionManagement.getUserDetails();

        String name = user.get(sessionManagement.KEY_ID);
        String email = user.get(sessionManagement.KEY_EMAIL);

           // Toast.makeText(this, "User ID is" + name, Toast.LENGTH_SHORT).show();
        myCalendar = Calendar.getInstance();
         String y=String.valueOf(myCalendar.get(Calendar.YEAR));
         String m=String.valueOf(myCalendar.get(Calendar.MONTH)+1);
         String d=String.valueOf(myCalendar.get(Calendar.DAY_OF_MONTH) );
         String myDate=d + "-" + m + "-" + y; //dayOfMonth + "-" + (month + 1) + "-" + year
         //Toast.makeText(this, "MY DATE IS :::::"+myDate, Toast.LENGTH_SHORT).show();

        getSupportActionBar().setTitle("Dashboard");

        FragmentManager fragmentManager = getSupportFragmentManager();

        //  getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.fragment_container, MainFragment.newInstance(), "frag_main");

        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headerView = navigationView.getHeaderView(0);
        try {
            SharedPreferences Profileprefs = this.getSharedPreferences(
                    "profilePicture", this.MODE_PRIVATE);
            String URL = Profileprefs.getString("URL", "");
            if(URL.length()>0){
                new DownloadImageTask((ImageView)headerView.findViewById(R.id.imageView))
                        .execute(URL);

            }else {
                headerView.findViewById(R.id.imageView).setBackgroundResource(R.mipmap.pic);
            }
        }
        catch (Exception e) {
            Toast.makeText(this, "It's Look Like You Don't Have Working Internet Connection.", Toast.LENGTH_SHORT).show();
            e.getMessage();
            e.printStackTrace();
        }

        try {

            Gson gson = new Gson();
            SharedPreferences prefs = this.getSharedPreferences(
                    "com.example.nabeel.lifesavers", this.MODE_PRIVATE);
            String storedHashMapString = prefs.getString("Data", "");
         //  Toast.makeText(this, storedHashMapString, Toast.LENGTH_SHORT).show();
            java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            HashMap<String, String> testHashMap2 = gson.fromJson(storedHashMapString, type);
            //  String toastString = testHashMap2.get("id") + " | " + testHashMap2.get("Name")  + " | " + testHashMap2.get("Email")  + " | " + testHashMap2.get("Mobile");
            // Toast.makeText(getActivity(), toastString, Toast.LENGTH_LONG).show();

            emailTextView = (TextView) headerView.findViewById(R.id.emailtextView);
            nameTextView = (TextView) headerView.findViewById(R.id.nametextView);
            emailTextView.setText(testHashMap2.get("Email"));
            nameTextView.setText(testHashMap2.get("Name"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
          //  Toast.makeText(this, "Image ....", Toast.LENGTH_SHORT).show();
           // Log.d(TAG, "onActivityResult: IMAGEEEE!!!! ");

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.findFragmentByTag("frag_main") == null) {
                getSupportActionBar().setTitle("Dashboard");
                //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, MainFragment.newInstance(), "frag_main");
                transaction.commit();
            } else {
                ActivityCompat.finishAffinity(Dashboard.this);

            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);

        }
    }

    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "I am using Life Savers , Download it here : \n com.example.nabeel.lifesavers \n " +
                        "Donate Blood Save Life.");
        return shareIntent;
    }

    @Override
    protected void onResume() {
        super.onResume();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);

        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        setShareIntent(createShareIntent());
        // Return true to display menu
        return true;
    }

    //Error 1 : android.widget.ShareActionProvider cannot be cast to android.support.v4.view.ActionProvider.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
     /*   if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
           startActivity(new Intent(Dashboard.this, Dashboard.class));
        } else if (id == R.id.nav_events) {
            getSupportActionBar().setTitle("Our Events");

            FragmentManager fragmentManager = getSupportFragmentManager();

            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.replace(R.id.fragment_container, EventFragment.newInstance(), "frag_events");

            transaction.commit();




        } else if (id == R.id.nav_addUser) {
            getSupportActionBar().setTitle("Add Users");

            FragmentManager fragmentManager = getSupportFragmentManager();

            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.replace(R.id.fragment_container, AddUserFragment.newInstance(), "frag_addUser");

            transaction.commit();



        }else if (id == R.id.nav_logout) {

            sessionManagement.logoutUser();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onProfileClick() {
        getSupportActionBar().setTitle("My Profile");

        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //  getSupportActionBar().setDisplayShowTitleEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.fragment_container, ProfileFragment.newInstance(), "frag_profile");

        transaction.commit();

    }

    @Override
    public void onRemindarClick() {
        getSupportActionBar().setTitle("Set Reminder");

        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //   getSupportActionBar().setDisplayShowTitleEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.fragment_container, ReminderFragment.newInstance(), "frag_reminder");

        transaction.commit();

    }

    @Override
    public void OnContactUs() {

        String phone = "+923401111424";
        Intent caller = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phone, null));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String [] {android.Manifest.permission.CALL_PHONE },0 );
        }
        startActivity(caller);
        }






}
