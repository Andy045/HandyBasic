package com.handy.basic.config

import android.content.Context
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.handy.basic.utils.ProcessUtils
import com.tencent.bugly.Bugly
import com.tencent.bugly.crashreport.CrashReport
import java.nio.charset.Charset

/**
 * @title: BuglyConfig
 * @projectName HandyBasicKT
 * @description: TODO
 * @author LiuJie https://github.com/Handy045
 * @date Created in 2019-11-04 10:49
 */
class BuglyConfig private constructor() {
    //============================================================
    //  私有配置
    //============================================================
    /**
     * Bugly注册的产品ID
     */
    private var buglyAppId = ""
    /**
     * Bugly注册的产品ID
     */
    private lateinit var userStrategy: CrashReport.UserStrategy
    /**
     * 发生Crash时，一起上报的一些自定义的环境信息
     */
    private var crashAddInfo: MutableMap<String, String> = mutableMapOf()
    /**
     * 发生Crash时，一起上报的一些附加的跟踪数据信息
     */
    private var crashFollowInfo: MutableMap<String, String> = mutableMapOf()

    //============================================================
    //  开放配置
    //============================================================

    companion object {
        val instance: BuglyConfig by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            BuglyConfig()
        }
    }

    //============================================================
    //  方法区
    //============================================================

    fun init(applicationContext: Context, buglyAppId: String) {
        try {
            if (this.buglyAppId.isEmpty()) {
                LogUtils.e("Bugly未初始化，请先配置BuglyAppId")
                return
            }

            this.buglyAppId = buglyAppId
            this.userStrategy = getUserStrategy(applicationContext)

            // 是否将App运行的设备注册为测试设备
            CrashReport.setIsDevelopmentDevice(applicationContext, AppUtils.isAppDebug())
            // 初始化Bugly
            Bugly.init(applicationContext, this.buglyAppId, false, this.userStrategy)
            // 打印配置信息
            printConfig()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getUserStrategy(applicationContext: Context): CrashReport.UserStrategy {
        val userStrategy: CrashReport.UserStrategy = CrashReport.UserStrategy(applicationContext)
        val packageName = applicationContext.packageName
        val processName = ProcessUtils.getProcessName(android.os.Process.myPid())
        // 设置是否只在主进程上报
        userStrategy.isUploadProcess =
            processName.isNullOrEmpty() || processName == packageName
        // 设置渠道
        userStrategy.appChannel =
            "V${AppUtils.getAppVersionName()}_${if (AppUtils.isAppDebug()) "Debug" else "Release"}"
        // App的版本
        userStrategy.appVersion = AppUtils.getAppVersionName()
        // App的包名
        userStrategy.appPackageName = AppUtils.getAppPackageName()
        // 发生Crash时，一起上报的附加的跟踪数据信息
        userStrategy.setCrashHandleCallback(object : CrashReport.CrashHandleCallback() {
            override fun onCrashHandleStart(
                p0: Int,
                p1: String?,
                p2: String?,
                p3: String?
            ): MutableMap<String, String> {
                try {
                    return crashFollowInfo
                } catch (e: Exception) {
                    return super.onCrashHandleStart(p0, p1, p2, p3)
                }
            }

            override fun onCrashHandleStart2GetExtraDatas(
                p0: Int,
                p1: String?,
                p2: String?,
                p3: String?
            ): ByteArray {
                try {
                    return "Extra data.".toByteArray(Charset.forName("UTF-8"))
                } catch (e: Exception) {
                    return super.onCrashHandleStart2GetExtraDatas(p0, p1, p2, p3)
                }
            }
        })
        return userStrategy
    }

    fun printConfig() {
        val stringBuilder: StringBuilder = StringBuilder()
        stringBuilder.append("======================= Bugly配置信息 =======================\n")
        stringBuilder.append("||\t是否启用Debug模式：").append(AppUtils.isAppDebug()).append("\n")
        stringBuilder.append("||\t设置是否只在主进程上报：").append(this.userStrategy.isUploadProcess)
            .append("\n")
        stringBuilder.append("||\t是否将App运行的设备注册为测试设备：").append(AppUtils.isAppDebug()).append("\n")
        stringBuilder.append("||\tBugly注册的产品ID：").append(this.buglyAppId).append("\n")
        stringBuilder.append("||\t设置渠道：").append(this.userStrategy.appChannel).append("\n")
        stringBuilder.append("||\tApp的版本名称：").append(this.userStrategy.appVersion).append("\n")
        stringBuilder.append("||\tApp的包名：").append(this.userStrategy.appPackageName).append("\n")

        stringBuilder.append("||\t发生Crash时，一起上报的自定义的环境信息：")
        if (this.crashAddInfo.isNotEmpty()) {
            stringBuilder.append("\n")
            for ((key, value) in this.crashAddInfo) {
                stringBuilder.append("||\t\t").append("Key：").append(key).append("\tValue：")
                    .append(value).append("\n")
            }
        } else {
            stringBuilder.append("暂无\n")
        }

        stringBuilder.append("||\t发生Crash时，一起上报的附加的跟踪数据信息：")
        if (this.crashFollowInfo.isNotEmpty()) {
            stringBuilder.append("\n")
            for ((key, value) in this.crashFollowInfo) {
                stringBuilder.append("||\t\t").append("Key：").append(key).append("\tValue：")
                    .append(value).append("\n")
            }
        } else {
            stringBuilder.append("暂无\n")
        }
        stringBuilder.append("============================================================")
    }
}