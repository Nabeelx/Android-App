package com.society.blooddonation.lifesavers.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.society.blooddonation.lifesavers.DashboardActivity;
import com.society.blooddonation.lifesavers.R;
import com.society.blooddonation.lifesavers.SignupActivity;
import com.google.gson.JsonObject;

import android.app.AlertDialog;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainFragment extends Fragment {

    CardView profileCard,reminderCard, ShareCard,contactCard;
    TextView newsTicker;
    private OnMainActivityListener mListener;
    public SpannableStringBuilder builder = new SpannableStringBuilder();

    public MainFragment() {
        // Required empty public constructor
    }


    public static MainFragment newInstance( ) {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);




    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
         profileCard=view.findViewById(R.id.profileCard);
         reminderCard=view.findViewById(R.id.reminderCard);
         contactCard=view.findViewById(R.id.contactCard);
         //reminderCard=view.findViewById(R.id.reminderCard);
        newsTicker=view.findViewById(R.id.newsTicker);
        String url="http://webapi.lifesavers.org.pk/news_api.php";
        RequestQueue rq= Volley.newRequestQueue(getActivity());
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Jee",response);
               // Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                try {

                     List<String> news=new ArrayList<>();
                     JSONObject jsonObject = new JSONObject(response);
                     String result=jsonObject.getString("latest_news");
                     JSONArray jsonArray = new JSONArray(result);
                     for(int i =0 ; i<jsonArray.length() ;i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String r = obj.getString("name");
                        news.add(r);

                        Log.d("onResponse: ", r);
                         StringBuilder xBuilder= new StringBuilder();
                         for(String value : news){
                             xBuilder.append(value+"\n\t ");
                         }
                         newsTicker.setText(xBuilder);
                         newsTicker.setSelected(true);


                    }} catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Something Is Not Right", Toast.LENGTH_SHORT).show();
                }
    });
         rq.add(stringRequest);

         profileCard.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 mListener.onProfileClick();

             }
         });
         reminderCard.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 mListener.onRemindarClick();
             }
         });
         contactCard.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                     Toast.makeText(getActivity(), "Can not make call due to your network services", Toast.LENGTH_SHORT).show();
                     return;
                 }
                 final AlertDialog.Builder contactUs = new AlertDialog.Builder(getActivity());
                 contactUs.setTitle("Please Note Before You Proceed");
                 contactUs.setIcon(R.mipmap.ic_launcher_foreground);
                 TextView textView = new TextView(getActivity());
                 String s ="\n"+
                         "\t\t  Use Ext 1 For Islamabad\n\n" +
                         "\t\t  Use Ext 2 For Rawalpindi\n\n" +
                         "\t\t  Use Ext 3 For Lahore\n\n" +
                         "\t\t  Use Ext 4 For Multan\n\n";
                        /*"Please Note Before You Proceed \n" +
                                                    "\n" +*/
                 textView.setText(s);
                 contactUs.setView(textView);
                 contactUs.setPositiveButton("Proceed To Call", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int which) {
                         mListener.OnContactUs();
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
         });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMainActivityListener) {
            mListener = (OnMainActivityListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMainActivityListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnMainActivityListener {
        void onProfileClick();
        void onRemindarClick();
        void OnContactUs();
    }
}
