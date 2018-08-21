package com.woaiqw.simpledemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.woaiqw.scm_api.AppSCMTable;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppSCMTable appSCMTable = new AppSCMTable();


    }
}
