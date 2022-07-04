package com.example.appbanhang.service;

import android.widget.RemoteViews;

import androidx.annotation.NonNull;

import com.example.appbanhang.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagerRecevier extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
    }

    private RemoteViews customView(String title, String body){
        RemoteViews remoteViews= new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.title_noti,title);
        remoteViews.setTextViewText(R.id.body_noti,body);
        remoteViews.setImageViewResource(R.id.imgnoti,R.drawable.ic_email);
        return  remoteViews;
    }
}
