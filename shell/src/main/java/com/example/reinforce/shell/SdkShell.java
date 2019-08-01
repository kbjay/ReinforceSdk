package com.example.reinforce.shell;

import android.content.Context;

import dalvik.system.PathClassLoader;

/**
 * @anthor kb_jay
 * create at 2019-08-01 13:15
 */
public class SdkShell {
    static {
        System.loadLibrary("reinforce");
    }

    public static native void init(Context context, PathClassLoader loader);
}
