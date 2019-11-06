package com.handy.basic.base

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.handy.basic.eventbus.BaseEventMessage
import com.handy.basic.mvp.BasePresenter
import com.handy.basic.utils.ActivityStackUtils
import com.handy.basic.utils.PermissionsUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @title: BaseActivity
 * @projectName HandyBasicKT
 * @description: Activity通用超类
 * @author LiuJie https://github.com/Handy045
 * @date Created in 2019-10-11 16:29
 */
open class BaseActivity<P : BasePresenter> : AppCompatActivity(),
    BaseApplicationApi.ActivityApi<P> {
    //============================================================
    //  私有配置
    //============================================================

    /**
     * 初始化意图内容
     */
    private var isInitializedIntent = true
    /**
     * 初始化界面视图
     */
    private var isInitializedView = true
    /**
     * 初始化界面数据
     */
    private var isInitializedData = true
    /**
     * 界面请求处理
     */
    private var isInitializedRequest = true

    //============================================================
    //  开放配置
    //============================================================

    /**
     * 是否在日志打印生命周期
     */
    var isPrintLifecycle = false
    /**
     * 是否启用权限扫描功能
     */
    var isCheckPermissions = false
    /**
     * 屏幕宽度
     */
    var screenWidth = 0
    /**
     * 屏幕高度
     */
    var screenHeight = 0
    /**
     * MVP处理者
     */
    var presenter: P? = null
        get() {
            if (field == null) {
                field = initPresenter()
            }
            return field
        }
    /**
     * 保存的实例状态
     */
    var savedInstanceState: Bundle? = null
    /**
     * 界面视图布局
     */
    lateinit var rootLayout: View
    /**
     * 上下文
     */
    lateinit var mContext: Context
    /**
     * 父类实例
     */
    lateinit var mActivity: BaseActivity<P>

    //============================================================
    //  方法区
    //============================================================

    override fun onCreate(savedInstanceState: Bundle?) {
        if (isPrintLifecycle) {
            LogUtils.d("lifecycle - ${javaClass.simpleName} - onCreate(savedInstanceState: Bundle?)")
        }
        super.onCreate(savedInstanceState)

        try {
            this.mContext = this
            this.mActivity = this
            this.savedInstanceState = savedInstanceState
            this.screenWidth = ScreenUtils.getScreenWidth()
            this.screenHeight = ScreenUtils.getScreenHeight()

            this.presenter = initPresenter()

            ActivityStackUtils.addActivity(this)
            EventBus.getDefault().register(this)

            if (isInitializedIntent) {
                initIntentBundle(intent?.extras ?: Bundle())
                isInitializedIntent = false
            }

            if (isCheckPermissions) {
                checkPermissionsHDB()
                isCheckPermissions = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStart() {
        if (isPrintLifecycle) {
            LogUtils.d("lifecycle - ${javaClass.simpleName} - onStart()")
        }
        super.onStart()

        try {
            if (!isCheckPermissions) {
                if (isInitializedView) {
                    initViewHDB(savedInstanceState)
                    isInitializedView = false
                }

                if (isInitializedData) {
                    initDataHDB()
                    isInitializedData = false
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        if (isPrintLifecycle) {
            LogUtils.d("lifecycle - ${javaClass.simpleName} - onResume()")
        }
        super.onResume()

        try {
            onResumeHDB()

            if (isInitializedRequest) {
                initRequestHDB()
                isInitializedRequest = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        if (isPrintLifecycle) {
            LogUtils.d("lifecycle - ${javaClass.simpleName} - onPause()")
        }
        super.onPause()

        try {
            if (isFinishing) {
                onFinishing()

                KeyboardUtils.hideSoftInput(this)
                EventBus.getDefault().unregister(this)
                ActivityStackUtils.finishChoiceDesc(this)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStop() {
        if (isPrintLifecycle) {
            LogUtils.d("lifecycle - ${javaClass.simpleName} - onStop()")
        }
        super.onStop()
    }

    override fun onDestroy() {
        if (isPrintLifecycle) {
            LogUtils.d("lifecycle - ${javaClass.simpleName} - onDestroy()")
        }
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (isPrintLifecycle) {
            LogUtils.d("lifecycle - ${javaClass.simpleName} - onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)")
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        try {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (PermissionsUtils.checkPermissions(mActivity, true)) {
                    onPermissionSuccessHDB()
                }
            } else {
                onPermissionRejectionHDB(permissions)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun initPresenter(): P? {
        return null
    }

    override fun initIntentBundle(bundle: Bundle) {
    }

    override fun checkPermissionsHDB() {
        try {
            if (PermissionsUtils.checkPermissions(mActivity, true)) {
                onPermissionSuccessHDB()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun initViewHDB(savedInstanceState: Bundle?) {
    }

    override fun initDataHDB() {
    }

    override fun onResumeHDB() {
    }

    override fun initRequestHDB() {
    }

    override fun onFinishing() {
    }

    override fun onPermissionSuccessHDB() {
        try {
            if (isCheckPermissions) {
                if (isInitializedView) {
                    initViewHDB(savedInstanceState)
                    isInitializedView = false
                }

                if (isInitializedData) {
                    initDataHDB()
                    isInitializedData = false
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPermissionRejectionHDB(permissions: Array<String>) {
        if (permissions.isNotEmpty()) {
            LogUtils.d(permissions[0] + " requires permission which may be rejected by use.")
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getMessageEventOnMain(event: BaseEventMessage) {
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun getMessageEventOnBackground(event: BaseEventMessage) {
    }
}