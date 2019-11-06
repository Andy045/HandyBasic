package com.handy.basic.utils

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.alibaba.fastjson.serializer.SerializerFeature
import com.blankj.utilcode.util.LogUtils
import java.math.BigDecimal
import java.math.BigInteger

/**
 * @title: FastJsonUtil
 * @projectName HandyBasicKT
 * @description: FastJson工具类
 * @author LiuJie https://github.com/Handy045
 * @date Created in 2019-11-04 16:51
 */
class FastJsonUtil private constructor() {
    companion object {
        //============================================================
        // TODO 格式化JSONObject
        //============================================================

        /**
         * 格式化Json(美化)
         *
         * @return json
         */
        fun format(json: String): String {
            return JSON.toJSONString(JSONObject.parseObject(json), SerializerFeature.PrettyFormat)
        }

        //============================================================
        // TODO 判断JSON类型
        //============================================================

        /**
         * 判断字符串是否是json
         */
        fun isJson(json: String): Boolean {
            try {
                JSON.parse(json)
                return true
            } catch (e: Exception) {
                LogUtils.e("fastjson check json error, json: {}", json, e)
                return false
            }

        }

        /**
         * 判断字符串是否是json
         */
        fun isJSONArray(json: String): Boolean {
            try {
                JSON.parseArray(json)
                return true
            } catch (e: Exception) {
                LogUtils.e("fastjson check json error, json: {}", json, e)
                return false
            }

        }

        /**
         * 判断字符串是否是json
         */
        fun isJSONObject(json: String): Boolean {
            try {
                JSON.parseObject(json)
                return true
            } catch (e: Exception) {
                LogUtils.e("fastjson check json error, json: {}", json, e)
                return false
            }

        }

        //============================================================
        // TODO 序列化、反序列化
        //============================================================

        /**
         * 序列化为JSON
         */
        fun <V> to(v: V): String {
            return JSON.toJSONString(v)
        }

        /**
         * JSON反序列化
         */
        fun <V> from(json: String, c: Class<V>): V {
            return JSON.parseObject(json, c)
        }

        /**
         * JSON反序列化
         */
        fun <V> from(json: String, typeReference: TypeReference<V>): V? {
            return JSON.parseObject<V>(json, typeReference.type)
        }

        /**
         * JSON反序列化
         */
        fun fromJSONArray(json: String): JSONArray? {
            return if (isJSONArray(json)) {
                JSON.parseObject(json, JSONArray::class.java)
            } else null
        }

        /**
         * JSON反序列化
         */
        fun fromJSONObject(json: String): JSONObject? {
            return if (isJSONObject(json)) {
                JSON.parseObject(json, JSONObject::class.java)
            } else null
        }

        //============================================================
        // TODO 从JSONObject中获取某个字段
        //============================================================

        fun getString(json: String, key: String): String? {
            return if (isJSONObject(json)) {
                getString(from(json, JSONObject::class.java), key)
            } else null
        }

        fun getString(jsonObject: JSONObject, key: String): String? {
            try {
                return jsonObject.getString(key)
            } catch (e: Exception) {
                LogUtils.e("fastjson get string error, json: {}, key: {}", jsonObject, key, e)
                return null
            }

        }

        fun getInt(json: String, key: String): Int? {
            return if (isJSONObject(json)) {
                getInt(from(json, JSONObject::class.java), key)
            } else null
        }

        fun getInt(jsonObject: JSONObject, key: String): Int? {
            try {
                return jsonObject.getInteger(key)
            } catch (e: Exception) {
                LogUtils.e("fastjson get string error, json: {}, key: {}", jsonObject, key, e)
                return null
            }

        }

        fun getLong(json: String, key: String): Long? {
            return if (isJSONObject(json)) {
                getLong(from(json, JSONObject::class.java), key)
            } else null
        }

        fun getLong(jsonObject: JSONObject, key: String): Long? {
            try {
                return jsonObject.getLong(key)
            } catch (e: Exception) {
                LogUtils.e("fastjson get string error, json: {}, key: {}", jsonObject, key, e)
                return null
            }

        }

        fun getDouble(json: String, key: String): Double? {
            return if (isJSONObject(json)) {
                getDouble(from(json, JSONObject::class.java), key)
            } else null
        }

        fun getDouble(jsonObject: JSONObject, key: String): Double? {
            try {
                return jsonObject.getDouble(key)
            } catch (e: Exception) {
                LogUtils.e("fastjson get string error, json: {}, key: {}", jsonObject, key, e)
                return null
            }

        }

        fun getBigInteger(json: String, key: String): BigInteger? {
            return if (isJSONObject(json)) {
                getBigInteger(from(json, JSONObject::class.java), key)
            } else null
        }

        fun getBigInteger(jsonObject: JSONObject, key: String): BigInteger? {
            try {
                return jsonObject.getBigInteger(key)
            } catch (e: Exception) {
                LogUtils.e("fastjson get string error, json: {}, key: {}", jsonObject, key, e)
                return null
            }

        }

        fun getBigDecimal(json: String, key: String): BigDecimal? {
            return if (isJSONObject(json)) {
                getBigDecimal(from(json, JSONObject::class.java), key)
            } else null
        }

        fun getBigDecimal(jsonObject: JSONObject, key: String): BigDecimal? {
            try {
                return jsonObject.getBigDecimal(key)
            } catch (e: Exception) {
                LogUtils.e("fastjson get string error, json: {}, key: {}", jsonObject, key, e)
                return null
            }

        }

        fun getBoolean(json: String, key: String): Boolean? {
            return if (isJSONObject(json)) {
                getBoolean(from(json, JSONObject::class.java), key)
            } else null
        }

        fun getBoolean(jsonObject: JSONObject, key: String): Boolean? {
            try {
                return jsonObject.getBoolean(key)
            } catch (e: Exception) {
                LogUtils.e("fastjson get string error, json: {}, key: {}", jsonObject, key, e)
                return null
            }

        }

        fun getByte(json: String, key: String): Byte? {
            return if (isJSONObject(json)) {
                getByte(from(json, JSONObject::class.java), key)
            } else null
        }

        fun getByte(jsonObject: JSONObject, key: String): Byte? {
            try {
                return jsonObject.getByte(key)
            } catch (e: Exception) {
                LogUtils.e("fastjson get string error, json: {}, key: {}", jsonObject, key, e)
                return null
            }

        }

        fun <T> getList(json: String, key: String, clazz: Class<T>): List<T>? {
            return if (isJSONObject(json)) {
                getList(from(json, JSONObject::class.java), key, clazz)
            } else null
        }

        fun <T> getList(jsonObject: JSONObject, key: String, c: Class<T>): List<T>? {
            try {
                val jsonArray = jsonObject.getJSONArray(key)
                return jsonArray.toJavaList(c)
            } catch (e: Exception) {
                LogUtils.e("fastjson get list error, json: {}, key: {}", jsonObject, key, e)
            }

            return null
        }

        //============================================================
        // TODO 从JSONObject中修改属性
        //============================================================

        /**
         * 向json中添加属性
         */
        fun <T> add(json: String, key: String, value: T): String? {
            if (isJSONObject(json)) {
                val jsonObject = from(json, JSONObject::class.java)
                return add(jsonObject, key, value).toString()
            }
            return null
        }

        /**
         * 向json中添加属性
         */
        fun <T> add(jsonObject: JSONObject?, key: String, value: T): JSONObject {
            if (value is String || value is Number || value is Boolean || value is Array<*>) {
                jsonObject!![key] = value
            } else {
                jsonObject!![key] = to(value)
            }
            return jsonObject
        }

        /**
         * 除去json中的某个属性
         */
        fun remove(json: String, key: String): String? {
            if (isJSONObject(json)) {
                val jsonObject = from(json, JSONObject::class.java)
                return remove(jsonObject, key).toJSONString()
            }
            return null
        }

        /**
         * 除去json中的某个属性
         */
        fun remove(jsonObject: JSONObject, key: String): JSONObject {
            jsonObject.remove(key)
            return jsonObject
        }

        /**
         * 修改json中的属性
         */
        fun <T> update(json: String, key: String, value: T): String? {
            if (isJSONObject(json)) {
                val jsonObject = from(json, JSONObject::class.java)
                return update(jsonObject, key, value)!!.toString()
            }
            return null
        }

        /**
         * 修改json中的属性
         */
        fun <T> update(jsonObject: JSONObject?, key: String, value: T): JSONObject? {
            add(jsonObject, key, value)
            return jsonObject
        }
    }
}