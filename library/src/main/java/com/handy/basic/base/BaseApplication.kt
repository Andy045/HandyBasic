package com.handy.basic.base

import android.app.Application
import android.os.Looper
import android.os.SystemClock
import android.widget.Toast
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.CrashUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.handy.basic.config.BuglyConfig
import kotlin.concurrent.thread

/**
 * @title: BaseApplication
 * @projectName HandyBasicKT
 * @description: TODO
 * @author LiuJie https://github.com/Handy045
 * @date Created in 2019-11-04 10:49
 */
open class BaseApplication() : Application(), BaseApplicationApi.ApplicationApi {
    //============================================================
    //  私有配置
    //============================================================

    /**
     * 实例对象
     */
    private var application: Application? = null

    //============================================================
    //  开放配置
    //============================================================

    /**
     * Bugly应用ID
     */
    var buglyAppId = ""
    /**
     * 是否使用Log打印工具
     */
    var isUseLogUtils = true
    /**
     * 是否使用全局异常监控
     */
    var isUseCrashUtil = true
    /**
     * 使用全局异常监控后，出现异常时是否退出App。
     */
    var isExitAppWhenCrash = true

    //============================================================
    //  方法区
    //============================================================

    /**
     * 构造函数
     * @param application Application
     */
    constructor(app: Application) : this() {
        application = app
    }

    override fun onCreate() {
        super.onCreate()
        try {
            if (application == null) {
                application = this
            }

            Utils.init(application)

            if (isUseCrashUtil) {
                CrashUtils.init { crashInfo, e ->
                    LogUtils.e(crashInfo)
                    if (isExitAppWhenCrash) {
                        thread(start = true) {
                            Looper.prepare()
                            Toast.makeText(
                                application!!.applicationContext,
                                "很抱歉：程序出现异常即将退出",
                                Toast.LENGTH_SHORT
                            ).show()
                            Looper.loop()
                        }
                        SystemClock.sleep(1200L)
                        AppUtils.exitApp()
                    } else {
                        AppUtils.relaunchApp()
                    }
                }
            }

            if (isUseLogUtils) {
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
                    // 设置日志文件后缀
                    .setFileExtension(".log")
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
                    .addFormatter<Any>(null)
                    // 自定义日志文件写入方法
                    .setFileWriter(null)
            }

            resetBuglyConfig(BuglyConfig.instance).init(applicationContext, buglyAppId)

            onCreateHDB(application!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 在子类中加载其他功能
     * @param application 当前或传入的Application
     */
    override fun onCreateHDB(application: Application) {
    }

    override fun resetBuglyConfig(buglyConfig: BuglyConfig): BuglyConfig {
        return buglyConfig
    }
}