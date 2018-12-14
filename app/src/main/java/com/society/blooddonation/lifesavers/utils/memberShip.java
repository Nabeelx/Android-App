package com.society.blooddonation.lifesavers.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.society.blooddonation.lifesavers.SigninActivity;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class memberShip {

    Context context;

    memberShip(){}

  public  memberShip(Context context,String ID,String roleID,String userName,String email,String mobile ,String BG, final String fatherName, final String cnic,
               final String designation, final String department, final String section, final String postalAdd,
               final String ice){
        Map<String, String> data = new HashMap<String,String>();
        data.put("id",ID);
        data.put("role_id",roleID);
        data.put("Name", userName);
        data.put("FatherName", fatherName);
        data.put("CNIC", cnic);
        data.put("Email", email);
        data.put("Mobile", mobile);
        data.put("Blood_Group", BG);
        data.put("Designation", designation);
        data.put("Department", department);
        data.put("Section", section);
        data.put("PostalAdd", postalAdd);
        data.put("ICE", ice);
        this.context=context;
        Gson gson= new Gson();
        String hashMapString = gson.toJson(data);
        SharedPreferences sharedPref = this.context.getSharedPreferences(
                "com.example.nabeel.lifesavers", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("Data");
        editor.putString("Data",hashMapString);
        editor.commit();

    }

}
