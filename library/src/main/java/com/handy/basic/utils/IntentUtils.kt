package com.handy.basic.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle

/**
 * @title: IntentUtils
 * @projectName HandyBasicKT
 * @description: 意图相关工具类
 * @author LiuJie https://github.com/Handy045
 * @date Created in 2019-11-04 16:28
 */
class IntentUtils private constructor() {
    companion object {
        fun openActivity(activity: Activity, clss: Class<*>, isFinish: Boolean) {
            val intent = Intent(activity, clss)
            activity.startActivity(intent)
            if (isFinish) {
                activity.finish()
            }
        }

        fun openActivity(activity: Activity, clss: Class<*>, bundle: Bundle, isFinish: Boolean) {
            val intent = Intent(activity, clss)
            intent.putExtras(bundle)
            activity.startActivity(intent)
            if (isFinish) {
                activity.finish()
            }
        }

        fun openActivityForResult(
            activity: Activity,
            clss: Class<*>,
            requestCode: Int,
            isFinish: Boolean
        ) {
            val intent = Intent(activity, clss)
            activity.startActivityForResult(intent, requestCode)
            if (isFinish) {
                activity.finish()
            }
        }

        fun openActivityForResult(
            activity: Activity,
            clss: Class<*>,
            bundle: Bundle,
            requestCode: Int,
            isFinish: Boolean
        ) {
            val intent = Intent(activity, clss)
            intent.putExtras(bundle)
            activity.startActivityForResult(intent, requestCode)
            if (isFinish) {
                activity.finish()
            }
        }
    }
}
