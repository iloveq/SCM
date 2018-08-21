package com.credic.home.action;

import android.content.Context;
import android.content.Intent;

import com.credic.home.view.HomeActivity;
import com.woaiqw.scm_annotation.annotion.Action;
import com.woaiqw.scm_api.ScAction;
import com.woaiqw.scm_api.ScCallback;


@Action(name = "HomeEntry")
public class HomeEntryAction implements ScAction {

    @Override
    public void invoke(Context context, String param, ScCallback callback) {
        Intent intent = new Intent();
        intent.setClass(context, HomeActivity.class);
        context.startActivity(intent);
        callback.onCallback(true, "HomeEntryAction:我把HomeActivity打开了", "");
    }
}
