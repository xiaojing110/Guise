package com.houvven.guise.util.android

import android.graphics.Point
import android.graphics.PointF
import java.util.Random
import java.util.UUID
import kotlin.math.roundToInt


object Randoms {

    /**
     * 生成一个指定长度的随机字符串
     * @param length 长度
     * @return 随机字符串
     */
    fun randomString(length: Int): String {
        val random = Random()
        val sb = StringBuilder()
        (0 until length).forEach { _ ->
            val number = random.nextInt(3)
            var result: Long = 0
            when (number) {
                0 -> result = (Math.random() * 25 + 65).roundToInt().toLong()
                1 -> result = (Math.random() * 25 + 97).roundToInt().toLong()
                2 -> result = (Math.random() * 9 + 48).roundToInt().toLong()
            }
            sb.append(result.toChar())
        }
        return sb.toString()
    }

    /**
     * 生成一个指定长度的随机数字
     * @param length 长度
     * @return 随机数字
     */
    fun randomInt(length: Int): Int {
        val random = Random()
        val sb = StringBuilder()
        (0 until length).forEach { _ ->
            val number = random.nextInt(9)
            sb.append(number)
        }
        return sb.toString().toInt()
    }

    /**
     * 生成一个随机长度的随机字符串 长度在[10, 100]内
     */
    fun randomString(): String {
        return randomString(randomInt(2))
    }


    fun uuid(): String {
        return UUID.randomUUID().toString()
    }

    /**
     * 生成一个不带横线的UUID
     */
    fun uuidNoDash(): String {
        return uuid().replace("-", "")
    }

    /**
     * 随机生成Mac地址
     */
    fun randomMacAddress(): String {
        val mac = StringBuilder()
        (0 until 6).forEach { _ ->
            mac.append(randomString(2))
            mac.append(":")
        }
        return mac.substring(0, mac.length - 1)
    }


    /**
     * 随机生成IMEI
     */
    fun randomIMEI(): String {
        val imei = StringBuilder()
        (0 until 15).forEach { _ ->
            imei.append(randomInt(1))
        }
        return imei.toString()
    }

    fun randomPhoneNum(): String {
        val phoneNum = StringBuilder()
        phoneNum.append("1")
        (0 until 10).forEach { _ ->
            phoneNum.append(randomInt(1))
        }
        return phoneNum.toString()
    }

    fun randomFingerPrint(): String {
        val random = Random()
        val fingerprint = StringBuilder()
        fingerprint.append(randomString(5))
        fingerprint.append("/")
        fingerprint.append(randomString(6))
        fingerprint.append("/")
        fingerprint.append(randomString(4))
        fingerprint.append(":")
        fingerprint.append(randomInt(1))
        fingerprint.append("/")
        fingerprint.append(randomString(4))
        fingerprint.append("/")
        fingerprint.append(randomString(4))
        fingerprint.append(":")
        fingerprint.append("user/release-keys")
        return fingerprint.toString()
    }


    fun randomLatLac(): PointF {
        val random = Random()
        val lat = random.nextInt(120) + random.nextFloat()
        val lac = random.nextInt(30) + random.nextFloat()
        return PointF(lat, lac)
    }

}