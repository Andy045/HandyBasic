package com.handy.basic.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.handy.basic.eventbus.BaseEventMessage
import com.handy.basic.mvp.BasePresenter
import com.handy.basic.mvp.IView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @title: BaseFragment
 * @projectName HandyBasicKT
 * @description: Fragment通用超类
 * @author LiuJie https://github.com/Handy045
 * @date Created in 2019-10-11 16:29
 */
open class BaseFragment<A : BaseActivity<P>, P : BasePresenter> : Fragment(),
    BaseApplicationApi.FragmentApi, IView<P> {
    //============================================================
    //  私有配置
    //============================================================

    /**
     * 是否已创建
     */
    private var isCreated = false
    /**
     * 初始化界面视图
     */
    private var isInitializedView = true
    /**
     * 初始化界面数据
     */
    private var isInitializedData = true

    //============================================================
    //  开放配置
    //============================================================

    /**
     * 是否使用了新的生命周期启动模式
     */
    var isUseMaxLifecycle = false
    /**
     * 是否使用懒加载
     */
    var isUseLazyLoad = true
    /**
     * 是否在日志打印生命周期
     */
    var isPrintLifecycle = false
    /**
     * 屏幕宽度
     */
    var screenWidth = 0
    /**
     * 屏幕高度
     */
    var screenHeight = 0
    /**
     * 界面视图布局
     */
    lateinit var rootLayout: View
    /**
     * 所挂载的Activity
     */
    var parentActivity: A? = null
        get() {
            if (field == null) {
                field = this.mActivity as? A
            }
            return field
        }
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
     * 上下文
     */
    lateinit var mContext: Context
    /**
     * 父类实例
     */
    lateinit var mActivity: Activity

    //============================================================
    //  方法区
    //============================================================

    override fun onAttach(context: Context) {
        if (this.isPrintLifecycle) {
            LogUtils.d("lifecycle - ${javaClass.simpleName} - onAttach(context: Context)")
        }
        super.onAttach(context)

        this.mContext = context
    }

    override fun onAttach(activity: Activity) {
        if (this.isPrintLifecycle) {
            LogUtils.d("lifecycle - ${javaClass.simpleName} - onAttach(activity: Activity)")
        }
        super.onAttach(activity)

        this.mActivity = activity
        this.parentActivity = activity as? A
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (this.isPrintLifecycle) {
            LogUtils.d("lifecycle - ${javaClass.simpleName} - onCreate(savedInstanceState: Bundle?)")
        }
        super.onCreate(savedInstanceState)

        try {
            this.presenter = initPresenter()
            this.savedInstanceState = savedInstanceState
            this.screenWidth = ScreenUtils.getScreenWidth()
            this.screenHeight = ScreenUtils.getScreenHeight()

            EventBus.getDefault().register(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (this.isPrintLifecycle) {
            LogUtils.d("lifecycle - ${javaClass.simpleName} - onViewCreated(view: View, savedInstanceState: Bundle?)")
        }
        super.onViewCreated(view, savedInstanceState)

        try {
            this.isCreated = true
            this.rootLayout = view

            if (this.isInitializedView) {
                initViewHDB(view, savedInstanceState)
                this.isInitializedView = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        if (this.isPrintLifecycle) {
            LogUtils.d("lifecycle - ${javaClass.simpleName} - onActivityCreated(savedInstanceState: Bundle?)")
        }
        super.onActivityCreated(savedInstanceState)

        try {
            if (this.isInitializedData) {
                initDataHDB(savedInstanceState)
                this.isInitializedView = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStart() {
        if (this.isPrintLifecycle) {
            LogUtils.d("lifecycle - ${javaClass.simpleName} - onStart()")
        }
        super.onStart()
    }

    override fun onResume() {
        if (this.isPrintLifecycle) {
            LogUtils.d("lifecycle - ${javaClass.simpleName} - onResume()")
        }
        super.onResume()

        try {
            if (this.isCreated) {
                onResumeHDB()
                if (this.isUseMaxLifecycle && this.isUseLazyLoad) {
                    onLazyLoadHDB()
                    this.isUseLazyLoad = false
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (this.isPrintLifecycle) {
            LogUtils.d("lifecycle - ${javaClass.simpleName} - setUserVisibleHint(isVisibleToUser: Boolean)")
        }
        super.setUserVisibleHint(isVisibleToUser)

        try {
            if (this.isCreated && isVisibleToUser) {
                onVisiableHDB()
                if (!this.isUseMaxLifecycle && this.isUseLazyLoad) {
                    onLazyLoadHDB()
                    this.isUseLazyLoad = false
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        if (this.isPrintLifecycle) {
            LogUtils.d("lifecycle - ${javaClass.simpleName} - onPause()")
        }
        super.onPause()
    }

    override fun onStop() {
        if (this.isPrintLifecycle) {
            LogUtils.d("lifecycle - ${javaClass.simpleName} - onStop()")
        }
        super.onStop()
    }

    override fun onDestroyView() {
        if (this.isPrintLifecycle) {
            LogUtils.d("lifecycle - ${javaClass.simpleName} - onDestroyView()")
        }
        super.onDestroyView()
    }

    override fun onDestroy() {
        if (this.isPrintLifecycle) {
            LogUtils.d("lifecycle - ${javaClass.simpleName} - onDestroy()")
        }
        super.onDestroy()

        try {
            this.isCreated = false
            onFinishing()
            EventBus.getDefault().unregister(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDetach() {
        if (this.isPrintLifecycle) {
            LogUtils.d("lifecycle - ${javaClass.simpleName} - onDetach()")
        }
        super.onDetach()
    }

    override fun initViewHDB(view: View, savedInstanceState: Bundle?) {
    }

    override fun initDataHDB(savedInstanceState: Bundle?) {
    }

    override fun onResumeHDB() {
    }

    override fun onVisiableHDB() {
    }

    override fun onLazyLoadHDB() {
    }

    override fun onFinishing() {
    }

    override fun initPresenter(): P? {
        return parentActivity?.presenter
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getMessageEventOnMain(event: BaseEventMessage) {
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun getMessageEventOnBackground(event: BaseEventMessage) {
    }
}