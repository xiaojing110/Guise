package com.houvven.twig.ui.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.houvven.twig.ui.route.LocalNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

@get:JvmName("antiShakeClickKeys")
val antiShakeClick = mutableListOf<Any>()

inline fun antiShakeClick(key: Any, delay: Long = 300, crossinline action: () -> Unit) {
    if (antiShakeClick.contains(key)) return
    antiShakeClick.add(key)
    action()
    thread {
        runBlocking {
            delay(delay)
            antiShakeClick.remove(key)
        }
    }
}

fun antiShakeNavigatePopBackStack(delay: Long = 500) {
    antiShakeNavOption(key = "NAV_BACK_STACK", delay = delay) { popBackStack() }
}

inline fun antiShakeNavOption(
    key: Any,
    delay: Long = 500,
    crossinline action: NavHostController.() -> Unit
) {
    antiShakeClick(key = key, delay = delay) {
        LocalNavController.current.action()
    }
}

// @Composable
// @SuppressLint("ComposableNaming")
fun antiShakeNavOption(
    route: String,
    delay: Long = 500,
) {
    antiShakeNavOption(key = route, delay = delay, action = { navigate(route) })
}