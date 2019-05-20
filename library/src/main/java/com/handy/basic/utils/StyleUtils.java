package com.handy.basic.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

/**
 * 样式效果工具类
 *
 * @author LiuJie https://github.com/Handy045
 * @description File Description
 * @date Created in 2019-04-25 08:49
 * @modified By liujie
 */
public class StyleUtils {

    private StyleUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 通过代码设置Selector效果
     *
     * @param context   上下文
     * @param idNormal  默认样式（或者图片和颜色的资源ID）
     * @param idPressed 点击样式（或者图片和颜色的资源ID）
     * @param idFocused 焦点样式（或者图片和颜色的资源ID）
     * @return Selector样式
     */
    public static StateListDrawable getStateDrawable(@NonNull Context context, @DrawableRes int idNormal, @DrawableRes int idPressed, @DrawableRes int idFocused) {
        Drawable normal = idNormal == 0 ? null : context.getResources().getDrawable(idNormal);
        Drawable pressed = idPressed == 0 ? null : context.getResources().getDrawable(idPressed);
        Drawable focus = idFocused == 0 ? null : context.getResources().getDrawable(idFocused);
        return getStateDrawable(normal, pressed, focus);
    }

    /**
     * 通过代码设置Selector效果
     *
     * @return Selector样式
     */
    public static StateListDrawable getStateDrawable(Drawable normal, Drawable pressed, Drawable focus) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, focus);
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        stateListDrawable.addState(new int[]{android.R.attr.state_focused}, focus);
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressed);
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled}, normal);
        stateListDrawable.addState(new int[]{}, normal);
        return stateListDrawable;
    }

    /**
     * 通过代码设置Selector效果
     *
     * @param context   上下文
     * @param idNormal  默认样式（或者图片和颜色的资源ID）
     * @param idPressed 点击样式（或者图片和颜色的资源ID）
     * @param idFocused 焦点样式（或者图片和颜色的资源ID）
     * @return Selector样式
     */
    public static ColorStateList getStateColor(@NonNull Context context, @ColorRes int idNormal, @ColorRes int idPressed, @ColorRes int idFocused) {
        int normal = context.getResources().getColor(idNormal);
        int pressed = context.getResources().getColor(idPressed);
        int focus = context.getResources().getColor(idFocused);
        return getStateColor(normal, pressed, focus);
    }

    /**
     * 通过代码设置Selector效果
     *
     * @return Selector样式
     */
    public static ColorStateList getStateColor(@ColorInt int normal, @ColorInt int pressed, @ColorInt int focus) {
        int[] colors = new int[]{focus, pressed, focus, pressed, normal, normal};
        int[][] states = new int[6][];
        states[0] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[1] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[2] = new int[]{android.R.attr.state_focused};
        states[3] = new int[]{android.R.attr.state_pressed};
        states[4] = new int[]{android.R.attr.state_enabled};
        states[5] = new int[]{};
        return new ColorStateList(states, colors);
    }
}
