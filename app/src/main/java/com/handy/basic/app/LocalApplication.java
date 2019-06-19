package com.handy.basic.app;

import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * FileName
 *
 * @author LiuJie https://github.com/Handy045
 * @description File Description
 * @date Created in 2019-06-03 19:22
 * @modified By liujie
 */
public class LocalApplication extends BaseApplication {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    void onCreateHDB() {

    }
}
