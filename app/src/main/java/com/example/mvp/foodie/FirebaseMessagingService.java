package com.example.mvp.foodie;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import static com.example.mvp.foodie.UtilHelper.POST_ID;
import static com.example.mvp.foodie.UtilHelper.REQUEST_CODE;
import static com.example.mvp.foodie.UtilHelper.USER_ID;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String messageTitle = remoteMessage.getData().get("title");
        String messageBody = remoteMessage.getData().get("body");
        String click_action = remoteMessage.getData().get("click_action");
        String postID = remoteMessage.getData().get("post_id");
        String postOwnerID = remoteMessage.getData().get("post_owner_id");
        String requestCode = remoteMessage.getData().get("request_code");
        String userID = remoteMessage.getData().get("user_id");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            CharSequence name = getString(R.string.default_notification_channel_id);
            String description = getString(R.string.default_notification_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel mChannel = new NotificationChannel(getString(R.string.default_notification_channel_id), name, importance);
            mChannel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) getSystemService(
                    NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mChannel);
            }
        }


        Intent resultIntent = null;

        if (postID != null && postOwnerID != null) {
            resultIntent = new Intent(click_action);
//            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            resultIntent.putExtra(POST_ID, postID);
            resultIntent.putExtra(USER_ID, postOwnerID);
        } else if (requestCode != null && userID != null) {
            resultIntent = new Intent(click_action);
//            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            int resultCodeInt = Integer.parseInt(requestCode);
            resultIntent.putExtra(REQUEST_CODE, resultCodeInt);
            resultIntent.putExtra(USER_ID, userID);
        } else if (userID != null) {
            resultIntent = new Intent(click_action);
//            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            resultIntent.putExtra(USER_ID, userID);
        }


        if (resultIntent != null) {
            PendingIntent resultPendingIntent = PendingIntent.getActivity(
                    this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT
            );

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle(messageTitle)
                    .setContentText(messageBody)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);


            mBuilder.setContentIntent(resultPendingIntent);


            int mNotificationId = (int) System.currentTimeMillis();
            NotificationManager mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyManager.notify(mNotificationId, mBuilder.build());
        }
    }


}
