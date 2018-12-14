package com.society.blooddonation.lifesavers.utils;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.content.Context;
import android.widget.Toast;

import com.society.blooddonation.lifesavers.Dashboard;
import com.society.blooddonation.lifesavers.R;

import java.util.Calendar;

public class BackgroundService extends Service {
    private PendingIntent pendingIntent;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;
    Calendar  myCalendar = Calendar.getInstance();
    String y=String.valueOf(myCalendar.get(Calendar.YEAR));
    String m=String.valueOf(myCalendar.get(Calendar.MONTH)+1);
    String d=String.valueOf(myCalendar.get(Calendar.DAY_OF_MONTH) );
    String Date=d + "-" + m + "-" + y;
    private NotificationManager notifManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        Log.d("BackGround :","RUNNING");
        SharedPreferences prefs= this.getSharedPreferences(
                "com.lifesavers", this.MODE_PRIVATE);
        String storedDate = prefs.getString("Date", "");

       // Toast.makeText(getActivity(), editor.toString(), Toast.LENGTH_SHORT).show();
      //  Toast.makeText(this, "Date Stored In Pref"+storedDate, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "Date from system"+Date , Toast.LENGTH_SHORT).show();
        if (storedDate.equals(Date)){
          //  Toast.makeText(this, "yes", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor=prefs.edit();
            editor.remove("Date");
            editor.commit();

    /**
     *
     *
     * DELETED THIS CODE BECAUSE IT WONT RUN ON ALL DEVICES: IF YOU DON'T RECIEVE
     * NOTIFICATION UNCOMMENT THIS CODE AND REMOVE public void createNotification(String aMessage)
     *
     * */
           /*



           Intent myIntent=new Intent(this,Dashboard.class);
            //Initialize PendingIntent
            pendingIntent = PendingIntent.getActivity(this, 0, myIntent, 0);
            //Initialize NotificationManager using Context.NOTIFICATION_SERVICE
            notificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle("Life Savers").setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pendingIntent)
                    .setContentText("It's been A 3 Month You Haven't Donate Blood,You Can Now Donate Blood.");
            //add sound
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notificationBuilder.setSound(uri);
            //vibrate
            long[] va = {500,1000};
            notificationBuilder.setVibrate(va);
            notificationManager.notify(1, notificationBuilder.build());*/
            createNotification("You Can Now Donate Blood");
            NotificationCompat.Builder mBuilder= new NotificationCompat.Builder(this);

            Intent i= new Intent(this, BackgroundService.class);
            stopService(i);

        }else {

            Toast.makeText(this, "You Are 3 Months Away From Donation", Toast.LENGTH_SHORT).show();
        }

            return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }


    public void createNotification(String aMessage) {
        final int NOTIFY_ID = 1002;

        // There are hardcoding only for show it's just strings
        String name = "my_package_channel";
        String id = "my_package_channel_1"; // The user-visible name of the channel.
        String description = "my_package_first_channel"; // The user-visible description of the channel.

        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;

        if (notifManager == null) {
            notifManager =
                    (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, name, importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(this, id);

            intent = new Intent(this, Dashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            builder.setContentTitle(aMessage)  // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder) // required
                    .setContentText(this.getString(R.string.app_name))  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        } else {

            builder = new NotificationCompat.Builder(this);

            intent = new Intent(this, Dashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            builder.setContentTitle(aMessage)                           // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder) // required
                    .setContentText(this.getString(R.string.app_name))  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        } // else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);
    }

}
