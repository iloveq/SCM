package com.credic.home.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.credic.home.R;
import com.woaiqw.scm_api.SCM;
import com.woaiqw.scm_api.ScCallback;
import com.woaiqw.scm_api.utils.Constants;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {


/****************************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_home);

        findViewById(R.id.home_tv).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        try {
            SCM.get().req(HomeActivity.this, "CloseMainActivityAction", new ScCallback() {
                @Override
                public void onCallback(boolean b, final String data, String tag) {
                    if (b)
                        Toast.makeText(HomeActivity.this, data, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e(Constants.SCM, e.getMessage());
        }
    }
}