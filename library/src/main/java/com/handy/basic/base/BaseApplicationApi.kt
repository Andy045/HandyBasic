package com.handy.basic.base

import android.app.Application
import android.os.Bundle
import android.view.View
import com.handy.basic.config.BuglyConfig
import com.handy.basic.mvp.BasePresenter

/**
 * @title: BaseApplicationApi
 * @projectName HandyBasicKT
 * @description: 基础组件超类通用接口
 * @author LiuJie https://github.com/Handy045
 * @date Created in 2019-11-04 10:49
 */
class BaseApplicationApi {
    interface ApplicationApi {
        /**
         * application初始化
         */
        fun onCreateHDB(application: Application)

        fun resetBuglyConfig(buglyConfig: BuglyConfig): BuglyConfig
    }

    interface ActivityApi<P : BasePresenter> {
        /**
         * 初始化MVP处理者
         */
        fun initPresenter(): P?

        /**
         * 初始化意图参数
         */
        fun initIntentBundle(bundle: Bundle)

        /**
         * 检查应用权限
         */
        fun checkPermissionsHDB()

        /**
         * 初始化布局控件
         */
        fun initViewHDB(savedInstanceState: Bundle?)

        /**
         * 初始化相关数据
         */
        fun initDataHDB()

        /**
         * 界面刷新或恢复
         */
        fun onResumeHDB()

        /**
         * 初始化请求
         */
        fun initRequestHDB()

        /**
         * 界面结束并销毁
         */
        fun onFinishing()

        /**
         * 权限检查通过
         */
        fun onPermissionSuccessHDB()

        /**
         * 权限检查不通过
         */
        fun onPermissionRejectionHDB(permissions: Array<String>)
    }

    interface FragmentApi {
        /**
         * 初始化布局控件
         */
        fun initViewHDB(view: View, savedInstanceState: Bundle?)

        /**
         * 初始化相关数据
         */
        fun initDataHDB(savedInstanceState: Bundle?)

        /**
         * 界面刷新或恢复
         */
        fun onResumeHDB()

        /**
         * 界面可见时
         */
        fun onVisiableHDB()

        /**
         * 懒加载
         */
        fun onLazyLoadHDB()

        /**
         * 界面结束并销毁
         */
        fun onFinishing()
    }
}