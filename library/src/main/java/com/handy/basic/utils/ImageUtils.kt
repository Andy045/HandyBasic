package com.handy.basic.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.blankj.utilcode.util.LogUtils
import java.io.File

/**
 * @title: ImageUtils
 * @projectName HandyBasicKT
 * @description: 图片相关工具类
 * @author LiuJie https://github.com/Handy045
 * @date Created in 2019-11-04 16:49
 */
class ImageUtils private constructor() {
    companion object {
        /**
         * 通过代码改变图片颜色
         *
         * @param bitmap    原图片
         * @param tintColor 目标颜色 (16进制)
         * @return 改色后的Bitmap
         */
        fun tintBitmap(bitmap: Bitmap, tintColor: Int): Bitmap? {
            val bitmap1 = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
            val canvas = Canvas(bitmap1)
            val paint = Paint()
            paint.colorFilter = PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(bitmap, 0f, 0f, paint)
            return bitmap1
        }

        /**
         * 按目标宽高所计算的宽高比压缩图片
         *
         * @param filePath     原图片路径
         * @param targetWidth  目标宽度（建议值：360）
         * @param targetHeight 目标高度（建议值：640）
         * @param recycle      是否回收
         * @return 压缩后的图片
         */
        fun compressByScale(
            filePath: String,
            targetWidth: Int,
            targetHeight: Int,
            recycle: Boolean
        ): Bitmap? {
            if (filePath.isEmpty()) {
                LogUtils.e("图片路径为空")
                return null
            }
            val barcode = BitmapFactory.decodeFile(filePath)
            return if (isEmptyBitmap(barcode)) null else compressByScale(
                barcode,
                targetWidth,
                targetHeight,
                recycle
            )
        }

        /**
         * 按目标宽高所计算的宽高比压缩图片
         *
         * @param file         原图片文件
         * @param targetWidth  目标宽度（建议值：360）
         * @param targetHeight 目标高度（建议值：640）
         * @param recycle      是否回收
         * @return 压缩后的图片
         */
        fun compressByScale(
            file: File,
            targetWidth: Int,
            targetHeight: Int,
            recycle: Boolean
        ): Bitmap? {
            return compressByScale(file.path, targetWidth, targetHeight, recycle)
        }

        /**
         * 按目标宽高所计算的宽高比压缩图片
         *
         * @param mBitmap      原图片BitMap
         * @param targetWidth  目标宽度（建议值：360）
         * @param targetHeight 目标高度（建议值：640）
         * @param recycle      是否回收
         * @return 压缩后的图片
         */
        fun compressByScale(
            mBitmap: Bitmap,
            targetWidth: Int,
            targetHeight: Int,
            recycle: Boolean
        ): Bitmap? {
            if (targetWidth == 0 || targetHeight == 0) {
                return null
            }

            //计算图片宽高与制定宽高的最大比例
            val scale: Double
            var bitmap: Bitmap? = null
            if (mBitmap.width < mBitmap.height) {
                if (mBitmap.width <= targetWidth || mBitmap.height <= targetHeight) {
                    scale = 1.0
                } else {
                    val widthScale = mBitmap.width.toDouble() / targetWidth.toDouble()
                    val heightScale = mBitmap.height.toDouble() / targetHeight.toDouble()
                    scale = if (widthScale > heightScale) widthScale else heightScale
                }
            } else {
                if (mBitmap.width <= targetHeight || mBitmap.height <= targetWidth) {
                    scale = 1.0
                } else {
                    val widthScale = mBitmap.width.toDouble() / targetHeight.toDouble()
                    val heightScale = mBitmap.height.toDouble() / targetWidth.toDouble()
                    scale = if (widthScale > heightScale) widthScale else heightScale
                }
            }

            // 如果缩放倍数大于1则对Bitmap实例进行压缩，如果小于1则无需压缩
            if (scale > 1) {
                val imgWidth = (mBitmap.width / scale).toInt()
                val imgHeight = (mBitmap.height / scale).toInt()

                bitmap = Bitmap.createScaledBitmap(mBitmap, imgWidth, imgHeight, true)
            }

            return if (bitmap == null) {
                mBitmap
            } else {
                if (recycle && !mBitmap.isRecycled) {
                    mBitmap.recycle()
                }
                bitmap
            }
        }

        /**
         * 添加文字水印
         *
         * @param src      源图片
         * @param content  水印文本
         * @param textSize 水印字体大小
         * @param color    水印字体颜色
         * @param x        起始坐标x
         * @param y        起始坐标y
         * @return 带有文字水印的图片
         */
        fun addTextWatermark(
            src: Bitmap,
            content: String,
            textSize: Int,
            color: Int,
            x: Float,
            y: Float
        ): Bitmap? {
            return addTextWatermark(src, content, textSize.toFloat(), color, x, y, false)
        }

        /**
         * 添加文字水印
         *
         * @param src       源图片
         * @param content   水印文本
         * @param textScale 水印字体比例（字体大小 = 照片高度 / 字体比例）
         * @param color     水印字体颜色
         * @param x         起始坐标x
         * @param y         起始坐标y
         * @return 带有文字水印的图片
         */
        fun addTextWatermarkScale(
            src: Bitmap,
            content: String,
            textScale: Int,
            color: Int,
            x: Float,
            y: Float
        ): Bitmap? {
            return addTextWatermark(
                src,
                content,
                (src.height / textScale).toFloat(),
                color,
                x,
                y,
                false
            )
        }

        /**
         * 添加文字水印，支持换行
         *
         * @param src      源图片
         * @param content  水印文本
         * @param textSize 水印字体大小
         * @param color    水印字体颜色
         * @param x        起始坐标x
         * @param y        起始坐标y
         * @param recycle  是否回收
         * @return 带有文字水印的图片
         */
        fun addTextWatermark(
            src: Bitmap,
            content: String,
            textSize: Float,
            color: Int,
            x: Float,
            y: Float,
            recycle: Boolean
        ): Bitmap? {
            if (isEmptyBitmap(src)) {
                return null
            }
            val ret = src.copy(src.config, true)
            val paint = TextPaint(Paint.ANTI_ALIAS_FLAG)
            val canvas = Canvas(ret)
            paint.color = color
            paint.textSize = textSize
            val bounds = Rect()
            paint.getTextBounds(content, 0, content.length, bounds)
            val layout = StaticLayout(
                content,
                paint,
                (ret.width - x).toInt(),
                Layout.Alignment.ALIGN_NORMAL,
                1.0f,
                0.0f,
                true
            )
            canvas.translate(x, y)
            layout.draw(canvas)
            if (recycle && !src.isRecycled) {
                src.recycle()
            }
            return ret
        }

        /**
         * 添加图片水印
         *
         * @param src       源图片
         * @param watermark 图片水印
         * @param x         起始坐标x
         * @param y         起始坐标y
         * @param alpha     透明度
         * @return 带有图片水印的图片
         */
        fun addImageWatermark(src: Bitmap, watermark: Bitmap, x: Int, y: Int, alpha: Int): Bitmap? {
            return addImageWatermark(src, watermark, x, y, alpha, false)
        }

        /**
         * 添加图片水印
         *
         * @param src       源图片
         * @param watermark 图片水印
         * @param x         起始坐标x
         * @param y         起始坐标y
         * @param alpha     透明度
         * @param recycle   是否回收
         * @return 带有图片水印的图片
         */
        fun addImageWatermark(
            src: Bitmap,
            watermark: Bitmap,
            x: Int,
            y: Int,
            alpha: Int,
            recycle: Boolean
        ): Bitmap? {
            if (isEmptyBitmap(src)) {
                return null
            }
            val ret = src.copy(src.config, true)
            if (!isEmptyBitmap(watermark)) {
                val paint = Paint(Paint.ANTI_ALIAS_FLAG)
                val canvas = Canvas(ret)
                paint.alpha = alpha
                canvas.drawBitmap(watermark, x.toFloat(), y.toFloat(), paint)
            }
            if (recycle && !src.isRecycled) {
                src.recycle()
            }
            return ret
        }

        /**
         * 判断bitmap对象是否为空
         *
         * @param src 源图片
         * @return `true`: 是<br></br>`false`: 否
         */
        private fun isEmptyBitmap(src: Bitmap?): Boolean {
            return src == null || src.width == 0 || src.height == 0
        }

        /**
         * 通过代码改变图片颜色
         *
         * @param context    上下文
         * @param idDrawable 原图片
         * @param tintColor  目标颜色 (16进制)
         * @return 改色后的Drawable
         */
        fun tintDrawable(context: Context, @DrawableRes idDrawable: Int, @ColorRes tintColor: Int): Drawable? {
            if (idDrawable == 0) {
                return null
            }
            val drawable = context.resources.getDrawable(idDrawable).mutate()
            val inBitmap = com.blankj.utilcode.util.ImageUtils.drawable2Bitmap(drawable)
            val outBitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                inBitmap.config
            )
            val canvas = Canvas(outBitmap)
            val paint = Paint()
            paint.colorFilter =
                PorterDuffColorFilter(context.resources.getColor(tintColor), PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(inBitmap, 0f, 0f, paint)
            return BitmapDrawable(null, outBitmap)
        }
    }
}