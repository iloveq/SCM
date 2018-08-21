package com.woaiqw.simpledemo;

import android.app.Application;

import com.woaiqw.scm_api.SCM;

/**
 * Created by haoran on 2018/8/21.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SCM.get().scanningSCMTable(new String[]{"Main", "Home"});
    }
}
