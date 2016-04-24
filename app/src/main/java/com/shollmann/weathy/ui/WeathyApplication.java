package com.shollmann.weathy.ui;

import android.app.Application;

public class WeathyApplication extends Application {
    private static WeathyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static WeathyApplication getApplication() {
        return instance;
    }
}
