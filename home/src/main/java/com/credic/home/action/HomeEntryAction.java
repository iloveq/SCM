package com.credic.home.action;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.credic.home.view.HomeActivity;
import com.woaiqw.annotation.Action;
import com.woaiqw.scm_api.ScAction;
import com.woaiqw.scm_api.ScCallback;


@Action(name = "HomeEntry", module = "Home")
public class HomeEntryAction implements ScAction {

    @Override
    public void invoke(Context context, String param, ScCallback callback) {
        Intent intent = new Intent();
        Log.d("111", param);
        intent.setClass(context, HomeActivity.class);
        context.startActivity(intent);
        callback.onCallback(true, "HomeEntryAction:我把HomeActivity打开了", "");
    }
}
