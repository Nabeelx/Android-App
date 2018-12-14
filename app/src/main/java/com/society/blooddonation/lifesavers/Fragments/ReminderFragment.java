package com.society.blooddonation.lifesavers.Fragments;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.society.blooddonation.lifesavers.Dashboard;
import com.society.blooddonation.lifesavers.DashboardActivity;
import com.society.blooddonation.lifesavers.R;
import com.society.blooddonation.lifesavers.utils.BackgroundService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ReminderFragment extends Fragment {

    final static int req1=1;
    public String a = "0"; // initialize this globally at the top of your class.
    EditText textInputlayout,donationdate;
    TextView donateAlert;
    private Calendar myCalendar;
    BroadcastReceiver receiver;
    private PendingIntent pendingIntent;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;
    String newDate;

    android.support.v7.widget.AppCompatButton btn_reminder;
    public ReminderFragment() {

    }

    public static ReminderFragment newInstance() {
        ReminderFragment fragment = new ReminderFragment();
        return fragment;
    }

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminder, container, false);


    }

    @Override
    public void onResume() {
        super.onResume();
         IntentFilter intentFilter=new IntentFilter("com.example.nabeel.lifesavers.utils.ResponseBroadcastReceiver");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver ,intentFilter);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        myCalendar = Calendar.getInstance();
       // textInputlayout=view.findViewById(R.id.donationdate);
        donateAlert=view.findViewById(R.id.donateAlert);
        btn_reminder=view.findViewById(R.id.btn_reminder);
        donationdate=view.findViewById(R.id.donationdate);
        //textInputlayout.setEnabled(false);
        donationdate.setInputType(InputType.TYPE_NULL);
        donationdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    final DatePickerDialog mDatePicker=
                            new DatePickerDialog(getActivity(),
                                    new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                            String myFormat = "MMM dd, yyyy";
                                            SimpleDateFormat sdformat = new SimpleDateFormat(myFormat, Locale.US);



                                            if((year <  myCalendar.get(Calendar.YEAR)
                                                    ||  year == myCalendar.get(Calendar.YEAR))
                                                    &&( month < myCalendar.get(Calendar.MONTH)
                                                    ||  month == myCalendar.get(Calendar.MONTH))
                                                    && (dayOfMonth <  myCalendar.get(Calendar.DAY_OF_MONTH)
                                                    ||  dayOfMonth == myCalendar.get(Calendar.DAY_OF_MONTH))){
                                                donationdate.setText(dayOfMonth + "-" + (month + 1) + "-" + year);

                                                int newMonth=(((month)+3)+1);
                                                if(newMonth>12){
                                                    newMonth=1;
                                                    newDate =(dayOfMonth + "-" + newMonth + "-" + (year + 1));
                                                }
                                                else {
                                                    newDate =dayOfMonth + "-" + ((month + 1)+3) + "-" + year;
                                                }

                                                Toast.makeText(getActivity(), newDate, Toast.LENGTH_LONG).show();

                                            }else {
                                               donationdate.setError("Please Choose Correct Date");
                                            }

                                        }},

                                    myCalendar.get(Calendar.YEAR),
                                    myCalendar.get(Calendar.MONTH),
                                    myCalendar.get(Calendar.DAY_OF_MONTH));
                    mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                    mDatePicker.show();
                }
            });


        //create Intent

        btn_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String y=String.valueOf(myCalendar.get(Calendar.YEAR));
                String m=String.valueOf(myCalendar.get(Calendar.MONTH)+1);
                String d=String.valueOf(myCalendar.get(Calendar.DAY_OF_MONTH) );
                String myDate=d + "-" + m + "-" + y; //dayOfMonth + "-" + (month + 1) + "-" + year

                SharedPreferences sharedPref = getActivity().getSharedPreferences(
                        "com.lifesavers", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                //editor.remove("Data");
                editor.putString("Date",  newDate);
                editor.commit();
                Toast.makeText(getActivity(), "Your Reminder Has Been Saved.", Toast.LENGTH_SHORT).show();
                Intent serviceIntent= new Intent(getActivity(), BackgroundService.class ) ;
               // serviceIntent.putExtra("date",myDate);
                getActivity().startService(serviceIntent);
                startActivity(new Intent(getActivity(),Dashboard.class));

                // THIS WORK HERE WILL BE DONE IN THE Dashboard Activity.


             //   PendingIntent toastAlarmIntent = PendingIntent.getService(getActivity().getApplicationContext(), 0, toastIntent,PendingIntent.FLAG_UPDATE_CURRENT);

/*

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity().getApplicationContext())
                        .setContentTitle("Wake Up")
                        .setContentText("Wake Up")
                        .setAutoCancel(true)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(((BitmapDrawable) getActivity().getApplicationContext().getDrawable(R.mipmap.ic_launcher)).getBitmap())
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
         Intent mNotificationIntent = new Intent(getActivity().getApplicationContext(), MyNotificationPublisher.class);
         PendingIntent mContent = PendingIntent.getActivity(getActivity().getApplicationContext(),0,
         mNotificationIntent,PendingIntent.FLAG_CANCEL_CURRENT);
                mNotificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, 1);
                mNotificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, "UTHO BHAI ");
                builder.setContentIntent(mContent);
                Notification notification = builder.build();
*/
 

             //   scheduleNotification(getActivity(),1, 1);
 //
/*

                Intent toastIntent= new Intent(getActivity().getApplicationContext(),BackgroundService.class);
                PendingIntent toastAlarmIntent = PendingIntent.getService(getActivity().getApplicationContext(), 0, toastIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                long startTime=System.currentTimeMillis();
                AlarmManager backupAlarmMgr=(AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
                backupAlarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,startTime,AlarmManager.INTERVAL_FIFTEEN_MINUTES,toastAlarmIntent);
*/

            }
        });



       Calendar cal = Calendar.getInstance();
        // Get Last Donation Date then calculate next three month date, store result then pass it as a parameter.
        cal.set(2018, 9, 1, 16, 45, 0);
     //   setAlarm(cal);
        if(a.equals("0"))
        {
            // ................... No Alarm Message Here........................!!!
        }
    }










}
