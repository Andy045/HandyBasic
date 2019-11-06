package com.handy.basic.utils

import android.app.Activity
import java.util.*

/**
 * @title: ActivityStackUtils
 * @projectName HandyBasicKT
 * @description: Activity堆栈手动管理工具类
 * @author LiuJie https://github.com/Handy045
 * @date Created in 2019-11-01 15:30
 */
class ActivityStackUtils private constructor() {
    companion object {
        private var activityStack: Stack<Activity> = Stack()

        /**
         * 获取当前Activity
         */
        fun getCurrent(): Activity? {
            return if (!activityStack.isNullOrEmpty()) activityStack.lastElement() else null
        }

        /**
         * 结束当前Activity
         */
        fun finishCurrent() {
            if (!activityStack.isNullOrEmpty()) {
                Collections.reverse(activityStack)
                val activity = activityStack.lastElement()
                activityStack.remove(activity)
                if (!activity.isFinishing) {
                    activity.finish()
                }
                Collections.reverse(activityStack)
            }
        }

        /**
         * 重载当前Activity
         */
        fun reloadCurrent() {
            if (!activityStack.isNullOrEmpty()) {
                val activity = activityStack.lastElement()
                val intent = activity.intent
                activity.finish()
                activity.startActivity(intent)
            }
        }

        /**
         * 添加一个Activity
         */
        fun addActivity(activity: Activity) {
            if (activityStack.isNullOrEmpty()) {
                activityStack = Stack()
            }
            activityStack.add(activity)
        }

        /**
         * 倒序中单次结束指定的Activity
         */
        fun finishChoiceDesc(activity: Activity) {
            if (!activityStack.isNullOrEmpty()) {
                Collections.reverse(activityStack)
                finishChoiceAsc(activity)
                Collections.reverse(activityStack)
            }
        }

        /**
         * 正序中单次结束指定的Activity
         */
        fun finishChoiceAsc(activity: Activity) {
            if (!activityStack.isNullOrEmpty()) {
                activityStack.remove(activity)
                if (!activity.isFinishing) {
                    activity.finish()
                }
            }
        }

        /**
         * 倒序中移除到指定的Activity
         */
        fun finishToChoiceDesc(activity: Activity) {
            if (!activityStack.isNullOrEmpty()) {
                Collections.reverse(activityStack)
                finishToChoiceAsc(activity)
                Collections.reverse(activityStack)
            }
        }

        /**
         * 升序中移除到指定的Activity
         */
        fun finishToChoiceAsc(activity: Activity) {
            if (!activityStack.isNullOrEmpty()) {
                val iterator = activityStack.iterator()
                while (iterator.hasNext()) {
                    val aty = iterator.next()
                    if (aty.javaClass != activity.javaClass) {
                        iterator.remove()
                        if (!aty.isFinishing) {
                            aty.finish()
                        }
                    } else {
                        break
                    }
                }
            }
        }

        /**
         * 移除所有指定的Activity
         */
        fun finishChoiceAll(activity: Activity) {
            if (!activityStack.isNullOrEmpty()) {
                val iterator = activityStack.iterator()
                while (iterator.hasNext()) {
                    val aty = iterator.next()
                    if (aty.javaClass == activity.javaClass) {
                        iterator.remove()
                        if (!aty.isFinishing) {
                            aty.finish()
                        }
                    }
                }
            }
        }

        /**
         * 移除到第一个Activity
         */
        fun finishToFirst() {
            if (!activityStack.isNullOrEmpty()) {
                val iterator = activityStack.iterator()
                while (iterator.hasNext()) {
                    val aty = iterator.next()
                    if (activityStack.size > 1) {
                        iterator.remove()
                        if (!aty.isFinishing) {
                            aty.finish()
                        }
                    }
                }
            }
        }

        /**
         * 移除全部的Activity
         */
        fun finishAll() {
            if (!activityStack.isNullOrEmpty()) {
                val iterator = activityStack.iterator()
                while (iterator.hasNext()) {
                    val aty = iterator.next()
                    iterator.remove()
                    if (!aty.isFinishing) {
                        aty.finish()
                    }
                }
                activityStack.clear()
            }
        }
    }
}