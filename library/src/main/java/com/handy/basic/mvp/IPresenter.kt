package com.handy.basic.mvp

import androidx.lifecycle.LifecycleObserver

/**
 * @title: IPresenter
 * @projectName HandyBasicKT
 * @description: presenter层通用接口
 * @author LiuJie https://github.com/Handy045
 * @date Created in 2019-10-11 16:30
 */
open interface IPresenter : LifecycleObserver {
    /**
     * 注销
     */
    fun onDestroy()
}