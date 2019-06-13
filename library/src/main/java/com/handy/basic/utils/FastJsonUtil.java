package com.handy.basic.utils;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * FastJson工具类
 *
 * @author LiuJie https://github.com/Handy045
 * @description File Description
 * @date Created in 2019-06-11 11:06
 * @modified By liujie
 */
public class FastJsonUtil {

    //============================================================
    // TODO 格式化JSONObject
    //============================================================

    /**
     * 格式化Json(美化)
     *
     * @return json
     */
    public static String format(String json) {
        return JSON.toJSONString(JSONObject.parseObject(json), SerializerFeature.PrettyFormat);
    }

    //============================================================
    // TODO 判断JSON类型
    //============================================================

    /**
     * 判断字符串是否是json
     */
    public static boolean isJson(String json) {
        try {
            JSON.parse(json);
            return true;
        } catch (Exception e) {
            LogUtils.e("fastjson check json error, json: {}", json, e);
            return false;
        }
    }

    /**
     * 判断字符串是否是json
     */
    public static boolean isJSONArray(String json) {
        try {
            JSON.parseArray(json);
            return true;
        } catch (Exception e) {
            LogUtils.e("fastjson check json error, json: {}", json, e);
            return false;
        }
    }

    /**
     * 判断字符串是否是json
     */
    public static boolean isJSONObject(String json) {
        try {
            JSON.parseObject(json);
            return true;
        } catch (Exception e) {
            LogUtils.e("fastjson check json error, json: {}", json, e);
            return false;
        }
    }

    //============================================================
    // TODO 序列化、反序列化
    //============================================================

    /**
     * 序列化为JSON
     */
    public static <V> String to(List<V> list) {
        return JSON.toJSONString(list);
    }

