package com.handy.basic.utils

import com.blankj.utilcode.util.LogUtils
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * @title: AesUtils
 * @projectName HandyBasicKT
 * @description: AES加密解密工具类
 * @author LiuJie https://github.com/Handy045
 * @date Created in 2019-11-05 11:40
 */
class AesUtils private constructor() {
    companion object {
        private val IV = byteArrayOf(
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8,
            9,
            'A'.toByte(),
            'B'.toByte(),
            'C'.toByte(),
            'D'.toByte(),
            'E'.toByte(),
            'F'.toByte(),
            0
        )
        private val DEFAULT_KEY = "HANDY_SECRET_KEY"

        /**
         * AES 加密
         *
         * @param src 明文
         * @return 密文
         */
        fun encrypt(src: String): String {
            try {
                return encrypt(DEFAULT_KEY, src)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }

        fun encrypt(key: String, src: String): String {
            try {
                if (key.isEmpty()) {
                    LogUtils.e("密钥为空")
                } else if (key.length != 16) {
                    LogUtils.e("密钥长度必须为16位")
                } else {
                    val keyByte = key.toByteArray()
                    val keySpec = SecretKeySpec(keyByte, "AES")
                    //“算法/模式/补码方式”。
                    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                    //使用CBC模式，需要一个向量iv，可增加加密算法的强度。
                    val ivSpec = IvParameterSpec(IV)
                    cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
                    //加密。
                    val srcByte = src.toByteArray()
                    val encrypted = cipher.doFinal(srcByte)
                    //Base64转码。
                    return Base64Utils.encode(encrypted)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }

        /**
         * AES 解密
         *
         * @param src 密文
         * @return 明文
         */
        fun decrypt(src: String): String {
            try {
                return decrypt(DEFAULT_KEY, src)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }

        fun decrypt(key: String, src: String): String {
            try {
                if (key.isEmpty()) {
                    LogUtils.e("密钥为空")
                } else if (key.length != 16) {
                    LogUtils.e("密钥长度必须为16位")
                } else {
                    val keyByte = key.toByteArray()
                    val keySpec = SecretKeySpec(keyByte, "AES")
                    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                    val ivSpec = IvParameterSpec(IV)
                    cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
                    //Base64转码。
                    val srcByte = Base64Utils.decode(src)
                    //解密。
                    val decrypted = cipher.doFinal(srcByte)
                    return String(decrypted)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }
    }
}