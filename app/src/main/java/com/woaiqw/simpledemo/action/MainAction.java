package com.woaiqw.simpledemo.action;

import android.content.Context;
import android.content.Intent;

import com.woaiqw.scm_annotation.annotion.Action;
import com.woaiqw.scm_api.ScAction;
import com.woaiqw.scm_api.ScCallback;

/**
 * Created by haoran on 2018/8/21.
 */
@Action(name = "CloseMainActivityAction")
public class MainAction implements ScAction {
    @Override
    public void invoke(Context context, String param, ScCallback callback) {
        context.sendBroadcast(new Intent("finish"));
        callback.onCallback(true, "MainAction：close - MainActivity", "");
    }
}
