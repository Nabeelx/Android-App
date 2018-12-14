package com.society.blooddonation.lifesavers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.society.blooddonation.lifesavers.Fragments.MainFragment;
import com.society.blooddonation.lifesavers.Fragments.ProfileFragment;
import com.society.blooddonation.lifesavers.Fragments.ReminderFragment;
import com.society.blooddonation.lifesavers.utils.SessionManagement;

import java.util.HashMap;


public class DashboardActivity extends AppCompatActivity implements MainFragment.OnMainActivityListener {
    SessionManagement sessionManagement;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManagement=new SessionManagement(getApplicationContext());
        sessionManagement.checkLogin();
        // get user data from session
        HashMap<String, String> user = sessionManagement.getUserDetails();

        String name = user.get(sessionManagement.KEY_ID);
        String email = user.get(sessionManagement.KEY_EMAIL);

        Toast.makeText(this, "User ID is"+name, Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_dashboard);


        /* LOGOUT SNIPPET */
      /*//  logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManagement.logoutUser();
            }
        });*/
        getSupportActionBar().setTitle("Dashboard");
        FragmentManager fragmentManager = getSupportFragmentManager();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.fragment_container, MainFragment.newInstance(), "frag_main");

        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag("frag_main") == null) {

            getSupportActionBar().setTitle("Dashboard");

            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.replace(R.id.fragment_container, MainFragment.newInstance(), "frag_main");

            transaction.commit();
        } else {

           super.onBackPressed();
        }
    }


    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    @Override
    public void onProfileClick() {
        getSupportActionBar().setTitle("My Profile");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();

      transaction.replace(R.id.fragment_container, ProfileFragment.newInstance(), "frag_profile");

        transaction.commit();

    }

    @Override
    public void onRemindarClick() {
        getSupportActionBar().setTitle("Set Reminder");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();

      transaction.replace(R.id.fragment_container, ReminderFragment.newInstance(), "frag_reminder");

        transaction.commit();

    }


    @Override
    public void OnContactUs() {

        final AlertDialog.Builder contactUs = new AlertDialog.Builder(getApplicationContext());
        contactUs.setTitle("Contact Us");
        contactUs.setIcon(R.drawable.ic_phone_android_black_24dp);
        TextView textView = new TextView(this);
        textView.setText("Please Note Before You Process " +
                "\n Use Ext 1 For Islamabad " +
                "\n Use Ext 2 For Rawalpindi \n" +
                "\n Use Ext 3 For Lahore \n" +
                "\n Use Ext 4 For Multan \n ");
        contactUs.setView(textView);
        contactUs.setPositiveButton("Proceed To Call", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String phone = "+923401111424";
                Intent caller = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phone, null));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(DashboardActivity.this, "Can not make call due to your network services", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(caller);
                dialog.dismiss();
            }
        });
        contactUs.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        contactUs.show();
    }

}