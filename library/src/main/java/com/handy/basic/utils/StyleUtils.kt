package com.handy.basic.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

/**
 * @title: StyleUtils
 * @projectName HandyBasicKT
 * @description: 样式效果工具类
 * @author LiuJie https://github.com/Handy045
 * @date Created in 2019-11-04 16:28
 */
class StyleUtils private constructor() {
    companion object {
        /**
         * 通过代码设置Selector效果
         *
         * @param context   上下文
         * @param idNormal  默认样式（或者图片和颜色的资源ID）
         * @param idPressed 点击样式（或者图片和颜色的资源ID）
         * @param idFocused 焦点样式（或者图片和颜色的资源ID）
         * @return Selector样式
         */
        fun getStateDrawable(context: Context, @DrawableRes idNormal: Int, @DrawableRes idPressed: Int, @DrawableRes idFocused: Int): StateListDrawable {
            val normal = if (idNormal == 0) null else ContextCompat.getDrawable(context, idNormal)
            val pressed =
                if (idPressed == 0) null else ContextCompat.getDrawable(context, idPressed)
            val focus = if (idFocused == 0) null else ContextCompat.getDrawable(context, idFocused)
            return getStateDrawable(normal, pressed, focus)
        }

        /**
         * 通过代码设置Selector效果
         *
         * @return Selector样式
         */
        fun getStateDrawable(
            normal: Drawable?,
            pressed: Drawable?,
            focus: Drawable?
        ): StateListDrawable {
            val stateListDrawable = StateListDrawable()
            stateListDrawable.addState(
                intArrayOf(
                    android.R.attr.state_enabled,
                    android.R.attr.state_focused
                ), focus
            )
            stateListDrawable.addState(
                intArrayOf(
                    android.R.attr.state_pressed,
                    android.R.attr.state_enabled
                ), pressed
            )
            stateListDrawable.addState(intArrayOf(android.R.attr.state_focused), focus)
            stateListDrawable.addState(intArrayOf(android.R.attr.state_pressed), pressed)
            stateListDrawable.addState(intArrayOf(android.R.attr.state_enabled), normal)
            stateListDrawable.addState(intArrayOf(), normal)
            return stateListDrawable
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
        fun getStateColor(context: Context, @ColorRes idNormal: Int, @ColorRes idPressed: Int, @ColorRes idFocused: Int): ColorStateList {
            val normal = ContextCompat.getColor(context, idNormal)
            val pressed = ContextCompat.getColor(context, idPressed)
            val focus = ContextCompat.getColor(context, idFocused)
            return getStateColor(normal, pressed, focus)
        }

        /**
         * 通过代码设置Selector效果
         *
         * @return Selector样式
         */
        fun getStateColor(@ColorInt normal: Int, @ColorInt pressed: Int, @ColorInt focus: Int): ColorStateList {
            val colors = intArrayOf(focus, pressed, focus, pressed, normal, normal)
            val states = arrayOfNulls<IntArray>(6)
            states[0] = intArrayOf(android.R.attr.state_enabled, android.R.attr.state_focused)
            states[1] = intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled)
            states[2] = intArrayOf(android.R.attr.state_focused)
            states[3] = intArrayOf(android.R.attr.state_pressed)
            states[4] = intArrayOf(android.R.attr.state_enabled)
            states[5] = intArrayOf()
            return ColorStateList(states, colors)
        }
    }
}