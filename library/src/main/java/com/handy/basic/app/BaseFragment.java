package com.handy.basic.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.handy.basic.evenbus.BaseMessageEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * BaseFragment
 *
 * @author LiuJie https://github.com/Handy045
 * @description functional description.
 * @date Created in 2019/2/27 16:53
 * @modified By liujie
 */
public abstract class BaseFragment extends RxFragment implements BaseApplicationApi.BaseFragmentApi {

    //============================================================
    //  功能配置
    //============================================================

    /**
     * Fragment是否已创建成功，用于控制在Fragment创建之前不调用 {@link BaseFragment#setUserVisibleHint(boolean)} 方法实现内部的 {@link BaseFragment#onVisiableHDB()} ()} 和 {@link BaseFragment#onLazyLoadHDB()} 方法
     */
    private boolean isCreateed = false;
    /**
     * onViewCreated中初始化界面视图
     */
    private boolean isInitViewHDB = true;
    /**
     * onActivityCreated中初始化界面数据
     */
    private boolean isInitDataHDB = true;

    //============================================================
    //  开放配置
    //============================================================

    /**
     * 用于控制每个Fragment进入{@link BaseFragment#setUserVisibleHint(boolean)} 时，是否重新执行onRequest()方法
     */
    public boolean isLazyLoadHDB = true;
    /**
     * 是否Log打印Fragment的生命周期
     */
    public boolean isLogFragmentLife = false;

    //============================================================
    //  全局变量
    //============================================================

    /**
     * 手机屏幕宽度参数
     */
    public int screenWidth;
    /**
     * 手机屏幕高度参数
     */
    public int screenHeight;
    /**
     * 界面视图布局
     */
    public View rootLayout;

    public Context context;
    public Activity activity;
    public Application application;

    @Override
    public void onAttach(Context context) {
        if (isLogFragmentLife) {
            LogUtils.d("Fragment - " + this.getClass().getSimpleName() + " - onAttach(Context context)");
        }
        super.onAttach(context);

        this.context = context;
    }

    @Override
    public void onAttach(Activity activity) {
        if (isLogFragmentLife) {
            LogUtils.d("Fragment - " + this.getClass().getSimpleName() + " - onAttach(Activity activity)");
        }
        super.onAttach(activity);

        this.activity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (isLogFragmentLife) {
            LogUtils.d("Fragment - " + this.getClass().getSimpleName() + " - onCreate(Bundle savedInstanceState)");
        }
        super.onCreate(savedInstanceState);

        this.application = activity.getApplication();
        this.screenWidth = ScreenUtils.getScreenWidth();
        this.screenHeight = ScreenUtils.getScreenHeight();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (isLogFragmentLife) {
            LogUtils.d("Fragment - " + this.getClass().getSimpleName() + " - onViewCreated(View view, Bundle savedInstanceState)");
        }
        super.onViewCreated(view, savedInstanceState);

        this.isCreateed = true;
        if (rootLayout == null) {
            rootLayout = view;
        }
        /* 初始化界面数据 */
        if (isInitViewHDB) {
            initViewHDB(view, savedInstanceState);
            isInitViewHDB = false;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (isLogFragmentLife) {
            LogUtils.d("Fragment - " + this.getClass().getSimpleName() + " - onActivityCreated(Bundle savedInstanceState)");
        }
        super.onActivityCreated(savedInstanceState);

        if (isInitDataHDB) {
            initDataHDB(savedInstanceState);
            isInitDataHDB = false;
        }
    }

    @Override
    public void onStart() {
        if (isLogFragmentLife) {
            LogUtils.d("Fragment - " + this.getClass().getSimpleName() + " - onStart()");
        }
        super.onStart();
    }

    @Override
    public void onResume() {
        if (isLogFragmentLife) {
            LogUtils.d("Fragment - " + this.getClass().getSimpleName() + " - onResume()");
        }
        super.onResume();

        if (getUserVisibleHint() && isCreateed) {
            onRefreshHDB();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isLogFragmentLife) {
            LogUtils.d("Fragment - " + this.getClass().getSimpleName() + " - setUserVisibleHint(" + isVisibleToUser + ")");
        }
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && isCreateed) {
            onVisiableHDB();
            if (isLazyLoadHDB) {
                onLazyLoadHDB();
                isLazyLoadHDB = false;
            }
        }
    }

    @Override
    public void onPause() {
        if (isLogFragmentLife) {
            LogUtils.d("Fragment - " + this.getClass().getSimpleName() + " - onPause()");
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        if (isLogFragmentLife) {
            LogUtils.d("Fragment - " + this.getClass().getSimpleName() + " - onStop()");
        }
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        if (isLogFragmentLife) {
            LogUtils.d("Fragment - " + this.getClass().getSimpleName() + " - onDestroyView()");
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (isLogFragmentLife) {
            LogUtils.d("Fragment - " + this.getClass().getSimpleName() + " - onDestroy()");
        }
        onFinishing();

        EventBus.getDefault().unregister(this);

        this.isCreateed = false;
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        if (isLogFragmentLife) {
            LogUtils.d("Fragment - " + this.getClass().getSimpleName() + " - onDetach()");
        }
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (isLogFragmentLife) {
            LogUtils.d("Fragment - " + this.getClass().getSimpleName() + " - onActivityResult(" + requestCode + "," + resultCode + ") data.size = " + ((data == null || data.getExtras() == null) ? 0 : data.getExtras().size()));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void initViewHDB(View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void initDataHDB(@Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onRefreshHDB() {
    }

    @Override
    public void onLazyLoadHDB() {
    }

    @Override
    public void onVisiableHDB() {
    }

    @Override
    public void onFinishing() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessageEventOnMain(BaseMessageEvent baseMessageEvent) {
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void getMessageEventOnBackground(BaseMessageEvent baseMessageEvent) {
    }
}
