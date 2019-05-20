package com.handy.basic.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.blankj.utilcode.util.ObjectUtils;

import java.io.File;

/**
 * 图片相关工具类
 *
 * @author LiuJie https://github.com/Handy045
 * @description functional description.
 * @date Created in 2019/2/27 16:53
 * @modified By liujie
 */
public class ImageUtils {

    private ImageUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 通过代码改变图片颜色
     *
     * @param bitmap    原图片
     * @param tintColor 目标颜色 (16进制)
     * @return 改色后的Bitmap
     */
    public static Bitmap tintBitmap(Bitmap bitmap, int tintColor) {
        if (ObjectUtils.isEmpty(bitmap)) {
            return null;
        }
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(bitmap1);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return bitmap1;
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
    public static Bitmap compressByScale(@NonNull String filePath, int targetWidth, int targetHeight, boolean recycle) {
        if (ObjectUtils.isEmpty(filePath)) {
            return null;
        }
        Bitmap barcode = BitmapFactory.decodeFile(filePath);
        if (isEmptyBitmap(barcode)) {
            return null;
        }
        return compressByScale(barcode, targetWidth, targetHeight, recycle);
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
    public static Bitmap compressByScale(@NonNull File file, int targetWidth, int targetHeight, boolean recycle) {
        if (ObjectUtils.isEmpty(file)) {
            return null;
        }
        Bitmap barcode = BitmapFactory.decodeFile(file.getPath());
        if (isEmptyBitmap(barcode)) {
            return null;
        }
        return compressByScale(barcode, targetWidth, targetHeight, recycle);
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
    public static Bitmap compressByScale(@NonNull Bitmap mBitmap, int targetWidth, int targetHeight, boolean recycle) {
        if (targetWidth == 0 || targetHeight == 0) {
            return null;
        }

        //计算图片宽高与制定宽高的最大比例
        double scale;
        Bitmap bitmap = null;
        if (mBitmap.getWidth() < mBitmap.getHeight()) {
            if (mBitmap.getWidth() <= targetWidth || mBitmap.getHeight() <= targetHeight) {
                scale = 1;
            } else {
                double widthScale = (double) mBitmap.getWidth() / (double) targetWidth;
                double heightScale = (double) mBitmap.getHeight() / (double) targetHeight;
                scale = widthScale > heightScale ? widthScale : heightScale;
            }
        } else {
            if (mBitmap.getWidth() <= targetHeight || mBitmap.getHeight() <= targetWidth) {
                scale = 1;
            } else {
                double widthScale = (double) mBitmap.getWidth() / (double) targetHeight;
                double heightScale = (double) mBitmap.getHeight() / (double) targetWidth;
                scale = widthScale > heightScale ? widthScale : heightScale;
            }
        }

        // 如果缩放倍数大于1则对Bitmap实例进行压缩，如果小于1则无需压缩
        if (scale > 1) {
            int imgWidth = (int) (mBitmap.getWidth() / scale);
            int imgHeight = (int) (mBitmap.getHeight() / scale);

            bitmap = Bitmap.createScaledBitmap(mBitmap, imgWidth, imgHeight, true);
        }

        if (bitmap == null) {
            return mBitmap;
        } else {
            if (recycle && !mBitmap.isRecycled()) {
                mBitmap.recycle();
            }
            return bitmap;
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
    public static Bitmap addTextWatermark(final Bitmap src, final String content, final int textSize, final int color, final float x, final float y) {
        return addTextWatermark(src, content, textSize, color, x, y, false);
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
    public static Bitmap addTextWatermarkScale(final Bitmap src, final String content, final int textScale, final int color, final float x, final float y) {
        return addTextWatermark(src, content, src.getHeight() / textScale, color, x, y, false);
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
    public static Bitmap addTextWatermark(final Bitmap src, final String content, final float textSize, final int color, final float x, final float y, final boolean recycle) {
        if (isEmptyBitmap(src) || content == null) {
            return null;
        }
        Bitmap ret = src.copy(src.getConfig(), true);
        TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        Canvas canvas = new Canvas(ret);
        paint.setColor(color);
        paint.setTextSize(textSize);
        Rect bounds = new Rect();
        paint.getTextBounds(content, 0, content.length(), bounds);
        StaticLayout layout = new StaticLayout(content, paint, (int) (ret.getWidth() - x), Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
        canvas.translate(x, y);
        layout.draw(canvas);
        if (recycle && !src.isRecycled()) {
            src.recycle();
        }
        return ret;
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
    public static Bitmap addImageWatermark(final Bitmap src, final Bitmap watermark, final int x, final int y, final int alpha) {
        return addImageWatermark(src, watermark, x, y, alpha, false);
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
    public static Bitmap addImageWatermark(final Bitmap src, final Bitmap watermark, final int x, final int y, final int alpha, final boolean recycle) {
        if (isEmptyBitmap(src)) {
            return null;
        }
        Bitmap ret = src.copy(src.getConfig(), true);
        if (!isEmptyBitmap(watermark)) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            Canvas canvas = new Canvas(ret);
            paint.setAlpha(alpha);
            canvas.drawBitmap(watermark, x, y, paint);
        }
        if (recycle && !src.isRecycled()) {
            src.recycle();
        }
        return ret;
    }

    /**
     * 判断bitmap对象是否为空
     *
     * @param src 源图片
     * @return {@code true}: 是<br>{@code false}: 否
     */
    private static boolean isEmptyBitmap(final Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

    /**
     * 通过代码改变图片颜色
     *
     * @param context    上下文
     * @param idDrawable 原图片
     * @param tintColor  目标颜色 (16进制)
     * @return 改色后的Drawable
     */
    public static Drawable tintDrawable(Context context, @DrawableRes int idDrawable, @ColorRes int tintColor) {
        if (idDrawable == 0) {
            return null;
        }
        Drawable drawable = context.getResources().getDrawable(idDrawable).mutate();
        Bitmap inBitmap = com.blankj.utilcode.util.ImageUtils.drawable2Bitmap(drawable);
        Bitmap outBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), inBitmap.getConfig());
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(tintColor), PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(inBitmap, 0, 0, paint);
        return new BitmapDrawable(null, outBitmap);
    }
}
