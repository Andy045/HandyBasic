package com.handy.basic.access;

import android.annotation.SuppressLint;
import android.app.Application;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.ObjectUtils;
import com.handy.basic.app.BaseApplicationMethod;
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
    /**
     * 使用全局异常监控后，出现异常时是否退出App</br>
     * true:退出app ; false:重启app
     */
    private boolean isCrashExitApp = true;

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

            /*初始化崩溃捕获工具*/
            if (isUseCrashUtil) {
                BaseApplicationMethod.initCrashUtils(application.getApplicationContext(), isCrashExitApp);
            }

            /*初始化日志工具*/
            if (isInitLogUtils) {
                BaseApplicationMethod.initLogUtils();
            }

            /*初始化侧滑返回功能*/
            BGASwipeBackHelper.init(application, null);

            /*初始化数据库*/
            FlowManager.init(application.getApplicationContext());

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

    public HandyBase setCrashExitApp(boolean crashExitApp) {
        isCrashExitApp = crashExitApp;
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
