package com.credic.home.data;

import com.woaiqw.scm_api.ScCallback;

/**
 * Created by haoran on 2018/8/24.
 */
public class DataProvider {


    private static class LoadNetworkConfigTask implements Runnable {

        private ScCallback callback;

        LoadNetworkConfigTask(ScCallback callback) {
            this.callback = callback;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                callback.onCallback(true, "加载了配置：今天是晴天！", "HHH");
            } catch (InterruptedException e) {
                e.printStackTrace();
                callback.onCallback(true, e.getMessage(), "HHH");
            }
        }
    }

    public static void getConfig(ScCallback callback) {
        Thread thread = new Thread(new LoadNetworkConfigTask(callback));
        thread.setName("child thread");
        thread.start();
    }

}
