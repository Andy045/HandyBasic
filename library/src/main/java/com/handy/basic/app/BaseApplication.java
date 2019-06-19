package com.handy.basic.app;

import android.annotation.SuppressLint;
import android.app.Application;

import com.blankj.utilcode.util.ObjectUtils;
import com.handy.basic.config.BuglyConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * BaseApplication
 *
 * @author LiuJie https://github.com/Handy045
 * @description functional description.
 * @date Created in 2019/2/27 16:53
 * @modified By liujie
 */
public abstract class BaseApplication extends Application {

    //============================================================
    //  开放配置
    //============================================================

    /**
     * Bugly应用ID
     */
    public String buglyAppId = "";
    /**
     * 是否使用Log打印工具
     */
    public boolean isInitLogUtils = true;
    /**
     * 是否使用全局异常监控
     */
    public boolean isUseCrashUtil = true;
    /**
     * 使用全局异常监控后，出现异常时是否退出App</br>
     * true:退出app ; false:重启app
     */
    public boolean isCrashExitApp = true;

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            onCreateHDB();

            /*初始化工具类*/
            com.blankj.utilcode.util.Utils.init(this);
            com.handy.basic.utils.androidutilcode.Utils.init(this);

            /*初始化崩溃捕获工具*/
            if (isUseCrashUtil) {
                BaseApplicationMethod.initCrashUtils(getApplicationContext(), isCrashExitApp);
            }

            /*初始化日志工具*/
            if (isInitLogUtils) {
                BaseApplicationMethod.initLogUtils();
            }

            /*初始化侧滑返回功能*/
            BGASwipeBackHelper.init(this, null);

            /*初始化数据库*/
            FlowManager.init(getApplicationContext());

            /*初始化腾讯Bugly应用分析上报功能*/
            if (ObjectUtils.isNotEmpty(buglyAppId)) {
                //重新设置Bugly配置对象
                BuglyConfig buglyConfig = resetBuglyConfig(BuglyConfig.getInstance().setBuglyAppId(buglyAppId));
                //初始化Bugly功能
                buglyConfig.init(getApplicationContext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 可以在baseApplication的子类重写此方法，直接修改入参对象然后return反馈即可。
     *
     * @param buglyConfig 原Bugly配置对象
     * @return 新Bugly配置对象
     */
    protected BuglyConfig resetBuglyConfig(BuglyConfig buglyConfig) {
        return buglyConfig;
    }

    /**
     * 子类中加载其他功能
     */
    abstract void onCreateHDB();
}
