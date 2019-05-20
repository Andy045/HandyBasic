package com.handy.basic.config;

import android.content.Context;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.handy.base.utils.ProcessUtils;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 腾讯Bugly配置对象
 *
 * @author LiuJie https://github.com/Handy045
 * @description 包括UserStrategy、项目BuglyID、是否开启Debug模式等
 * @date Created in 2018/1/23 14:02
 * @modified By LiuJie
 */
public class BuglyConfig {

    //============================================================
    //  功能配置
    //============================================================

    /**
     * Bugly注册的产品ID
     */
    private String buglyAppId;
    /**
     * 设置是否只在主进程上报，默认：true-是
     */
    private boolean isUploadProcess;
    /**
     * 设置渠道，默认："v" + AppUtils.getAppVersionName() + "_" + (AppUtils.isAppDebug() ? "Debug" : "Release")
     */
    private String appChannel;
    /**
     * App的版本名称，默认：AppUtils.getAppVersionName()
     */
    private String appVersion;
    /**
     * App的包名，默认：AppUtils.getAppPackageName()
     */
    private String appPackageName;
    /**
     * 发生Crash时，一起上报的一些自定义的环境信息，最多9组，默认：new HashMap<>(9)-已实例化但无内容
     */
    private HashMap<String, String> crashAddInfo;
    /**
     * 发生Crash时，一起上报的一些附加的跟踪数据信息，默认：new HashMap<>()-已实例化但无内容
     */
    private HashMap<String, String> crashFollowInfo;

    //============================================================
    //  全局变量
    //============================================================

    private volatile static BuglyConfig instance;

    public static BuglyConfig getInstance() {
        if (instance == null) {
            synchronized (BuglyConfig.class) {
                if (instance == null) {
                    instance = new BuglyConfig();
                }
            }
        }
        return instance;
    }

    /**
     * 实例化Bugly配置对象，由于会用到AppUtils工具类，请务必保证Application执行完父类的onCreate方法后在实例化此对象
     */
    private BuglyConfig() {
        this.appChannel = "V" + AppUtils.getAppVersionName() + "_" + (AppUtils.isAppDebug() ? "Debug" : "Release");
        this.appVersion = AppUtils.getAppVersionName();
        this.appPackageName = AppUtils.getAppPackageName();
        this.crashAddInfo = new HashMap<>(9);
        this.crashFollowInfo = new HashMap<>();
    }

    /**
     * 初始化Bugly，只能在Application的onCreate()方法中调用
     *
     * @param appLicationContext ApplicationContext 上下文
     */
    public void init(Context appLicationContext) {
        if (ObjectUtils.isNotEmpty(this.buglyAppId)) {
            //是否将App运行的设备注册为测试设备
            CrashReport.setIsDevelopmentDevice(appLicationContext, AppUtils.isAppDebug());

            Bugly.init(appLicationContext, this.buglyAppId, false, getUserStrategy(appLicationContext));

            printConfig();
        } else {
            LogUtils.e("Bugly初始化失败，请先配置BuglyAppId");
        }
    }