    /**
     * 序列化为JSON
     */
    public static <V> String to(V v) {
        return JSON.toJSONString(v);
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(Object jsonObj, Class<V> c) {
        return JSON.parseObject(jsonObj.toString(), c);
    }

    /**
     * JSON反序列化
     */
    public static JSONArray fromJSONArray(String json) {
        if (isJSONArray(json)) {
            return JSON.parseObject(json, JSONArray.class);
        }
        return null;
    }

    /**
     * JSON反序列化
     */
    public static JSONObject fromJSONObject(String json) {
        if (isJSONObject(json)) {
            return JSON.parseObject(json, JSONObject.class);
        }
        return null;
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(String json, Class<V> c) {
        return JSON.parseObject(json, c);
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(String json, TypeReference<V> typeReference) {
        return JSON.parseObject(json, typeReference.getType());
    }

    //============================================================
    // TODO 从JSONObject中获取某个字段
    //============================================================

    public static String getString(@NonNull String json, @NonNull String key) {
        if (FastJsonUtil.isJSONObject(json)) {
            return getString(FastJsonUtil.from(json, JSONObject.class), key);
        }
        return null;
    }

    public static String getString(@NonNull JSONObject jsonObject, @NonNull String key) {
        try {
            return StringUtils.null2Length0(jsonObject.getString(key));
        } catch (Exception e) {
            LogUtils.e("fastjson get string error, json: {}, key: {}", jsonObject, key, e);
            return null;
        }
    }

    public static Integer getInt(@NonNull String json, @NonNull String key) {
        if (FastJsonUtil.isJSONObject(json)) {
            return getInt(FastJsonUtil.from(json, JSONObject.class), key);
        }
        return null;
    }

    public static Integer getInt(@NonNull JSONObject jsonObject, @NonNull String key) {
        try {
            return jsonObject.getInteger(key);
        } catch (Exception e) {
            LogUtils.e("fastjson get string error, json: {}, key: {}", jsonObject, key, e);
            return null;
        }
    }

    public static Long getLong(@NonNull String json, @NonNull String key) {
        if (FastJsonUtil.isJSONObject(json)) {
            return getLong(FastJsonUtil.from(json, JSONObject.class), key);
        }
        return null;
    }

    public static Long getLong(@NonNull JSONObject jsonObject, @NonNull String key) {
        try {
            return jsonObject.getLong(key);
        } catch (Exception e) {
            LogUtils.e("fastjson get string error, json: {}, key: {}", jsonObject, key, e);
            return null;
        }
    }

    public static Double getDouble(@NonNull String json, @NonNull String key) {
        if (FastJsonUtil.isJSONObject(json)) {
            return getDouble(FastJsonUtil.from(json, JSONObject.class), key);
        }
        return null;
    }

    public static Double getDouble(@NonNull JSONObject jsonObject, @NonNull String key) {
        try {
            return jsonObject.getDouble(key);
        } catch (Exception e) {
            LogUtils.e("fastjson get string error, json: {}, key: {}", jsonObject, key, e);
            return null;
        }
    }

    public static BigInteger getBigInteger(@NonNull String json, @NonNull String key) {
        if (FastJsonUtil.isJSONObject(json)) {
            return getBigInteger(FastJsonUtil.from(json, JSONObject.class), key);
        }
        return null;
    }

    public static BigInteger getBigInteger(@NonNull JSONObject jsonObject, @NonNull String key) {
        try {
            return jsonObject.getBigInteger(key);
        } catch (Exception e) {
            LogUtils.e("fastjson get string error, json: {}, key: {}", jsonObject, key, e);
            return null;
        }
    }

    public static BigDecimal getBigDecimal(@NonNull String json, @NonNull String key) {
        if (FastJsonUtil.isJSONObject(json)) {
            return getBigDecimal(FastJsonUtil.from(json, JSONObject.class), key);
        }
        return null;
    }

    public static BigDecimal getBigDecimal(@NonNull JSONObject jsonObject, @NonNull String key) {
        try {
            return jsonObject.getBigDecimal(key);
        } catch (Exception e) {
            LogUtils.e("fastjson get string error, json: {}, key: {}", jsonObject, key, e);
            return null;
        }
    }

    public static Boolean getBoolean(@NonNull String json, @NonNull String key) {
        if (FastJsonUtil.isJSONObject(json)) {
            return getBoolean(FastJsonUtil.from(json, JSONObject.class), key);
        }
        return null;
    }

    public static Boolean getBoolean(@NonNull JSONObject jsonObject, @NonNull String key) {
        try {
            return jsonObject.getBoolean(key);
        } catch (Exception e) {
            LogUtils.e("fastjson get string error, json: {}, key: {}", jsonObject, key, e);
            return null;
        }
    }

    public static Byte getByte(@NonNull String json, @NonNull String key) {
        if (FastJsonUtil.isJSONObject(json)) {
            return getByte(FastJsonUtil.from(json, JSONObject.class), key);
        }
        return null;
    }

    public static Byte getByte(@NonNull JSONObject jsonObject, @NonNull String key) {
        try {
            return jsonObject.getByte(key);
        } catch (Exception e) {
            LogUtils.e("fastjson get string error, json: {}, key: {}", jsonObject, key, e);
            return null;
        }
    }

    public static <T> List<T> getList(@NonNull String json, @NonNull String key, @NonNull Class<T> clazz) {
        if (FastJsonUtil.isJSONObject(json)) {
            return getList(FastJsonUtil.from(json, JSONObject.class), key, clazz);
        }
        return null;
    }

    public static <T> List<T> getList(@NonNull JSONObject jsonObject, @NonNull String key, @NonNull Class<T> c) {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            return jsonArray.toJavaList(c);
        } catch (Exception e) {
            LogUtils.e("fastjson get list error, json: {}, key: {}", jsonObject, key, e);
        }
        return null;
    }

    //============================================================
    // TODO 从JSONObject中修改属性
    //============================================================

    /**
     * 向json中添加属性
     */
    public static <T> String add(String json, String key, T value) {
        if (FastJsonUtil.isJSONObject(json)) {
            JSONObject jsonObject = FastJsonUtil.from(json, JSONObject.class);
            return add(jsonObject, key, value).toString();
        }
        return null;
    }

    /**
     * 向json中添加属性
     */
    public static <T> JSONObject add(JSONObject jsonObject, String key, T value) {
        if (value instanceof String || value instanceof Number || value instanceof Boolean || value instanceof Byte[]) {
            jsonObject.put(key, value);
        } else {
            jsonObject.put(key, to(value));
        }
        return jsonObject;
    }

    /**
     * 除去json中的某个属性
     */
    public static String remove(String json, String key) {
        if (FastJsonUtil.isJSONObject(json)) {
            JSONObject jsonObject = FastJsonUtil.from(json, JSONObject.class);
            return remove(jsonObject, key).toJSONString();
        }
        return null;
    }

    /**
     * 除去json中的某个属性
     */
    public static JSONObject remove(JSONObject jsonObject, String key) {
        jsonObject.remove(key);
        return jsonObject;
    }

    /**
     * 修改json中的属性
     */
    public static <T> String update(String json, String key, T value) {
        if (FastJsonUtil.isJSONObject(json)) {
            JSONObject jsonObject = FastJsonUtil.from(json, JSONObject.class);
            return update(jsonObject, key, value).toString();
        }
        return null;
    }

    /**
     * 修改json中的属性
     */
    public static <T> JSONObject update(JSONObject jsonObject, String key, T value) {
        add(jsonObject, key, value);
        return jsonObject;
    }
}
