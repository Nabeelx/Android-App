package com.society.blooddonation.lifesavers.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.society.blooddonation.lifesavers.R;
import com.society.blooddonation.lifesavers.SignupActivity;

import java.util.ArrayList;
import java.util.List;

public class AddUserFragment extends Fragment {
    String SpinnerValue=" ";
    String BGValue;
    ScrollView addDonorLayout,addMemberLayout;
    Spinner addBloodGSpinner,addMemberBloodGSpinner;
    String role="";


    public AddUserFragment() {
        // Required empty public constructor
    }

    public static AddUserFragment newInstance() {
        AddUserFragment fragment = new AddUserFragment();

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
        View v= inflater.inflate(R.layout.fragment_add_user, container, false);
        Spinner spinner=v.findViewById(R.id.userRoleSpinner);
        addDonorLayout=v.findViewById(R.id.addDonorLayout);
        addMemberLayout=v.findViewById(R.id.addMemberLayout);
        addBloodGSpinner=v.findViewById(R.id.addBloodGSpinner);
        addMemberBloodGSpinner=v.findViewById(R.id.addMemberBloodGSpinner);

        List<String> list = new ArrayList<String>();
        list.add("Donor");
        list.add("Member");
        list.add("Both Member and Donor");
        list.add("Please Specify User");
        final int listsize = list.size() - 1;

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, list)
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
              //  Toast.makeText(getActivity(), position, Toast.LENGTH_SHORT).show();
                Log.d("onItemSelected: ", String.valueOf(position));
                   // 0 for Donor , 1 and 2 for Donor and Memeber
               switch (position){
                   case 0:
                        addMemberLayout.setVisibility(View.GONE);
                        addDonorLayout.setVisibility(View.VISIBLE);
                        role="1";

                   break;
                   case 1:
                        addDonorLayout.setVisibility(View.GONE);
                        addMemberLayout.setVisibility(View.VISIBLE);
                        role="2";
                   break;
                   case 2:
                       addDonorLayout.setVisibility(View.GONE);
                       addMemberLayout.setVisibility(View.VISIBLE);
                       role="3";
                   break;
                  default:
                   Toast.makeText(getActivity(), "Empty", Toast.LENGTH_SHORT).show();
               }

                Toast.makeText(getActivity(), role, Toast.LENGTH_SHORT).show();



            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> Bloodlist = new ArrayList<String>();
        Bloodlist.add("A+");
        Bloodlist.add("A-");
        Bloodlist.add("B+");
        Bloodlist.add("B-");
        Bloodlist.add("O+");
        Bloodlist.add("O-");
        Bloodlist.add("AB+");
        Bloodlist.add("AB-");
        Bloodlist.add("Please Select Blood Group");
        final int Bloodlistsize = Bloodlist.size() - 1;

        ArrayAdapter<String> BlooddataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, Bloodlist)
        {
            @Override
            public int getCount() {
                return(Bloodlistsize); // Truncate the list
            }
        };

        // ALL FOR DONOR

        BlooddataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addBloodGSpinner.setAdapter(BlooddataAdapter);
        addBloodGSpinner.setSelection(Bloodlistsize); // Hidden item to appear in the spinner


        addBloodGSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                BGValue = parent.getItemAtPosition(position).toString();


     //          Toast.makeText(getActivity(), BGValue, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // ALL FOR MEMBER
        addMemberBloodGSpinner.setAdapter(BlooddataAdapter);
        addMemberBloodGSpinner.setSelection(Bloodlistsize);
        addMemberBloodGSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                BGValue = parent.getItemAtPosition(position).toString();


                Toast.makeText(getActivity(), BGValue, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        return v;

    }

}
