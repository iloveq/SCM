package com.woaiqw.simpledemo;

import android.app.Application;

import com.woaiqw.scm_annotation.annotation.Modules;
import com.woaiqw.scm_api.SCM;

/**
 * Created by haoran on 2018/8/21.
 */
@Modules(names = {"Main", "Home"})
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SCM.get().scanningSCMTable(App.class);
    }
}
