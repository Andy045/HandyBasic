package com.handy.basic.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.handy.base.R;
import com.handy.base.evenbus.BaseMessageEvent;
import com.handy.base.utils.ActivityStackUtils;
import com.handy.base.utils.PermissionsUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * BaseActivity
 *
 * @author LiuJie https://github.com/Handy045
 * @description functional description.
 * @date Created in 2019/2/27 16:53
 * @modified By liujie
 */
public abstract class BaseActivity extends RxAppCompatActivity implements BaseApplicationApi.BaseActivityApi {

    //============================================================
    //  功能配置
    //============================================================

    /**
     * onStart中初始化界面视图
     */
    private boolean isInitViewHDB = true;
    /**
     * onStart中初始化界面数据
     */
    private boolean isInitDataHDB = true;
    /**
     * onResume中界面请求处理
     */
    private boolean isOnRequestHDB = true;
    /**
     * onStart中初始化意图内容
     */
    private boolean isInitIntentBundle = true;

    //============================================================
    //  开放配置
    //============================================================

    /**
     * 是否Log打印Activity的生命周期
     */
    public boolean isLogActivityLife = false;
    /**
     * 是否启用权限扫描功能
     */
    public boolean isCheckPermissions = false;
    /**
     * 是否使用滑动返回功能
     */
    public boolean isUseSwipeBackFinish = true;

    //============================================================
    //  全局变量
    //============================================================

    /**
     * 屏幕宽度
     */
    public int screenWidth = 0;
    /**
     * 屏幕高度
     */
    public int screenHeight = 0;
    /**
     * 界面视图布局
     */
    public View rootLayout = null;

    public Context context;
    public BaseActivity activity;
    public Application application;

    private Bundle savedInstanceState = null;
    private BGASwipeBackHelper mSwipeBackHelper;

    @Override
    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (isLogActivityLife) {
            LogUtils.d("Activity - " + this.getClass().getSimpleName() + " - onCreate()");
        }
        super.onCreate(savedInstanceState);

        try {
            this.context = this;
            this.activity = this;
            this.application = getApplication();
            this.savedInstanceState = savedInstanceState;
            this.screenWidth = ScreenUtils.getScreenWidth();
            this.screenHeight = ScreenUtils.getScreenHeight();

            initSwipeBackFinish();
            ActivityStackUtils.addActivity(this);
            EventBus.getDefault().register(this);

            /* 初始化Activity接收意图的内容 */
            if (isInitIntentBundle && getIntent() != null && getIntent().getExtras() != null) {
                initIntentBundle(getIntent().getExtras());
                isInitIntentBundle = false;
            }

            /* 安卓权限扫描 */
            if (isCheckPermissions) {
                checkPermissionsHDB();
                isCheckPermissions = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @CallSuper
    protected void onStart() {
        if (isLogActivityLife) {
            LogUtils.d("Activity - " + this.getClass().getSimpleName() + " - onStart()");
        }
        super.onStart();

        if (!isCheckPermissions) {
            /* 初始化界面视图 */
            if (isInitViewHDB) {
                initViewHDB(savedInstanceState);
                isInitViewHDB = false;
            }
            /* 初始化界面数据 */
            if (isInitDataHDB) {
                initDataHDB();
                isInitDataHDB = false;
            }
        }
    }

    @Override
    @CallSuper
    protected void onResume() {
        if (isLogActivityLife) {
            LogUtils.d("Activity - " + this.getClass().getSimpleName() + " - onResume()");
        }
        super.onResume();

        /* Activity刷新时调用 */
        onRefreshHDB();

        /* Activity请求时调用（可被重复调用） */
        if (isOnRequestHDB) {
            initRequestHDB();
            isOnRequestHDB = false;
        }
    }

    @Override
    @CallSuper
    protected void onPause() {
        if (isLogActivityLife) {
            LogUtils.d("Activity - " + this.getClass().getSimpleName() + " - onPause()");
        }
        super.onPause();

        if (isFinishing()) {
            onFinishing();

            KeyboardUtils.hideSoftInput(activity);
            EventBus.getDefault().unregister(this);
            ActivityStackUtils.finishChoiceDesc(this);
        }
    }

    @Override
    @CallSuper
    protected void onStop() {
        if (isLogActivityLife) {
            LogUtils.d("Activity - " + this.getClass().getSimpleName() + " - onStop()");
        }
        super.onStop();
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        if (isLogActivityLife) {
            LogUtils.d("Activity - " + this.getClass().getSimpleName() + " - onDestroy()");
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (isLogActivityLife) {
            LogUtils.d("Activity - " + this.getClass().getSimpleName() + " - onActivityResult(" + requestCode + "," + resultCode + ") data.size = " + ((data == null || data.getExtras() == null) ? 0 : data.getExtras().size()));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 应用权限判断处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (PermissionsUtils.checkPermissions(activity, true)) {
                onPermissionSuccessHDB();
            }
        } else {
            onPermissionRejectionHDB(permissions);
        }
    }

    /**
     * 初始化滑动返回功能。此功能的启用控制可在 {@link Activity#onCreate(Bundle)} 的 super.onCreate(savedInstanceState) 之前或构造方法中设置全局变量 {@link BaseActivity#isUseSwipeBackFinish}
     */
    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, new BGASwipeBackHelper.Delegate() {
            @Override
            public boolean isSupportSwipeBack() {
                return isUseSwipeBackFinish;
            }

            @Override
            public void onSwipeBackLayoutSlide(float slideOffset) {

            }

            @Override
            public void onSwipeBackLayoutCancel() {

            }

            @Override
            public void onSwipeBackLayoutExecuted() {
                mSwipeBackHelper.swipeBackward();
            }
        });
        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackManager.getInstance().init(this) 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。
        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f);
    }

