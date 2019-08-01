package com.example.reinforce.aar;

import android.app.Application;
import android.content.Context;

import com.example.reinforce.shell.SdkShell;

import dalvik.system.PathClassLoader;

/**
 * @anthor kb_jay
 * create at 2019-08-01 13:12
 */
public class MyApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        SdkShell.init(base, (PathClassLoader) base.getClassLoader());
    }
}
