package com.woaiqw.simpledemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.credic.common.utils.WeakHandler;
import com.woaiqw.scm_api.SCM;
import com.woaiqw.scm_api.ScCallback;
import com.woaiqw.scm_api.utils.Constants;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private WeakHandler h = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String s = (String) msg.obj;
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            return false;
        }
    });

    private Runnable entryHomeActivityTask = new Runnable() {
        @Override
        public void run() {
            try {
                SCM.get().req(MainActivity.this, "HomeEntry", new ScCallback() {
                    @Override
                    public void onCallback(boolean b, final String data, String tag) {
                        if (b)
                            Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.e(Constants.SCM, e.getMessage());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_load_config).setOnClickListener(this);
        h.postDelayed(entryHomeActivityTask, 3000);
    }


    @Override
    public void onClick(View v) {

        try {
            SCM.get().req(MainActivity.this, "LoadConfig", new ScCallback() {
                @Override
                public void onCallback(boolean b, String data, String tag) {
                    if (b) {
                        Message obtain = Message.obtain();
                        obtain.obj = data;
                        h.sendMessage(obtain);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(Constants.SCM, e.getMessage());
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        h.removeCallbacksAndMessages(null);
    }
}
