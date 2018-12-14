package com.society.blooddonation.lifesavers.Fragments;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.society.blooddonation.lifesavers.Dashboard;
import com.society.blooddonation.lifesavers.R;
import com.society.blooddonation.lifesavers.SigninActivity;
import com.society.blooddonation.lifesavers.SignupActivity;
import com.society.blooddonation.lifesavers.utils.DownloadImageTask;
import com.society.blooddonation.lifesavers.utils.SessionManagement;
import com.society.blooddonation.lifesavers.utils.memberShip;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class ProfileFragment extends Fragment {
    TextView MemberShip, userName, userEmail, userBG,
            memberFatherName, memberName, membersection, memberdepartment,
            memberCNIC, memberdesignation, memberAddress, memberBG, memberEmail;
    ScrollView ScrollView;
    EditText fname, cnic, designation, department, section, postalAdd, ice, reason, willpoint, ref;
    LinearLayout donor_Layout;
    String userID = "";
    String role_id = "1";
    HashMap<String, String> data = new HashMap<>(); // For Updated DATA.
    String storeID = "";
    String storename = "";
    String storemobile = "";
    String storebg = "";
    String storeemail = "";
    FrameLayout frameLayout;
    de.hdodenhof.circleimageview.CircleImageView image;

    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        final float scale = getResources().getDisplayMetrics().density;
        final int sizeInDp = (int) (12 * scale + 0.5f);
        Gson gson = new Gson();
        SharedPreferences prefs = getActivity().getSharedPreferences(
                "com.example.nabeel.lifesavers", getActivity().MODE_PRIVATE);
        String storedHashMapString = prefs.getString("Data", "");
        java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        Map<String, String> testHashMap2 = gson.fromJson(storedHashMapString, type);


        //  String toastString = testHashMap2.get("id") + " | " + testHashMap2.get("Name")  + " | " + testHashMap2.get("Email")  + " | " + testHashMap2.get("Mobile");
        // Toast.makeText(getActivity(), toastString, Toast.LENGTH_LONG).show();
        userID = testHashMap2.get("id");
        storeID = testHashMap2.get("id");
        storename = testHashMap2.get("Name");
        storeemail = testHashMap2.get("Email");
   //     Toast.makeText(getActivity(), "From Map::" + " " + testHashMap2.get("Name"), Toast.LENGTH_SHORT).show();
     //   Toast.makeText(getActivity(), "From String VAr::" + " " + storename, Toast.LENGTH_SHORT).show();
        storebg = testHashMap2.get("Blood_Group");
        storemobile = testHashMap2.get("Mobile");

       // Toast.makeText(getActivity(), storeemail, Toast.LENGTH_SHORT).show();

        // Assigning Values to Members View
        role_id = testHashMap2.get("role_id");


        frameLayout = view.findViewById(R.id.frameLayout);
        image = view.findViewById(R.id.circleImageView_personalInfo_profile);

        try {
            SharedPreferences Profileprefs = getActivity().getSharedPreferences(
                    "profilePicture", getActivity().MODE_PRIVATE);
            String URL = Profileprefs.getString("URL", "");
            if(URL.length()>0){
            new DownloadImageTask((ImageView) view.findViewById(R.id.circleImageView_personalInfo_profile))
                    .execute(URL);

        }else {
                view.findViewById(R.id.circleImageView_personalInfo_profile).setBackgroundResource(R.mipmap.pic);
            }
        }
        catch (Exception e) {
            Toast.makeText(getContext(), "It's Look Like You Don't Have Working Internet Connection.", Toast.LENGTH_SHORT).show();
            e.getMessage();
            e.printStackTrace();
        }

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*              To Customize Use This Code.
                PickSetup setup = new PickSetup()
                        .setTitle("Choose How You Want To Update Your Profile Image")
                        .setTitleColor(yourColor)
                        .setBackgroundColor(yourColor)
                        .setProgressText(yourText)
                        .setProgressTextColor(yourColor)
                        .setCancelText(yourText)
                        .setCancelTextColor(yourColor)
                        .setButtonTextColor(yourColor)
                        .setDimAmount(yourFloat)
                        .setFlip(true)
                        .setMaxSize(500)
                        .setPickTypes(EPickType.GALLERY, EPickType.CAMERA)
                        .setCameraButtonText(yourText)
                        .setGalleryButtonText(yourText)
                        .setIconGravity(Gravity.LEFT)
                        .setButtonOrientation(LinearLayoutCompat.VERTICAL)
                        .setSystemDialog(false)
                        .setGalleryIcon(yourIcon)
                        .setCameraIcon(yourIcon);*/
                PickImageDialog.build(new PickSetup())
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                //TODO: do what you have to...
                                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                                String image_name = "IMG" + timeStamp + ".jpg";
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                r.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                String  imagestring = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                Log.d("onPickResult: ", imagestring);
                                uploadProfilePic(image_name, imagestring);

                            }
                        })
                        .setOnPickCancel(new IPickCancel() {
                            @Override
                            public void onCancelClick() {
                                //TODO: do what you have to if user clicked cancel
                                //Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        }).show(getFragmentManager());
            }
        });


        // For DONORS Only
        MemberShip = view.findViewById(R.id.MemberShip);
        userName = view.findViewById(R.id.userName);
        userEmail = view.findViewById(R.id.userEmail);
        userBG = view.findViewById(R.id.userBG);
        donor_Layout = view.findViewById(R.id.donor_Layout);
        // For MEMBERS Only
        memberFatherName = view.findViewById(R.id.memberFatherName);
        memberName = view.findViewById(R.id.memberName);
        membersection = view.findViewById(R.id.membersection);
        memberdepartment = view.findViewById(R.id.memberdepartment);
        memberCNIC = view.findViewById(R.id.memberCNIC);
        memberAddress = view.findViewById(R.id.memberAddress);
        memberBG = view.findViewById(R.id.memberBG);
        memberEmail = view.findViewById(R.id.memberEmail);
        memberdesignation = view.findViewById(R.id.memberdesignation);
        ScrollView = view.findViewById(R.id.memberShip_Layout);


        if (role_id.equals("1")) {
            donor_Layout.setVisibility(View.VISIBLE);
            ScrollView.setVisibility(View.GONE);


        } else {

            donor_Layout.setVisibility(View.GONE);
            ScrollView.setVisibility(View.VISIBLE);
           // Toast.makeText(getContext(), " this" + role_id, Toast.LENGTH_SHORT).show();

        }

        userName.setText("NAME:" + " " + testHashMap2.get("Name"));
        userEmail.setText("Email Address:" + " " + testHashMap2.get("Email"));
        userBG.setText("Blood Group:" + " " + testHashMap2.get("Blood_Group"));
        memberFatherName.setText("Father Name:" + " " + testHashMap2.get("FatherName"));
        memberCNIC.setText(" CNIC:" + " " + testHashMap2.get("CNIC"));
        memberName.setText("NAME:" + " " + testHashMap2.get("Name"));
        membersection.setText("Section:" + " " + testHashMap2.get("Section"));
        memberdepartment.setText("Department:" + " " + testHashMap2.get("Department"));
        memberAddress.setText("Address:" + " " + testHashMap2.get("PostalAdd"));
        memberBG.setText("Blood Group:" + " " + testHashMap2.get("Blood_Group"));
        memberEmail.setText("Email Address:" + " " + testHashMap2.get("Email"));
        memberdesignation.setText("Designation:" + " " + testHashMap2.get("Designation"));


        return view;

    }


    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

        MemberShip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder insertMemberShip = new AlertDialog.Builder(getActivity());
                insertMemberShip.setTitle("Member Ship Form");
                insertMemberShip.setIcon(R.mipmap.ic_launcher_foreground);
                View inflatedView = LayoutInflater.from(getActivity()).inflate(R.layout.customalertbuilder, null);
                insertMemberShip.setView(inflatedView);
                fname = (EditText) inflatedView.findViewById(R.id.fname);
                cnic = inflatedView.findViewById(R.id.cnic);
                designation = (EditText) inflatedView.findViewById(R.id.designation);
                department = (EditText) inflatedView.findViewById(R.id.department);
                section = (EditText) inflatedView.findViewById(R.id.section);
                postalAdd = (EditText) inflatedView.findViewById(R.id.postalAdd);
                ice = (EditText) inflatedView.findViewById(R.id.ice);
                ref = (EditText) inflatedView.findViewById(R.id.ref);
                willpoint = (EditText) inflatedView.findViewById(R.id.willpoint);
                reason = (EditText) inflatedView.findViewById(R.id.reason);
                insertMemberShip.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (fname.getText().length() > 0
                                && cnic.getText().length() > 0
                                && designation.getText().length() > 0
                                && department.getText().length() > 0
                                && section.getText().length() > 0
                                && postalAdd.getText().length() > 0
                                && ice.getText().length() > 0
                                && reason.getText().length() > 0
                                && willpoint.getText().length() > 0
                                && ref.getText().length() > 0) {
                            donor_Layout.setVisibility(View.GONE);
                            ScrollView.setVisibility(View.VISIBLE);
                            //  Toast.makeText(getActivity(),fname.getText().toString(), Toast.LENGTH_SHORT).show();
                            //addMember(fname.getText().toString(),cnic.getText().toString(),designation.getText().toString(),department.getText().toString(),section.getText().toString(),postalAdd.getText().toString(),ice.getText().toString());
                            updateMember(fname.getText().toString(), cnic.getText().toString(), designation.getText().toString(), department.getText().toString(), section.getText().toString(), postalAdd.getText().toString(), ice.getText().toString(), reason.getText().toString(), willpoint.getText().toString(), ref.getText().toString());
                            Fragment frag = null;
                            frag = getActivity().getSupportFragmentManager().findFragmentByTag("frag_profile");
                            final android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.detach(frag);
                            ft.attach(frag);
                            ft.commit();
                            // REFRESHING HERE!!!!

                        } else {
                            Toast.makeText(getActivity(), "Please Enter Required Fields.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //Card View Properties and Opacity.
                insertMemberShip.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                insertMemberShip.setCancelable(false);
                insertMemberShip.show();
            }
        });
    }

    public void updateMember(final String fatherName, final String cnic, final String designation, final String department, final String section, final String postalAdd, final String ice, final String reason, final String willPoint, final String ref) {

        Map<String, String> newPost;
        newPost = new HashMap<>();
        newPost.put("id", userID);
        newPost.put("role_id", "2");
        newPost.put("designation", designation);
        newPost.put("department", department);
        newPost.put("father_name", fatherName);
        newPost.put("section", section);
        newPost.put("cnic", cnic);
        newPost.put("ice", ice);
        newPost.put("postal_address", postalAdd);
        newPost.put("reason", reason);
        newPost.put("refrence", ref);
        newPost.put("willpoint", willPoint);
        JSONObject mapObj = new JSONObject(newPost);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "http://webapi.lifesavers.org.pk/update_api.php";
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, mapObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Printing Response i.e SuccessFull Operation/ already Exist/ Parameter Are Not Allowed.
                            Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            // Printing Details Entered Here.
                          //  Toast.makeText(getActivity(), response.getString("users"), Toast.LENGTH_SHORT).show();

                            JSONObject obj = new JSONObject(response.getString("users"));

                           // Toast.makeText(getActivity(), "The String is" + obj.getString("role_id"), Toast.LENGTH_SHORT).show();
                          //  Toast.makeText(getActivity(), "No,The String is" + response.getJSONObject("users").getString("role_id"), Toast.LENGTH_SHORT).show();
                            // Toast.makeText(getActivity(), "M inside Check::"+" "+storename, Toast.LENGTH_SHORT).show();

                            new memberShip(getActivity(), storeID, obj.getString("role_id"), storename, storeemail, storemobile, storebg,
                                    obj.getString("father_name"), obj.getString("cnic"), obj.getString("designation"), obj.getString("department"),
                                    obj.getString("section"), obj.getString("postal_address"), obj.getString("ice"));



                        /*  data.put("id", response.getString("id"));
                            roleID= data.put("role_id", obj.getString("role_id"));
                            data.put("designation", obj.getString("designation"));
                            data.put("department", obj.getString("department"));
                            data.put("father_name", obj.getString("father_name"));
                            data.put("section", obj.getString("section"));
                            data.put("cnic", obj.getString("cnic"));
                            data.put("ice", obj.getString("ice"));
                            data.put("postalAdd", obj.getString("postal_address")); */


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                //Log.d("Jee",error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> newPost;
                newPost = new HashMap<>();
                newPost.put("id", userID);
                newPost.put("role_id", "2");
                newPost.put("designation", designation);
                newPost.put("department", department);
                newPost.put("father_name", fatherName);
                newPost.put("section", section);
                newPost.put("cnic", cnic);
                newPost.put("ice", ice);
                newPost.put("postal_address", postalAdd);
                newPost.put("reason", reason);
                newPost.put("refrence", ref);
                newPost.put("willpoint", willPoint);
                return newPost;
            }

        };


        queue.add(stringRequest);

    }

    /*private void addMember(String fatherName, String cnic, String designation,String department, String section, String postalAdd, String ice) {
        Map<String, String> newPost;
        newPost = new HashMap<>();
        *//*newPost.put("email",email.getText().toString());
        newPost.put("password",password.getText().toString());*//*
        JSONObject mapObj=new JSONObject(newPost);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url ="http://www.affordablesstore.pk/api/update_api.php";

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url,mapObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try{

                            Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(), response.getString("user"), Toast.LENGTH_SHORT).show();
                            //   Value {"id":40,"email":"NabeelEjaz@gmail.com","name":"NabeelEjaz","mobile":"O","blood_group":"658"}


                            JSONObject obj= new JSONObject(response.getString("user"));
                            HashMap<String, String> data = new HashMap<>();
                            data.put("id", obj.getString("id"));
                            data.put("Name", obj.getString("name"));
                            data.put("Email", obj.getString("email"));
                            data.put("Mobile", obj.getString("mobile"));
                            data.put("Blood_Group", obj.getString("blood_group"));
                        *//*    Context context= getContext();
                            Gson gson= new Gson();
                            String hashMapString = gson.toJson(data);
                            SharedPreferences sharedPref = context.getSharedPreferences(
                                    "com.example.nabeel.lifesavers", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("Data",  hashMapString) ;
                            editor.apply();*//*

     *//* SessionManagement sessionManager= new SessionManagement(context);
                            sessionManager.createLoginSession(obj.getString("id"),obj.getString("name"));
                            startActivity( new Intent(SigninActivity.this, Dashboard.class));
*//*
                        }catch (JSONException e){
                            Log.d("JEE", "onResponse: "+e );
                        }


                        //  startActivity(new Intent(SigninActivity.this, DashboardActivity.class));


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error" + error.getMessage(), Toast.LENGTH_SHORT).show();

                Log.d("JEEE", "onErrorResponse: "+ error.getMessage());
            }
        });


        queue.add(stringRequest);




    }
}
*/




    public void uploadProfilePic(final String imageName, final String imageEncodedString){
    RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
     String url = "http://webapi.lifesavers.org.pk/imageAPINew.php";
      StringRequest stringRequest= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
              Log.d("Image Response URL ", response);
              SharedPreferences sharedPref = getActivity().getSharedPreferences(
                      "profilePicture", Context.MODE_PRIVATE);
              SharedPreferences.Editor editor = sharedPref.edit();
              editor.remove("URL");
              editor.putString("URL",response);
              editor.apply();
              Fragment frag = null;
              frag = getActivity().getSupportFragmentManager().findFragmentByTag("frag_profile");
              final android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
              ft.detach(frag);
              ft.attach(frag);
              ft.commit();

          }
      }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {

              error.getMessage();
              error.printStackTrace();
          }
      }) {

          @Override
          public String getBodyContentType() {
              return "application/x-www-form-urlencoded; charset=UTF-8";
          }

          @Override
          protected Map<String, String> getParams() throws AuthFailureError {
              Map<String, String> params = new HashMap<String, String>();
              params.put("image_name", imageName);
              params.put("imagestring", imageEncodedString);
              return params;
          }

      };
      requestQueue.add(stringRequest);
    }




}