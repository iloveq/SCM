package com.woaiqw.scm_api;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.woaiqw.annotation.Modules;
import com.woaiqw.scm_api.utils.Constants;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.woaiqw.scm_api.utils.Constants.PACKAGE_OF_GENERATE_FILE;

/**
 * Created by haoran on 2018/8/16.
 */

public class SCM {

    private static volatile SCM instance = null;
    private HashMap<String, ScAction> actionMap = new HashMap<>();
    private AtomicBoolean isReady = new AtomicBoolean(false);

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

    public void scanningSCMTable(Class<? extends Application> o) {
        Modules annotation = o.getAnnotation(Modules.class);
        String[] moduleNames = annotation.names();
        if (moduleNames.length != 0) {
            for (String moduleName : moduleNames) {
                try {
                    Class clazz = Class.forName(PACKAGE_OF_GENERATE_FILE + "." + moduleName + "SCMTable");
                    Field[] fields = clazz.getFields();
                    if (fields != null && fields.length != 0) {
                        for (Field field : fields) {
                            String name = field.getName();
                            String path = (String) field.get(name);
                            System.out.println("111" + "action:" + field.getName() + "value:" + field.get(field.getName()));
                            ScAction sca = (ScAction) Class.forName(path).newInstance();
                            actionMap.put(name, sca);
                        }
                    }
                } catch (Exception e) {
                    Log.e(Constants.SCM, e.getMessage());
                }
            }
        } else {
            throw new IllegalStateException(" moduleNames must a exit arr for scanning ");
        }

        isReady.set(true);
    }

    /**
     * request/ no param
     *
     * @param actionName the annotation of action name
     * @param callback   the callback
     * @throws Exception
     */
    public void req(Context context, String actionName, ScCallback callback) throws Exception {
        req(context, actionName, null, callback);
    }


    /**
     * request/ param
     *
     * @param actionName the annotation of action name
     * @param param      request param or json
     * @param callback   the callback
     * @throws Exception
     */
    public void req(Context context, String actionName, String param, ScCallback callback) throws Exception {
        if (!isReady.get()) {
            throw new RuntimeException("SCM is not ready! pls wait!");
        }
        if (!actionMap.containsKey(actionName)) {
            throw new RuntimeException("SCM action not found! name:" + actionName);
        }
        ScAction action = actionMap.get(actionName);
        action.invoke(context, param, callback);
    }


}
