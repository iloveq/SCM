package com.credic.home.action;

import android.content.Context;

import com.credic.home.data.DataProvider;
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
        //模拟加载网络数据
        DataProvider.getConfig(callback);

    }

}