    @Override
    public boolean setContentViewHDB(@LayoutRes int layoutResId) {
        rootLayout = LayoutInflater.from(context).inflate(layoutResId, null);
        if (rootLayout != null) {
            setContentView(rootLayout);
            return true;
        }
        return false;
    }

    @Override
    public void initIntentBundle(Bundle bundle) {
    }

    @Override
    public void checkPermissionsHDB() {
        if (PermissionsUtils.checkPermissions(activity, true)) {
            onPermissionSuccessHDB();
        }
    }

    @Override
    public void initViewHDB(@Nullable Bundle savedInstanceState) {
    }

    @Override
    public void initDataHDB() {
    }

    @Override
    public void onRefreshHDB() {
    }

    @Override
    public void initRequestHDB() {
    }

    @Override
    public void onFinishing() {
    }

    @Override
    public void onPermissionSuccessHDB() {
        if (isCheckPermissions) {
            /* 初始化界面视图 */
            if (isInitViewHDB) {
                initViewHDB(savedInstanceState);
                isInitViewHDB = false;
            }
            /* 初始化界面数据 */
            if (isInitDataHDB) {
                initDataHDB();
                isInitDataHDB = false;
            }
        }
    }

    @Override
    public void onPermissionRejectionHDB(String[] permissions) {
        if (permissions.length > 0) {
            LogUtils.d(permissions[0] + " requires permission which may be rejected by use.");
        }
        /*
         * // 发现未启用的权限时，可以参考一下进行处理。
         * // 弹出Dialog，提示用户需要开启权限，引导用户进入应用设置界面手动开启，返回到
         * SweetDialogUT.showNormalDialog((BaseActivity) activity, "发现未启用权限", "为保障应用正常使用，请开启应用权限", "开启", "退出", new SweetAlertDialog.OnSweetClickListener() {
         *     @Override
         *     public void onClick(SweetAlertDialog sweetAlertDialog) {
         *         PrintfUT.showShortToast(context, "请在手机设置权限管理中启用开启此应用系统权限");
         *         Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
         *         intent.setData(Uri.parse("package:" + getPackageName()));
         *         startActivityForResult(intent, 45);
         *         sweetAlertDialog.dismiss();
         *     }
         * }, new SweetAlertDialog.OnSweetClickListener() {
         *     @Override
         *     public void onClick(SweetAlertDialog sweetAlertDialog) {
         *         sweetAlertDialog.dismiss();
         *         ActivityStackUtils.AppExit(context);
         *     }
         * }).setCancelable(false);
         *
         * // 若从设置界面返回，重新扫描权限（请将此方法放与onActivityPermissionRejection()同级）
         * @Override
         * protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         *     super.onActivityResult(requestCode, resultCode, data);
         *     if (requestCode == 45) {
         *         PermissionsUtils.checkDeniedPermissions(activity, true);
         *     }
         * }
         */
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessageEventOnMain(BaseMessageEvent baseMessageEvent) {
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void getMessageEventOnBackground(BaseMessageEvent baseMessageEvent) {
    }
}