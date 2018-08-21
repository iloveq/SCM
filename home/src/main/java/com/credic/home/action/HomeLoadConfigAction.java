package com.credic.home.action;

import android.content.Context;

import com.woaiqw.scm_annotation.annotion.Action;
import com.woaiqw.scm_api.ScAction;
import com.woaiqw.scm_api.ScCallback;

/**
 * Created by haoran on 2018/8/21.
 */
@Action(name = "LoadConfig")
public class HomeLoadConfigAction implements ScAction {
    @Override
    public void invoke(Context context, String param, ScCallback callback) {

        loadConfig();
        callback.onCallback(true, "加载了配置", "HHH");

    }

    private void loadConfig() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
