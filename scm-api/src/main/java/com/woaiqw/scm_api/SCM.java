package com.woaiqw.scm_api;

import android.util.Log;

import com.woaiqw.scm_api.utils.Constants;

import java.lang.reflect.Field;

import static com.woaiqw.scm_api.utils.Constants.PACKAGE_OF_GENERATE_FILE;

/**
 * Created by haoran on 2018/8/16.
 */

public class SCM {

    private static volatile SCM instance = null;

    private SCM() {
        initServers();
    }

    private void initServers() {

    }

    public static SCM initialize() {
        if (instance == null) {
            synchronized (SCM.class) {
                if (instance == null)
                    instance = new SCM();
            }
        }
        return instance;
    }

    public void scanningSCMTable() {
        try {
            Class clazz = Class.forName(PACKAGE_OF_GENERATE_FILE + "."+"SCMTable");
            Field[] fields = clazz.getFields();
            for (Field field : fields) {
                System.out.println("111"+field.getName() + " " + field.getInt(clazz));
            }
        } catch (Exception e) {
            Log.e(Constants.SCM, e.getMessage());
        }

    }

}
