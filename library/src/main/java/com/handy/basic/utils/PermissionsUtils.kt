package com.handy.basic.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * @title: PermissionsUtils
 * @projectName HandyBasicKT
 * @description: 权限扫描
 * @author LiuJie https://github.com/Handy045
 * @date Created in 2019-11-04 10:04
 */
class PermissionsUtils private constructor() {
    companion object {
        var permissions = mutableMapOf(
            Manifest.permission.INTERNET to Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE to Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_WIFI_STATE to Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE to Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE to Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        /**
         * 补充需要校验的权限
         */
        fun addPermission(string: String) {
            permissions[string] = string
        }

        /**
         * 补充需要校验的权限
         */
        fun addPermissions(strings: MutableList<String>) {
            if (strings.isNotEmpty()) {
                for (string: String in strings) {
                    permissions[string] = string
                }
            }
        }

        /**
         * 扫描权限集合 如果发现未启用权限，停止扫描并启动权限动态请求。
         *
         * @param activity  发起请求的Activity
         * @param isRequest 是否发起系统权限请求
         * @return true：权限检查通过，false：发现未允许的应用权限
         */
        fun checkPermissions(activity: Activity, isRequest: Boolean): Boolean {
            if (permissions.isNotEmpty()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return checkPermissionsBase(
                        activity,
                        isRequest,
                        permissions.values.toMutableList()
                    )
                }
            }
            return true
        }

        /**
         * 扫描权限集合 如果发现未启用权限，停止扫描并启动权限动态请求。
         *
         * @param activity    发起请求的Activity
         * @param isRequest   是否发起系统权限请求
         * @param permissions 权限扫描的参数
         * @return true：权限检查通过，false：发现未允许的应用权限
         */
        fun checkPermissions(
            activity: Activity,
            isRequest: Boolean,
            permissions: MutableList<String>
        ): Boolean {
            if (permissions.isNotEmpty()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return checkPermissionsBase(activity, isRequest, permissions)
                }
            }
            return true
        }

        /**
         * 检查权限
         *
         * @param activity    发起请求的Activity
         * @param isRequest   是否要向Activity发起消息
         * @param permissions 需要扫描的权限内容
         * @return true：权限检查通过，false：发现未允许的应用权限
         */
        private fun checkPermissionsBase(
            activity: Activity,
            isRequest: Boolean,
            permissions: MutableList<String>
        ): Boolean {
            for (index in permissions.indices) {
                val result = ContextCompat.checkSelfPermission(activity, permissions.get(index))
                if (result == PackageManager.PERMISSION_DENIED) {
                    if (isRequest) {
                        ActivityCompat.requestPermissions(
                            activity,
                            mutableListOf(permissions.get(index)).toTypedArray(),
                            index
                        )
                    }
                    return false
                }
            }
            return true
        }
    }
}