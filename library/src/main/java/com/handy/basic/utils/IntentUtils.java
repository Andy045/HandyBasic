package com.handy.basic.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * 意图相关工具类
 *
 * @author LiuJie https://github.com/Handy045
 * @description functional description.
 * @date Created in 2019/2/27 16:53
 * @modified By liujie
 */
public final class IntentUtils {

    private IntentUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void openActivity(Activity activity, Class<?> clss, boolean isFinish) {
        Intent intent = new Intent(activity, clss);
        activity.startActivity(intent);
        if (isFinish) {
            activity.finish();
        }
    }

    public static void openActivity(Activity activity, Class<?> clss, Bundle bundle, boolean isFinish) {
        Intent intent = new Intent(activity, clss);
        intent.putExtras(bundle);
        activity.startActivity(intent);
        if (isFinish) {
            activity.finish();
        }
    }

    public static void openActivityForResult(Activity activity, Class<?> clss, int requestCode, boolean isFinish) {
        Intent intent = new Intent(activity, clss);
        activity.startActivityForResult(intent, requestCode);
        if (isFinish) {
            activity.finish();
        }
    }

    public static void openActivityForResult(Activity activity, Class<?> clss, Bundle bundle, int requestCode, boolean isFinish) {
        Intent intent = new Intent(activity, clss);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
        if (isFinish) {
            activity.finish();
        }
    }
}
