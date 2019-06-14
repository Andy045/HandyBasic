package com.handy.basic.mvp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

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

    protected AppCompatActivity activity;
    protected List<IView> iViews = new ArrayList<>();

    public BasePresenter() {
        onStart();
    }

    public BasePresenter(@NonNull AppCompatActivity activity) {
        this.activity = activity;
        onStart();
    }

    public BasePresenter(@NonNull AppCompatActivity activity, @NonNull List<IView> iViews) {
        this.activity = activity;
        this.iViews = new ArrayList<>(iViews);
        for (IView iView : iViews) {
            iView.setIPresenter(this);
        }
        onStart();
    }

    public BasePresenter addiView(IView iView) {
        this.iViews.add(iView);
        iView.setIPresenter(this);
        return this;
    }

    public BasePresenter addiViews(List<IView> iViews) {
        this.iViews.addAll(iViews);
        for (IView iView : iViews) {
            iView.setIPresenter(this);
        }
        return this;
    }

    /**
     * 启动
     */
    @Override
    public void onStart() {
        //将 LifecycleObserver 注册给 LifecycleOwner 后 @OnLifecycleEvent 才可以正常使用
        if (activity != null) {
            ((LifecycleOwner) activity).getLifecycle().addObserver(this);
        }
    }

    /**
     * 销毁
     */
    @Override
    public void onDestroy() {
        this.activity = null;
        this.iViews = null;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
    }
}
