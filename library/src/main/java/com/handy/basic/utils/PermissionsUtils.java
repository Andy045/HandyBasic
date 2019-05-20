package com.handy.basic.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.blankj.utilcode.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限扫描工具类
 *
 * @author LiuJie https://github.com/Handy045
 * @description 权限处理工具类，检查是否有未启用权限
 * @date Created in 2019/2/27 16:53
 * @modified By liujie
 */
public final class PermissionsUtils {

    private static ArrayList<String> PERMISSIONS = new ArrayList<>();

    private PermissionsUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化全校扫描内容
     */
    private static void initPermissions() {
        PERMISSIONS = new ArrayList<String>() {{
            add(Manifest.permission.INTERNET);
            add(Manifest.permission.READ_PHONE_STATE);
            add(Manifest.permission.ACCESS_WIFI_STATE);
            add(Manifest.permission.ACCESS_NETWORK_STATE);
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }};
    }

    public static ArrayList<String> getPermissions() {
        return PERMISSIONS;
    }

    public static void setPermissions(ArrayList<String> permissions) {
        PERMISSIONS = permissions;
    }

    /**
     * 设置应用敏感权限
     * <pre>
     * 设置后请通过public boolean checkDeniedPermissions(Activity activity, boolean isRequest)方法扫描权限。
     * 请注意：配置的权限内容必须与AndroidManifest中使用的敏感权限保持一致！
     *
     * 框架默认已配置的权限有：
     * add(Manifest.permission.INTERNET);
     * add(Manifest.permission.READ_PHONE_STATE);
     * add(Manifest.permission.ACCESS_WIFI_STATE);
     * add(Manifest.permission.ACCESS_NETWORK_STATE);
     * add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
     * </pre>
     */
    public static void addPermissions(List<String> permissions) {
        PERMISSIONS.addAll(permissions);
    }

    /**
     * 扫描权限集合 如果发现未启用权限，停止扫描并启动权限动态请求。
     *
     * @param activity  发起请求的Activity
     * @param isRequest 是否发起系统权限请求
     * @return true：权限检查通过，false：发现未允许的应用权限
     */
    public static boolean checkPermissions(Activity activity, boolean isRequest) {
        if (ObjectUtils.isEmpty(PERMISSIONS)) {
            initPermissions();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkPermissionsBase(activity, isRequest, PERMISSIONS);
        }
        return true;
    }

    /**
     * 扫描权限集合 如果发现未启用权限，停止扫描并启动权限动态请求。
     *
     * @param activity    发起请求的Activity
     * @param isRequest   是否发起系统权限请求
     * @param permissions 权限扫描的参数
     * @return true：权限检查通过，false：发现未允许的应用权限
     */
    public static boolean checkPermissions(Activity activity, List<String> permissions, boolean isRequest) {
        if (ObjectUtils.isEmpty(permissions)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return checkPermissionsBase(activity, isRequest, permissions);
            }
        }
        return true;
    }

    /**
     * 检查权限
     *
     * @param activity    发起请求的Activity
     * @param isRequest   是否要向Activity发起消息
     * @param permissions 需要扫描的权限内容
     * @return true：权限检查通过，false：发现未允许的应用权限
     */
    private static boolean checkPermissionsBase(Activity activity, boolean isRequest, List<String> permissions) {
        for (int index = 0; index < permissions.size(); index++) {
            if (ContextCompat.checkSelfPermission(activity, permissions.get(index)) == PackageManager.PERMISSION_DENIED) {
                if (isRequest) {
                    ActivityCompat.requestPermissions(activity, new String[]{permissions.get(index)}, index);
                }
                return false;
            }
        }
        return true;
    }
}
