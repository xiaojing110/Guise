package com.houvven.guise.lsposed

import android.annotation.SuppressLint
import android.os.Process
import android.util.Log
import com.houvven.guise.module.SystemProp
import com.houvven.guise.module.ktx.download
import com.houvven.lib.command.SQLite3Shell
import com.houvven.lib.command.ShellActuators
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.runBlocking
import java.io.File

object LsposedHelper {

    private const val DOWNLOAD_HOST = "http://rq20dpvrv.hn-bkt.clouddn.com/android-sqlite-bin"

    private const val DOWNLOAD_ARM64 = "${DOWNLOAD_HOST}/arm64-v8a/sqlite3"

    private const val DOWNLOAD_ARM32 = "${DOWNLOAD_HOST}/armeabi-v7a/sqlite3"

    private const val DOWNLOAD_X86 = "${DOWNLOAD_HOST}/x86/sqlite3"

    private const val DOWNLOAD_X86_64 = "${DOWNLOAD_HOST}/x86_64/sqlite3"


    private const val DB_PATH = "/data/adb/lspd/config/modules_config.db"

    private lateinit var SQLite3Bin: String

    private val sqlite3Shell by lazy { SQLite3Shell.of(SQLite3Bin) }

    fun init(sqlite3BinPath: String) {
        SQLite3Bin = sqlite3BinPath
    }

    fun isOk() = ShellActuators.exec("$SQLite3Bin -version", true).isSuccess

    @SuppressLint("SetWorldReadable")
    fun download() = runCatching {
        if (isOk()) return@runCatching
        val file = File(SQLite3Bin)
        if (file.exists().not()) {
            file.parentFile?.mkdirs()
            file.createNewFile()
            file.setReadable(true, false)
            file.setExecutable(true, false)
        }
        runBlocking {
            HttpClient(OkHttp).download(
                when (SystemProp.abi) {
                    "arm64-v8a" -> DOWNLOAD_ARM64
                    "armeabi-v7a" -> DOWNLOAD_ARM32
                    "x86" -> DOWNLOAD_X86
                    "x86_64" -> DOWNLOAD_X86_64
                    else -> throw RuntimeException("Unknown ABI")
                }, file.outputStream()
            )
        }
    }


    val allModules by lazy {
        sqlite3Shell.query(DB_PATH, "select * from modules")?.let {
            Log.d("Lsposed", it)
        }
    }

    fun enableModule(modulePackageName: String) {
        sqlite3Shell.update(
            DB_PATH, "update modules set enabled = 1 where module_pkg_name = ?", modulePackageName
        )
    }

    fun disableModule(modulePackageName: String) {
        sqlite3Shell.update(
            DB_PATH, "update modules set enabled = 0 where module_pkg_name = ?", modulePackageName
        )
    }

    fun addScope(modulePackageName: String, appPkgName: String) {
        sqlite3Shell.insert(
            DB_PATH,
            "insert into scope (mid, app_pkg_name, user_id) values ((select mid from modules where module_pkg_name = ?), ?, 0)",
            modulePackageName,
            appPkgName
        )
    }

    fun removeScope(modulePackageName: String, appPkgName: String) {
        sqlite3Shell.delete(
            DB_PATH,
            "delete from scope where mid = (select mid from modules where module_pkg_name = ?) and app_pkg_name = ?",
            modulePackageName,
            appPkgName
        ).let { Log.d("Lsposed", it.toString()) }
    }

    fun removeAllScope(modulePackageName: String) {
        sqlite3Shell.delete(
            DB_PATH,
            "delete from scope where mid = (select mid from modules where module_package_name = ?)",
            modulePackageName
        )
    }

    fun removeAllScope() {
        sqlite3Shell.delete(
            DB_PATH, "delete from scope"
        )
    }

}