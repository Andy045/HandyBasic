package com.handy.basic.utils

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

/**
 * @title: ProcessUtils
 * @projectName HandyBasicKT
 * @description: 进程相关工具类
 * @author LiuJie https://github.com/Handy045
 * @date Created in 2019-10-28 16:34
 */
class ProcessUtils private constructor() {
    companion object {
        /**
         * 获取进程号对应的进程名
         *
         * @param pid 进程号
         * @return 进程名
         */
        fun getProcessName(pid: Int): String? {
            var reader: BufferedReader? = null
            try {
                reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
                var processName = reader.readLine()
                if (!processName.isNullOrEmpty()) {
                    processName = processName.trim()
                }
                return processName
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            } finally {
                try {
                    reader?.close()
                } catch (exception: IOException) {
                    exception.printStackTrace()
                }
            }
            return null
        }
    }
}