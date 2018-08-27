package com.woaiqw.simpledemo;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.credic.common.utils.WeakHandler;
import com.woaiqw.scm_api.SCM;
import com.woaiqw.scm_api.ScCallback;
import com.woaiqw.scm_api.utils.Constants;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private WeakHandler h = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String s = (String) msg.obj;
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            tvLoadConfig.setText(s);
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
    private TextView tvLoadConfig, tvJumpPage;

    BroadcastReceiver b = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            String action = intent.getAction();
            if (action.equals("finish")) {
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvLoadConfig = findViewById(R.id.tv_load_config);
        tvJumpPage = findViewById(R.id.tv_jump_page);
        tvLoadConfig.setOnClickListener(this);
        tvJumpPage.setOnClickListener(this);
        registerReceiver(b, new IntentFilter("finish"));
    }

    @Override
    protected void onResume() {
        final long time = SystemClock.uptimeMillis();
        super.onResume();
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                // on Measure() -> onDraw() 耗时
                Log.i(MainActivity.this.getClass().getSimpleName(), "onCreate -> idle : " + (SystemClock.uptimeMillis() - time));
                return false;
            }
        });
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.tv_load_config) {
            //app module 使用用 home module 获取配置
            loadConfigByHomeModule();
        } else if (v.getId() == R.id.tv_jump_page) {
            //3s 后进入 home 进程的 HomeActivity
            h.postDelayed(entryHomeActivityTask, 3000);
        }


    }

    private void loadConfigByHomeModule() {
        try {
            SCM.get().req(MainActivity.this, "LoadConfig", new ScCallback() {
                @Override
                public void onCallback(boolean b, String data, String tag) {
                    if (b) {
                        Message obtain = Message.obtain();
                        obtain.obj = data;
                        if (h != null) {
                            h.sendMessage(obtain);
                        } else {
                            Toast.makeText(MainActivity.this, "WeakHandler has been Gc", Toast.LENGTH_SHORT).show();
                        }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(b);
    }

    /*public static void closeSelf(Context context) {
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            assert am != null;
            List<ActivityManager.RunningAppProcessInfo> listOfProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo process : listOfProcesses) {
                if (process.processName.contains("com.woaiqw.simpledemo.MainActivity")) {
                    am.killBackgroundProcesses(process.processName);
                    break;
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }*/
}
