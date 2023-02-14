package com.houvven.lib.command


@Suppress("unused", "MemberVisibilityCanBePrivate")
class SQLite3Shell private constructor(private val sqlite3: String) {

    private val shell = ShellActuators

    @Throws(RuntimeException::class)
    private fun exec(db: String, sql: String) = arrayOf("$sqlite3 -json $db", sql).let {
        val result = shell.exec(it, true)
        if (result.isSuccess.not()) {
            // throw RuntimeException(result.exceptionOrNull())
        }
        result.getOrNull()
    }


    fun query(db: String, sql: String, vararg args: Any?) = exec(db, formatSQL(sql, *args))


    fun update(db: String, sql: String, vararg args: Any?) = exec(db, formatSQL(sql, *args))


    fun insert(db: String, sql: String, vararg args: Any?) = exec(db, formatSQL(sql, *args))


    fun delete(db: String, sql: String, vararg args: Any?) = exec(db, formatSQL(sql, *args))


    private fun formatSQL(sql: String, vararg args: Any?): String {
        var result = sql
        if (result.endsWith(";").not()) {
            result += ";"
        }
        args.forEach { any ->
            result = result.replaceFirst("?","'${any.toString()}'")
        }
        return result
    }


    companion object {
        @JvmStatic
        fun of(sqlite3: String) = SQLite3Shell(sqlite3)
    }

}