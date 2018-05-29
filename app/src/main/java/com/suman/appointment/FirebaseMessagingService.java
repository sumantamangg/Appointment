package com.suman.appointment;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{

    public void onMessageReceived(RemoteMessage remoteMessage){
        super.onMessageReceived(remoteMessage);
        String notification_title = remoteMessage.getNotification().getTitle();
        String notification_message = remoteMessage.getNotification().getBody();

        String click_action = remoteMessage.getNotification().getClickAction();
        String from_user_id = remoteMessage.getData().get("from_user_id");
        String partyname = remoteMessage.getData().get("partyname");
        String heading = remoteMessage.getData().get("heading");
        String agenda = remoteMessage.getData().get("agenda");
        String reqdate = remoteMessage.getData().get("reqdate");
        String fd = remoteMessage.getData().get("fd");
        String notification_type = remoteMessage.getData().get("noti_type");
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(notification_title)
                    .setContentText(notification_message);

        Intent resultIntent = new Intent(click_action);
        resultIntent.putExtra("from_user_id",from_user_id);
        resultIntent.putExtra("partyname",partyname);
        resultIntent.putExtra("heading",heading);
        resultIntent.putExtra("agenda",agenda);
        resultIntent.putExtra("reqdate",reqdate);
        resultIntent.putExtra("fd",fd);
        resultIntent.putExtra("noti_type",notification_type);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        int mNotificationId = (int) System.currentTimeMillis();
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }
}
