package com.houvven.twig.preset.device

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.houvven.twig.config.AppConfig
import java.io.Closeable

class DeviceDBHelper(context: Context) : Closeable {

    private val version = 8
    private val deviceDBFileName = "devices.db"
    private val deviceDBFile = context.getDatabasePath(deviceDBFileName)

    init {
        if (AppConfig.run { mmkv<Int> {decodeInt(DEVICE_DB_VERSION, 0)} } < version) {
            AppConfig.run { mmkv { encode(DEVICE_DB_VERSION, version) } }
            deviceDBFile.delete()
            context.assets.open(deviceDBFileName).use { input ->
                deviceDBFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }
    }

    private val db: SQLiteDatabase by lazy {
        SQLiteDatabase.openDatabase(deviceDBFile.absolutePath, null, SQLiteDatabase.OPEN_READONLY)
    }

    fun getAllBrand(): Map<String, String> {
        val cursor = db.rawQuery(
            "select brand, brand_title from (select * from models group by brand)",
            null
        )
        val map = mutableMapOf<String, String>()
        while (cursor.moveToNext()) {
            val brand = cursor.getString(0)
            val brandName = cursor.getString(1)
            map[brand] = brandName
        }
        cursor.close()
        return map
    }

    fun getDevicesByBrand(brand: String): List<Device> {
        val cursor = runCatching {
            db.rawQuery(
                "select * from (select * from models group by model) where brand = ? " +
                        "order by coalesce(year, 0) desc, " +
                        "case when dtype = 'phone' then 0 else 1 end, model_name",
                arrayOf(brand)
            )
        }.getOrElse {
            db.rawQuery(
                "select * from (select * from models group by model) where brand = ?",
                arrayOf(brand)
            )
        }
        val list = mutableListOf<Device>()
        while (cursor.moveToNext()) {
            list.add(cursor.toDevice())
        }
        cursor.close()
        return list
    }

    /**
     * 随机取一台近年机型，用于「一键随机」。
     * `year` 字段由 assets/devices.db 提供；旧库没有该字段时自动降级。
     */
    fun getRandomDevice(minYear: Int = 2026): Device? {
        val cursor = runCatching {
            db.rawQuery(
                "select * from models where year >= ? and dtype = 'phone' " +
                        "and model is not null and model != '' order by random() limit 1",
                arrayOf(minYear.toString())
            )
        }.getOrElse {
            db.rawQuery(
                "select * from models where dtype = 'phone' " +
                        "and model is not null and model != '' order by random() limit 1",
                null
            )
        }
        val device = if (cursor.moveToNext()) cursor.toDevice() else null
        cursor.close()
        return device
    }

    private fun Cursor.toDevice(): Device = Device(
        brand = getString(getColumnIndexOrThrow("brand")),
        brandTitle = getString(getColumnIndexOrThrow("brand_title")),
        code = getString(getColumnIndexOrThrow("code")),
        codeAlias = getString(getColumnIndexOrThrow("code_alias")),
        dtype = getString(getColumnIndexOrThrow("dtype")),
        model = getString(getColumnIndexOrThrow("model")),
        modelName = getString(getColumnIndexOrThrow("model_name")),
        verName = getString(getColumnIndexOrThrow("ver_name"))
    )

    override fun close() {
        db.close()
    }


}