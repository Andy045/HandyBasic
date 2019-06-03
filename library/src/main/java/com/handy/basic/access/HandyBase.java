package com.handy.basic.access;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.CrashUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.handy.basic.config.BuglyConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * 第三方接入，不需要使用BaseApplication
 *
 * @author LiuJie https://www.Handy045.com
 * @description 第三方接入
 * @date Created in 2018/6/6 13:09
 * @modified By LiuJie
 */
public class HandyBase {

    //============================================================
    //  功能配置
    //============================================================

    /**
     * Bugly应用ID
     */
    private String buglyAppId = "";
    /**
     * 是否使用Log打印工具
     */
    private boolean isInitLogUtils = true;
    /**
     * 是否使用全局异常监控
     */
    private boolean isUseCrashUtil = true;

    //============================================================
    //  全局变量
    //============================================================

    private volatile static HandyBase instance;

    private BuglyConfigApi buglyConfigApi;

    public static HandyBase getInstance() {
        if (instance == null) {
            synchronized (HandyBase.class) {
                if (instance == null) {
                    instance = new HandyBase();
                }
            }
        }
        return instance;
    }

    private HandyBase() {
    }

    /**
     * 在第一个启动的Activity或者自定义Application中的onCreate()方法里调用
     */
    @SuppressLint("MissingPermission")
    public void init(@NonNull final Application application) {
        try {
            /*初始化工具类*/
            com.blankj.utilcode.util.Utils.init(application);
            com.handy.basic.utils.androidutilcode.Utils.init(application);

            /*初始化侧滑返回功能*/
            BGASwipeBackHelper.init(application, null);

            /*初始化数据库*/
            FlowManager.init(application.getApplicationContext());

            /*初始化崩溃捕获工具*/
            if (isUseCrashUtil) {
                CrashUtils.init(new CrashUtils.OnCrashListener() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onCrash(String crashInfo, Throwable e) {
                        LogUtils.e(e);

                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... voids) {
                                Looper.prepare();
                                Toast.makeText(application.getApplicationContext(), "很抱歉：程序出现异常即将退出", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                                return null;
                            }
                        }.execute();

                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        application.startActivity(startMain);

                        SystemClock.sleep(1200L);

                        AppUtils.exitApp();
                    }
                });
            }

            /*初始化日志工具*/
            if (isInitLogUtils) {
                LogUtils.getConfig()
                        //设置 log 总开关，包括输出到控制台和文件，默认开
                        .setLogSwitch(AppUtils.isAppDebug())
                        //设置是否输出到控制台开关，默认开
                        .setConsoleSwitch(AppUtils.isAppDebug())
                        //设置 log 全局标签，默认为空
                        //当全局标签不为空时，我们输出的 log 全部为该 tag，为空时，如果传入的 tag 为空那就显示类名，否则显示 tag
                        .setGlobalTag("HandyBasic")
                        //设置 log 头信息开关，默认为开
                        .setLogHeadSwitch(true)
                        //打印 log 时是否存到文件的开关，默认关
                        .setLog2FileSwitch(AppUtils.isAppDebug())
                        //当自定义路径为空时，写入应用的/cache/log/目录中
                        .setDir("")
                        //当文件前缀为空时，默认为"util"，即写入文件为"util-yyyy-MM-dd.txt"
                        .setFilePrefix("")
                        //输出日志是否带边框开关，默认开
                        .setBorderSwitch(true)
                        //一条日志仅输出一条，默认开，为美化 AS 3.1 的 Logcat
                        .setSingleTagSwitch(true)
                        //log 的控制台过滤器，和 logcat 过滤器同理，默认 Verbose
                        .setConsoleFilter(LogUtils.V)
                        //log 文件过滤器，和 logcat 过滤器同理，默认 Verbose
                        .setFileFilter(LogUtils.V)
                        //log 栈深度，默认为 1
                        .setStackDeep(1)
                        //设置栈偏移，比如二次封装的话就需要设置，默认为 0
                        .setStackOffset(0);
            }

            /*初始化腾讯Bugly应用分析上报功能*/
            if (ObjectUtils.isNotEmpty(buglyAppId)) {
                //重新设置Bugly配置对象
                BuglyConfig buglyConfig = BuglyConfig.getInstance().setBuglyAppId(buglyAppId);

                if (ObjectUtils.isNotEmpty(buglyConfigApi)) {
                    buglyConfig = buglyConfigApi.resetBuglyConfig(buglyConfig);
                }

                //初始化Bugly功能
                buglyConfig.init(application.getApplicationContext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HandyBase setBuglyAppId(String buglyAppId) {
        this.buglyAppId = buglyAppId;
        return this;
    }

    public HandyBase setInitLogUtils(boolean initLogUtils) {
        isInitLogUtils = initLogUtils;
        return this;
    }

    public HandyBase setUseCrashUtil(boolean useCrashUtil) {
        isUseCrashUtil = useCrashUtil;
        return this;
    }

    public HandyBase setBuglyConfigApi(BuglyConfigApi buglyConfigApi) {
        this.buglyConfigApi = buglyConfigApi;
        return this;
    }

    public interface BuglyConfigApi {
        BuglyConfig resetBuglyConfig(BuglyConfig buglyConfig);
    }
}
