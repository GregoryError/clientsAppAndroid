package org.success.isp.utils.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.success.isp.R;
import org.success.isp.views.MainActivity;

public class AppFirebaseMessaging extends FirebaseMessagingService {

    private static int count = 0;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
//        Log.e("newToken", s);
        getSharedPreferences("success_prefs", Context.MODE_PRIVATE).edit().putString("fb", s).apply();
        getSharedPreferences("success_prefs", Context.MODE_PRIVATE).edit().putBoolean("newToken", true).apply();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
       // super.onMessageReceived(remoteMessage);
        try {
            sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences("success_prefs", Context.MODE_PRIVATE).getString("fb", "empty");
    }

    private void sendNotification(String title, String messageBody) {

//        Intent intent = new Intent("com.example.NOTIFICATION_ACTION");
//        intent.putExtra("pushnotification", messageBody);
//        intent.putExtra("pushnotification_title", title);
//        sendBroadcast(intent);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        //  you can use your launcher Activity,But if the Activity you used here is not launcher Activty than its not work when App is in background.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add Any key-value to pass extras to intent
        intent.putExtra("pushnotification", messageBody);
        intent.putExtra("pushnotification_title", title);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //For Android Version Orio and greater than orio.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel("Sesame", "Sesame", importance);
            mChannel.setDescription(messageBody);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();
            mChannel.setSound(defaultSoundUri, att);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            mNotifyManager.createNotificationChannel(mChannel);

        }

        //For Android Version lower than oreo.
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "Seasame");
        mBuilder.setContentTitle(title)
                .setContentText(messageBody)
                .setSmallIcon(R.drawable.logo_circle)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo_circle))
                .setAutoCancel(true)
                .setSound(defaultSoundUri, AudioManager.STREAM_NOTIFICATION)
                .setColor(Color.parseColor("#FFD600"))
                .setContentIntent(pendingIntent)
                .setChannelId("Sesame")
                .setPriority(NotificationCompat.PRIORITY_LOW);

        mNotifyManager.notify(count, mBuilder.build());
        count++;
    }
}


