package com.mayeosurge.questmaster;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by eoin2 on 22/10/2016.
 */
public class NotifPub extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    public void onReceive(Context c, Intent i){
        NotificationManager nm = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification n = i.getParcelableExtra(NOTIFICATION);
        int id = i.getIntExtra(NOTIFICATION_ID, 0);
        nm.notify(id, n);
    }
}