    /**
     * 根据配置内容，获取Bugly初始化所需的配置对象
     *
     * @param appLicationContext ApplicationContext 上下文
     * @return Bugly初始化所需的配置对象
     */
    private CrashReport.UserStrategy getUserStrategy(Context appLicationContext) {
        CrashReport.UserStrategy userStrategy = new CrashReport.UserStrategy(appLicationContext);
        // 设置是否只在主进程上报
        String packageName = appLicationContext.getPackageName();
        String processName = ProcessUtils.getProcessName(android.os.Process.myPid());
        this.isUploadProcess = processName == null || processName.equals(packageName);
        userStrategy.setUploadProcess(this.isUploadProcess);
        // 设置渠道
        userStrategy.setAppChannel(this.appChannel);
        // App的版本
        userStrategy.setAppVersion(this.appVersion);
        // App的包名
        userStrategy.setAppPackageName(this.appPackageName);
        // 发生Crash时，一起上报的附加的跟踪数据信息
        userStrategy.setCrashHandleCallback(new CrashReport.CrashHandleCallback() {
            @Override
            public synchronized Map<String, String> onCrashHandleStart(int crashType, String errorType, String errorMessage, String errorStack) {
                try {
                    if (ObjectUtils.isNotEmpty(crashFollowInfo)) {
                        return crashFollowInfo;
                    } else {
                        return super.onCrashHandleStart(crashType, errorType, errorMessage, errorStack);
                    }
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            public synchronized byte[] onCrashHandleStart2GetExtraDatas(int crashType, String errorType, String errorMessage, String errorStack) {
                try {
                    if (ObjectUtils.isNotEmpty(crashFollowInfo)) {
                        return "Extra data.".getBytes(Charset.forName("UTF-8"));
                    } else {
                        return super.onCrashHandleStart2GetExtraDatas(crashType, errorType, errorMessage, errorStack);
                    }
                } catch (Exception e) {
                    return null;
                }
            }
        });
        return userStrategy;
    }

    /**
     * 设置Bugly应用配置ID
     *
     * @param buglyAppId Bugly注册的产品ID
     */
    public BuglyConfig setBuglyAppId(@NonNull String buglyAppId) {
        this.buglyAppId = buglyAppId;
        return this;
    }

    /**
     * 设置发生Crash时的自定义的环境信息
     *
     * @param appLicationContext ApplicationContext
     * @param crashAddInfo       信息内容
     */
    public BuglyConfig setCrashAddInfo(@NonNull Context appLicationContext, @NonNull HashMap<String, String> crashAddInfo) {
        if (ObjectUtils.isNotEmpty(crashAddInfo)) {
            this.crashAddInfo = crashAddInfo;
            if (ObjectUtils.isNotEmpty(crashAddInfo)) {
                for (Map.Entry<String, String> entry : this.crashAddInfo.entrySet()) {
                    if (ObjectUtils.isNotEmpty(entry.getKey()) && ObjectUtils.isNotEmpty(entry.getValue())) {
                        CrashReport.putUserData(appLicationContext, entry.getKey(), entry.getValue());
                    }
                }
            }
        }
        return this;
    }

    /**
     * 设置发生Crash时附加的跟踪数据信息
     *
     * @param crashFollowInfo 信息内容
     */
    public BuglyConfig setCrashFollowInfo(@NonNull HashMap<String, String> crashFollowInfo) {
        if (ObjectUtils.isNotEmpty(crashFollowInfo)) {
            this.crashFollowInfo = crashFollowInfo;
        }
        return this;
    }

    private void printConfig() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("======================= Bugly配置信息 =======================\n");
        stringBuilder.append("||\t是否启用Debug模式：").append(AppUtils.isAppDebug()).append("\n");
        stringBuilder.append("||\t设置是否只在主进程上报：").append(this.isUploadProcess).append("\n");
        stringBuilder.append("||\t是否将App运行的设备注册为测试设备：").append(AppUtils.isAppDebug()).append("\n");
        stringBuilder.append("||\tBugly注册的产品ID：").append(this.buglyAppId).append("\n");
        stringBuilder.append("||\t设置渠道：").append(this.appChannel).append("\n");
        stringBuilder.append("||\tApp的版本名称：").append(this.appVersion).append("\n");
        stringBuilder.append("||\tApp的包名：").append(this.appPackageName).append("\n");

        stringBuilder.append("||\t发生Crash时，一起上报的自定义的环境信息：");
        if (ObjectUtils.isNotEmpty(this.crashAddInfo)) {
            stringBuilder.append("\n");
            for (Map.Entry<String, String> entry : this.crashAddInfo.entrySet()) {
                if (ObjectUtils.isNotEmpty(entry.getKey()) && ObjectUtils.isNotEmpty(entry.getValue())) {
                    stringBuilder.append("||\t\t").append("Key：").append(entry.getKey()).append("Value：").append(entry.getValue()).append("\n");
                }
            }
        } else {
            stringBuilder.append("暂无\n");
        }

        stringBuilder.append("||\t发生Crash时，一起上报的附加的跟踪数据信息：");
        if (ObjectUtils.isNotEmpty(this.crashFollowInfo)) {
            stringBuilder.append("\n");
            for (Map.Entry<String, String> entry : this.crashFollowInfo.entrySet()) {
                if (ObjectUtils.isNotEmpty(entry.getKey()) && ObjectUtils.isNotEmpty(entry.getValue())) {
                    stringBuilder.append("||\t\t").append("Key：").append(entry.getKey()).append("Value：").append(entry.getValue()).append("\n");
                }
            }
        } else {
            stringBuilder.append("暂无\n");
        }

        stringBuilder.append("============================================================");

        LogUtils.d(stringBuilder.toString());
    }
}
