package com.handy.basic.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.CrashUtils;
import com.blankj.utilcode.util.LogUtils;

/**
 * FileName
 *
 * @author LiuJie https://github.com/Handy045
 * @description File Description
 * @date Created in 2019-06-03 20:01
 * @modified By liujie
 */
public class BaseApplicationMethod {

    @SuppressLint("StaticFieldLeak")
    public static void initCrashUtils(final Context appContext, final boolean isExitApp) {
        CrashUtils.init(new CrashUtils.OnCrashListener() {
            @Override
            public void onCrash(String crashInfo, Throwable e) {
                if (isExitApp) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            Looper.prepare();
                            Toast.makeText(appContext, "很抱歉：程序出现异常即将退出", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                            return null;
                        }
                    }.execute();

                    SystemClock.sleep(1200L);

                    AppUtils.exitApp();
                } else {
                    AppUtils.relaunchApp();
                }
            }
        });
    }

    public static void initLogUtils() {
        LogUtils.getConfig()
                // 设置 log 总开关，包括输出到控制台和文件，默认开
                .setLogSwitch(AppUtils.isAppDebug())
                // 设置是否输出到控制台开关，默认开
                .setConsoleSwitch(AppUtils.isAppDebug())
                // 设置 log 全局标签，默认为空
                // 当全局标签不为空时，我们输出的 log 全部为该 tag，
                // 为空时，如果传入的 tag 为空那就显示类名，否则显示 tag
                .setGlobalTag("HandyBasic")
                // 设置 log 头信息开关，默认为开
                .setLogHeadSwitch(true)
                // 打印 log 时是否存到文件的开关，默认关
                .setLog2FileSwitch(AppUtils.isAppDebug())
                // 当自定义路径为空时，写入应用的/cache/log/目录中
                .setDir("")
                // 当文件前缀为空时，默认为"util"，即写入文件为"util-yyyy-MM-dd.txt"
                .setFilePrefix("")
                // 输出日志是否带边框开关，默认开
                .setBorderSwitch(true)
                // 一条日志仅输出一条，默认开，为美化 AS 3.1 的 Logcat
                .setSingleTagSwitch(true)
                // log 的控制台过滤器，和 logcat 过滤器同理，默认 Verbose
                .setConsoleFilter(LogUtils.V)
                // log 文件过滤器，和 logcat 过滤器同理，默认 Verbose
                .setFileFilter(LogUtils.V)
                // log 栈深度，默认为 1
                .setStackDeep(1)
                // 设置栈偏移，比如二次封装的话就需要设置，默认为 0
                .setStackOffset(0)
                // 设置日志可保留天数，默认为 -1 表示无限时长
                .setSaveDays(7)
                // 新增 ArrayList 格式化器，默认已支持 Array, Throwable, Bundle, Intent 的格式化输出
                .addFormatter(null)
                // 自定义日志文件写入方法
                .setFileWriter(null);
    }
}
