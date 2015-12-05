package com.android.ifmo_android_2015.loafer;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ruslanabdulhalikov on 06.12.15.
 */
public class DataBaseInitService extends IntentService {

    public DataBaseInitService() {
        super("DataBaseInitService");
    }

    public void onCreate() {
        super.onCreate();
        Log.d("DBService", "onCreate");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            URL url = new URL(intent.getStringExtra("URL"));
            /**
             * Здесь вызываем парсер по урлу, который мы передали, получим list ивентов и добавим в базу
             * через бродкаст сообщим что все тру, мы запихнули или нет
             *
             */
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public void onDestroy() {
        super.onDestroy();
        Log.d("DBService", "onDestroy");
    }
}
