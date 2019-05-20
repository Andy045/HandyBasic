package com.handy.basic.utils;

import android.app.Activity;

import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;

/**
 * Activity栈工具类
 *
 * @author LiuJie https://github.com/Handy045
 * @description functional description.
 * @date Created in 2019/2/27 16:53
 * @modified By liujie
 */
public final class ActivityStackUtils {

    /**
     * Activity栈
     */
    private static Stack<Activity> activityStack;

    private ActivityStackUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public static Activity getCurrent() {
        if (activityStack != null) {
            return activityStack.lastElement();
        }
        return null;
    }

    /**
     * 添加Activity到堆栈
     */
    public static void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public static void finish() {
        if (activityStack != null) {
            Activity activity = activityStack.lastElement();
            finishChoiceDesc(activity);
        }
    }

    /**
     * 按顺序单次结束指定的Activity
     */
    public static void finishChoiceAsc(Activity activity) {
        if (activity != null && activityStack != null) {
            activityStack.remove(activity);
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 按倒序单次结束指定的Activity
     */
    public static void finishChoiceDesc(Activity activity) {
        if (activity != null && activityStack != null) {
            /*倒序排列*/
            Collections.reverse(activityStack);
            activityStack.remove(activity);
            if (!activity.isFinishing()) {
                activity.finish();
            }
            /*排列还原*/
            Collections.reverse(activityStack);
        }
    }

    /**
     * 结束全部指定的Activit
     */
    public static void finishChoices(Activity activity) {
        if (activity != null && activityStack != null) {
            Iterator iterator = activityStack.iterator();
            while (iterator.hasNext()) {
                Activity aty = (Activity) iterator.next();
                if (aty.getClass().equals(activity.getClass())) {
                    iterator.remove();
                    if (!aty.isFinishing()) {
                        aty.finish();
                    }
                }
            }
        }
    }

    /**
     * 按顺序单次结束指定的Activity
     */
    public static void finishChoiceAsc(Class<?> cls) {
        if (activityStack != null) {
            Iterator iterator = activityStack.iterator();
            while (iterator.hasNext()) {
                Activity aty = (Activity) iterator.next();
                if (aty.getClass().equals(cls)) {
                    iterator.remove();
                    if (!aty.isFinishing()) {
                        aty.finish();
                    }
                    break;
                }
            }
        }
    }

    /**
     * 按倒序单次结束指定的Activity
     */
    public static void finishChoiceDesc(Class<?> cls) {
        if (activityStack != null) {
            /*倒序排列*/
            Collections.reverse(activityStack);
            Iterator iterator = activityStack.iterator();
            while (iterator.hasNext()) {
                Activity aty = (Activity) iterator.next();
                if (aty.getClass().equals(cls)) {
                    iterator.remove();
                    if (!aty.isFinishing()) {
                        aty.finish();
                    }
                    break;
                }
            }
            /*排列还原*/
            Collections.reverse(activityStack);
        }
    }

    /**
     * 结束全部指定的Activit
     */
    public static void finishChoices(Class<?> cls) {
        if (activityStack != null) {
            Iterator iterator = activityStack.iterator();
            while (iterator.hasNext()) {
                Activity aty = (Activity) iterator.next();
                if (aty.getClass().equals(cls)) {
                    iterator.remove();
                    if (!aty.isFinishing()) {
                        aty.finish();
                    }
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public static void finishAll() {
        if (activityStack != null) {
            Iterator iterator = activityStack.iterator();
            while (iterator.hasNext()) {
                Activity aty = (Activity) iterator.next();
                iterator.remove();
                if (!aty.isFinishing()) {
                    aty.finish();
                }
            }
            activityStack.clear();
        }
    }

    /**
     * 结束第一个Activity之后的所有Activity
     */
    public static void finish2First() {
        if (activityStack != null) {
            Iterator iterator = activityStack.iterator();
            while (iterator.hasNext()) {
                Activity aty = (Activity) iterator.next();
                if (activityStack.size() > 1) {
                    iterator.remove();
                    if (!aty.isFinishing()) {
                        aty.finish();
                    }
                }
            }
        }
    }

    /**
     * 按顺序结束到指定的Activity
     */
    public static void finish2ChoiceAsc(Class<?> cls) {
        if (activityStack != null) {
            Iterator iterator = activityStack.iterator();
            while (iterator.hasNext()) {
                Activity aty = (Activity) iterator.next();
                if (!aty.getClass().equals(cls)) {
                    iterator.remove();
                    if (!aty.isFinishing()) {
                        aty.finish();
                    }
                } else {
                    break;
                }
            }
        }
    }

    /**
     * 按倒序结束到指定的Activity
     */
    public static void finish2ChoiceDesc(Class<?> cls) {
        if (activityStack != null) {
            /*倒序排列*/
            Collections.reverse(activityStack);
            Iterator iterator = activityStack.iterator();
            while (iterator.hasNext()) {
                Activity aty = (Activity) iterator.next();
                if (!aty.getClass().equals(cls)) {
                    iterator.remove();
                    if (!aty.isFinishing()) {
                        aty.finish();
                    }
                } else {
                    break;
                }
            }
            /*排列还原*/
            Collections.reverse(activityStack);
        }
    }

    public static Stack<Activity> getActivityStack() {
        return activityStack;
    }

    public static void setActivityStack(Stack<Activity> activityStack) {
        ActivityStackUtils.activityStack = activityStack;
    }
}
