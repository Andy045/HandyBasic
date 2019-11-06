package com.handy.basic.mvp

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 * @title: BasePresenter
 * @projectName HandyBasicKT
 * @description: Mvp-Presenter通用超类
 * @author LiuJie https://github.com/Handy045
 * @date Created in 2019-10-11 16:30
 */
open class BasePresenter(private var lifecycleOwner: LifecycleOwner?) : IPresenter {
    init {
        // 将 LifecycleObserver 注册给 LifecycleOwner 后 @OnLifecycleEvent 才可以正常使用
        lifecycleOwner?.lifecycle?.addObserver(this)
    }

    override fun onDestroy() {
        this.lifecycleOwner = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    internal fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
    }
}