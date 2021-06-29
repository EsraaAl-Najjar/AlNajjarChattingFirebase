package com.alnajjarchattingfirebase.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

public class OreoNotification extends ContextWrapper {
private static final String CHANNEL_ID ="com.alnajjarchattingfirebase";
private static final String CHANNEL_NAME ="chatapp";
private NotificationManager notifyManager;

    public OreoNotification(Context base) {
        super( base );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
        }
    }
    @TargetApi( Build.VERSION_CODES.O )
    private void createChannel() {

        NotificationChannel notificationChannel = new NotificationChannel( CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT );
        notificationChannel.enableLights( false );
        notificationChannel.enableVibration( true );
        notificationChannel.setLockscreenVisibility( Notification.VISIBILITY_PRIVATE );
        getManager().createNotificationChannel( notificationChannel);
    }
    public NotificationManager getManager(){
        if (notifyManager == null){
            notifyManager = (NotificationManager)getSystemService( Context.NOTIFICATION_SERVICE );
        }
        return notifyManager;
    }

    @TargetApi( Build.VERSION_CODES.O )
    public  Notification.Builder getOreoNotification(String title, String body, PendingIntent pendingIntent, Uri soundUri, String icon){
        return  new Notification.Builder( getApplicationContext(), CHANNEL_ID )
                .setContentIntent( pendingIntent )
                .setContentTitle( title )
                .setContentText( body )
                .setSmallIcon( Integer.parseInt( icon ) )
                .setSound( soundUri )
                .setAutoCancel( true );

    }
}
