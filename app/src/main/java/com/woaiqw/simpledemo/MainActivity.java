package com.woaiqw.simpledemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.woaiqw.scm_api.SCM;
import com.woaiqw.scm_api.ScCallback;
import com.woaiqw.scm_api.utils.Constants;

import java.util.concurrent.CountDownLatch;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    CountDownLatch countDownLatch = new CountDownLatch(2) {
        @Override
        public void await() throws InterruptedException {
            super.await();
            System.out.println(Thread.currentThread().getName() + " count down is ok");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_load_config).setOnClickListener(this);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            SCM.get().req(this, "HomeEntry", new ScCallback() {
                @Override
                public void onCallback(boolean b, String data, String tag) {
                    if (b)
                        Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e(Constants.SCM, e.getMessage());
        }


    }

    @Override
    public void onClick(View v) {

        try {
            SCM.get().req(MainActivity.this, "LoadConfig", new ScCallback() {
                @Override
                public void onCallback(boolean b, String data, String tag) {
                    if (b)
                        Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e(Constants.SCM, e.getMessage());
        }

    }

}
