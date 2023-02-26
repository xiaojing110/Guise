package com.houvven.ktxposed.log

data class Log(
    val level: String,
    val message: String,
    val source: String,
    val time: Long = System.currentTimeMillis()
) {
    override fun toString(): String {
        return "[$time] $level/$source: $message"
    }
}
