package com.woaiqw.scm_api;

import android.content.Context;
import android.util.Log;

import com.woaiqw.scm_api.utils.Constants;

import java.lang.reflect.Field;
import java.util.HashMap;

import static com.woaiqw.scm_api.utils.Constants.PACKAGE_OF_GENERATE_FILE;

/**
 * Created by haoran on 2018/8/16.
 */

public class SCM {

    private static volatile SCM instance = null;
    private HashMap<String, ScAction> actionMap = new HashMap<>();
    private boolean isReady;

    private SCM() {
    }

    public static SCM get() {
        if (instance == null) {
            synchronized (SCM.class) {
                if (instance == null)
                    instance = new SCM();
            }
        }
        return instance;
    }

    public void scanningSCMTable(String[] moduleNames) {
        for (String moduleName : moduleNames) {
            try {
                Class clazz = Class.forName(PACKAGE_OF_GENERATE_FILE + "." + moduleName + "SCMTable");
                Field[] fields = clazz.getFields();
                for (Field field : fields) {
                    System.out.println("111" + "action:" + field.getName() + "value:" + field.get(field.getName()));
                    String name = field.getName();
                    String path = (String) field.get(name);
                    ScAction sca = (ScAction) Class.forName(path).newInstance();
                    actionMap.put(name, sca);
                }
            } catch (Exception e) {
                Log.e(Constants.SCM, e.getMessage());
            }
        }
        isReady = true;
    }


    public void req(Context context,String action,ScCallback callback) throws Exception{
        req(context,action,null,callback);
    }


    /**
     * 请求服务
     *
     * @param actionName 请求的服务名称
     * @param param      携带参数，json格式的string
     * @param callback   标准返回
     * @throws Exception
     */
    public void req(Context context, String actionName, String param, ScCallback callback) throws Exception {
        if (!isReady) {
            throw new RuntimeException("SCM is not ready! pls wait!");
        }
        if (!actionMap.containsKey(actionName)) {
            throw new RuntimeException("SCM action not found! name:" + actionName);
        }
        ScAction action = actionMap.get(actionName);
        action.invoke(context, param, callback);
    }


}
