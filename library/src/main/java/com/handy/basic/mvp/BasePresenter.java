package com.handy.basic.mvp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Mvp框架中 Presenter层通用基类
 *
 * @author LiuJie https://github.com/Handy045
 * @description 所有Presenter层均需要实现此接口
 * @date Created in 2018/2/1 上午11:16
 * @modified By LiuJie
 */
public class BasePresenter implements IPresenter {

    private LifecycleOwner lifecycleOwner;
    private List<IView> iViews = new ArrayList<>();

    public BasePresenter(@NonNull LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
        onStart();
    }

    public BasePresenter(@NonNull LifecycleOwner lifecycleOwner, @NonNull List<IView> iViews) {
        this.lifecycleOwner = lifecycleOwner;
        this.iViews = new ArrayList<>(iViews);
        for (IView iView : iViews) {
            if (ObjectUtils.isNotEmpty(iView)) {
                iView.setIPresenter(this);
            }
        }
        onStart();
    }

    public BasePresenter addiView(@NonNull IView iView) {
        this.iViews.add(iView);
        if (ObjectUtils.isNotEmpty(iView)) {
            iView.setIPresenter(this);
        }
        return this;
    }

    public BasePresenter addiViews(@NonNull List<IView> iViews) {
        this.iViews.addAll(iViews);
        for (IView iView : iViews) {
            if (ObjectUtils.isNotEmpty(iView)) {
                iView.setIPresenter(this);
            }
        }
        return this;
    }

    public void resetIPresenter() {
        for (IView iView : iViews) {
            if (ObjectUtils.isNotEmpty(iView)) {
                iView.setIPresenter(this);
            }
        }
    }

    /**
     * 启动
     */
    @Override
    public void onStart() {
        //将 LifecycleObserver 注册给 LifecycleOwner 后 @OnLifecycleEvent 才可以正常使用
        if (lifecycleOwner != null) {
            lifecycleOwner.getLifecycle().addObserver(this);
        }
    }

    /**
     * 销毁
     */
    @Override
    public void onDestroy() {
        this.lifecycleOwner = null;
        this.iViews = null;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
    }
}
