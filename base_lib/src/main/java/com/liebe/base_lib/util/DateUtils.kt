package com.dating.app.lib.util

import android.annotation.SuppressLint
import com.liebe.base_lib.util.Flog
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description dateUtil
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
object DateUtils {
    val ymd = "yyyy-MM-dd"
    val ymdhms = "yyyy-MM-dd HH:mm:ss"
    val hms = "HH:mm:ss"
    val hm = "HH:mm"

    fun getTimeMillisBeforeYear(years: Int): Long {
        val c = Calendar.getInstance()
        c.add(Calendar.YEAR, -years)
        return c.time.time
    }

    /**
     * description:获取当前年份
     * @return year
     */
    fun getYear(): Int {
        return Calendar.getInstance().get(Calendar.YEAR)
    }


    @SuppressLint("SimpleDateFormat")
    fun getToDay(format: String? = ymdhms): String {
        return SimpleDateFormat(format).format(Date(System.currentTimeMillis()))
    }

    @SuppressLint("SimpleDateFormat")
    fun getToDayTimestamp(format: String? = ymdhms): Long {
        return SimpleDateFormat(format).parse(getToDay(format)).time
    }

    /**
     * 比较两个时间值大小
     * @param firstTime
     * @param secondTime
     * @param 格式化
     */
    fun compareTime(firstTime: String?, secondTime: String?, format: String? = ymdhms): Boolean {
        try {
            return SimpleDateFormat(format).parse(firstTime).time > SimpleDateFormat(format).parse(
                secondTime
            ).time
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getTimestamp(time: String, formatString: String? = ymdhms): Long {
        return SimpleDateFormat(formatString).parse(time).time
    }

    fun srtToDate(time: String, formatString: String? = ymd): Date {
        return SimpleDateFormat(formatString).parse(time)
    }

    fun getDayFromString(time: String, formatString: String? = ymd): Int {
        var day = 0
        try {
            val calendar = Calendar.getInstance()
            calendar.time = srtToDate(time, formatString)
            day = calendar.get(Calendar.DAY_OF_MONTH)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return day
    }

    fun getMonthFromString(time: String, formatString: String? = ymd): Int {
        var month = 0
        try {
            val calendar = Calendar.getInstance()
            calendar.time = srtToDate(time, formatString)
            month = calendar.get(Calendar.MONTH)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return month
    }

    fun getYearFromString(time: String, formatString: String? = ymd): Int {
        var year = 0
        try {
            val calendar = Calendar.getInstance()
            calendar.time = srtToDate(time, formatString)
            year = calendar.get(Calendar.YEAR)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return year
    }

}