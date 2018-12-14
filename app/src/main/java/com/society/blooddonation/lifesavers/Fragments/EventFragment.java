package com.society.blooddonation.lifesavers.Fragments;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.society.blooddonation.lifesavers.R;
import com.society.blooddonation.lifesavers.utils.EventsAdapter;
import com.society.blooddonation.lifesavers.utils.EventsList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class EventFragment extends Fragment {


    private  static  final  String TAG="Events";
    android.support.v7.widget.RecyclerView eventView;
    String end,start,address,name,desc;
    String first_name = "";
    String last_name = "";
    StringBuilder xBuilder= new StringBuilder();


    public EventFragment() {
        // Required empty public constructor
    }


    public static EventFragment newInstance( ) {
        EventFragment fragment = new EventFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_event, container, false);
        FacebookSdk.sdkInitialize(getActivity());
        AppEventsLogger.activateApp(getActivity());
           final List<EventsList> m = new ArrayList<>();
        eventView=v.findViewById(R.id.eventsRecycler);
        eventView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        eventView.setLayoutManager(llm);
        String val="EAAD0JQAuiXIBANsPO6ujk1qJA5SCBzDSUFUy89Phv7YpRIDQYj7zD4O12CKWmXf7a9bYlpGfmt1gZCX5UuZApo3ZBXaOcETKAQRqduNgNVo0V9FxT22PS4Bb97za1IwZASykQqF4OT9CsGZBbAZBYlVsR2BELFfVU9UKctBuQFKAZDZD";
        AccessToken accessToken = new AccessToken(val,
                "268439754017138",
                "268439754017138",
                null,
                null, null,
                null, null,null);
        GraphRequest request = GraphRequest.newGraphPathRequest(
                 accessToken,
                "/LifeSaversBloodDonorSociety/events",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                      //  final JSONObject jsonObject = response.getJSONObject();
                        JSONObject jsonObject = response.getJSONObject();
                        List<String> landx=new ArrayList<>(  );
                        Log.d("RESPONSE",response.getJSONObject().toString());

                        try {

                            String data= jsonObject.getString("data");
                            JSONArray jsonArray = new JSONArray(data);
                            String placeString ="";

                            for(int i =0 ; i <jsonArray.length();i++){

                                JSONObject jsonObj=jsonArray.getJSONObject(i);
                                JSONObject place= new JSONObject(jsonObj.optString("place"));
                                m.add(new EventsList("Description: "+jsonObj.optString("description"),
                                       "Event: "+ jsonObj.optString("name"),
                                        "Start Time: "+jsonObj.optString("start_time"),
                                        "End Time: "+jsonObj.optString("end_time"),
                                        "Place: " + place.optString("name")));


                                Log.d(TAG, "onCompleted: Here "+place);
                                EventsAdapter adapter = new EventsAdapter(m);
                                eventView.setAdapter(adapter);

                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        request.executeAsync();

        return  v;
    }

}

