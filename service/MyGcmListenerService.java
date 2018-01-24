package com.moocall.moocall.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import com.google.android.gms.gcm.GcmListenerService;
import com.moocall.moocall.C0530R;
import com.moocall.moocall.MyMoocallActivity;
import com.moocall.moocall.PostDetailsActivity;
import com.moocall.moocall.SocialNetworkActivity;
import com.moocall.moocall.util.StorageContainer;
import com.moocall.moocall.util.Utils;
import io.intercom.android.sdk.models.Part;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.lang3.time.DateUtils;

public class MyGcmListenerService extends GcmListenerService {
    private static final String TAG = "MyGcmListenerService";

    public static class NotificationID {
        private static final AtomicInteger f23c = new AtomicInteger(0);

        public static int getID() {
            return f23c.incrementAndGet();
        }
    }

    public void onMessageReceived(String from, Bundle data) {
        Log.d(TAG, "data: " + data);
        final String message = data.getString("message");
        final String time = Utils.calculateTime(data.getString("time"), "yyyy-MM-dd HH:mm");
        final String device = data.getString("device_code");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
        if (from.startsWith("/topics/")) {
            StorageContainer.wakeApp(this);
        } else {
            StorageContainer.wakeApp(this);
        }
        if (device != null) {
            final WakeLock wl = ((PowerManager) getSystemService("power")).newWakeLock(1, TAG);
            wl.acquire();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                public void run() {
                    MyGcmListenerService.this.sendNotification(message, device, time, wl);
                }
            }, DateUtils.MILLIS_PER_MINUTE);
            return;
        }
        String title = "Moocall";
        if (data.getString("title") != null) {
            title = data.getString("title");
        }
        sendSocialNotification(message, title, time, Integer.valueOf(data.getInt(Part.POST_MESSAGE_STYLE)));
    }

    private void sendNotification(String message, String device, String time, WakeLock wl) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean enableNotification = Boolean.valueOf(sharedPreferences.getBoolean("enableNotification", true));
        Boolean signout = Boolean.valueOf(sharedPreferences.getBoolean("signout", false));
        if (enableNotification.booleanValue() && !signout.booleanValue()) {
            String ringtone = sharedPreferences.getString("ringtone", null);
            Uri defaultSoundUri = Uri.parse("android.resource://com.moocall.moocall/raw/cowsound_long_long");
            if (!(ringtone == null || ringtone.equals("Moocall Moo"))) {
                RingtoneManager manager = new RingtoneManager(this);
                manager.setType(1);
                Cursor cursor = manager.getCursor();
                while (cursor.moveToNext()) {
                    if (ringtone.equals(cursor.getString(1))) {
                        defaultSoundUri = manager.getRingtoneUri(cursor.getPosition());
                    }
                }
            }
            Integer notId = Integer.valueOf(NotificationID.getID());
            Intent intent = new Intent(this, MyMoocallActivity.class);
            intent.putExtra("notification", true);
            intent.addFlags(67108864);
            ((NotificationManager) getSystemService("notification")).notify(notId.intValue(), new Builder(this).setSmallIcon(C0530R.drawable.moocall_logo).setLargeIcon(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), C0530R.mipmap.ic_launcher), 96, 96, false)).setContentTitle(device).setContentText(message).setSubText(time).setAutoCancel(true).setSound(defaultSoundUri).setContentIntent(PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 1073741824)).build());
        }
        wl.release();
    }

    private void sendSocialNotification(String message, String title, String time, Integer postId) {
        Intent intent;
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(2);
        Integer notId = Integer.valueOf(NotificationID.getID());
        if (postId.intValue() > 0) {
            intent = new Intent(this, PostDetailsActivity.class);
            intent.putExtra("post-id", postId);
        } else {
            intent = new Intent(this, SocialNetworkActivity.class);
        }
        intent.addFlags(67108864);
        ((NotificationManager) getSystemService("notification")).notify(notId.intValue(), new Builder(this).setSmallIcon(C0530R.drawable.moocall_logo).setLargeIcon(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), C0530R.mipmap.ic_launcher), 96, 96, false)).setContentTitle(title).setContentText(message).setSubText(time).setAutoCancel(true).setSound(defaultSoundUri).setContentIntent(PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 1073741824)).build());
    }
}
