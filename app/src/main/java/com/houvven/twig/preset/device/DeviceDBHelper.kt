package com.houvven.twig.preset.device

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.houvven.twig.config.AppConfig
import java.io.Closeable

class DeviceDBHelper(context: Context) : Closeable {

    private val version = 2
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
        val cursor = db.rawQuery(
            "select * from (select * from models group by model) where brand = ?",
            arrayOf(brand)
        )
        val list = mutableListOf<Device>()
        while (cursor.moveToNext()) {
            val brand = cursor.getString(cursor.getColumnIndexOrThrow("brand"))
            val brandTitle = cursor.getString(cursor.getColumnIndexOrThrow("brand_title"))
            val code = cursor.getString(cursor.getColumnIndexOrThrow("code"))
            val codeAlias = cursor.getString(cursor.getColumnIndexOrThrow("code_alias"))
            val dtype = cursor.getString(cursor.getColumnIndexOrThrow("dtype"))
            val model = cursor.getString(cursor.getColumnIndexOrThrow("model"))
            val modelName = cursor.getString(cursor.getColumnIndexOrThrow("model_name"))
            val verName = cursor.getString(cursor.getColumnIndexOrThrow("ver_name"))
            list.add(
                Device(
                    brand = brand,
                    brandTitle = brandTitle,
                    code = code,
                    codeAlias = codeAlias,
                    dtype = dtype,
                    model = model,
                    modelName = modelName,
                    verName = verName
                )
            )
        }
        cursor.close()
        return list
    }

    override fun close() {
        db.close()
    }


}