package com.woaiqw.scm_api;

import android.content.Context;

/**
 * Created by haoran on 2018/8/20.
 */

public interface ScAction {
    void invoke(Context context, String param, ScCallback callback);
}
