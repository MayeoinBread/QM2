package com.mayeosurge.questmaster;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class Notifiers {

    public static void scheduleNotification(Context c, Notification n, int d){
        Intent notifIntent = new Intent(c, NotifPub.class);
        notifIntent.putExtra(NotifPub.NOTIFICATION_ID, 1);
        notifIntent.putExtra(NotifPub.NOTIFICATION, n);
        PendingIntent pi = PendingIntent.getBroadcast(c, 0, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + d;
        AlarmManager am = (AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pi);
    }

    public static Notification getNotification(Context c, String content){
        Notification.Builder b = new Notification.Builder(c);
        //TODO Can't set intent to ActivityQuests.class as Hero needs to be passed in also
        PendingIntent pi = PendingIntent.getActivity(c, 0, new Intent(c, GamePage.class), 0);
        b.setContentTitle("QM Notification")
                .setContentText(content)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.hero_blue);
        return b.build();

    }
}
