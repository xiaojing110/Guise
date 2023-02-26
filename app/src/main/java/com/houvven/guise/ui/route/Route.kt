package com.houvven.guise.ui.route

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.houvven.guise.ui.route.launcher.LauncherScreen


enum class Routes {
    LAUNCHER_ROUTES,
}


object LocalNavController {
    private class Builder(val navHost: NavHostController)

    private lateinit var instance: Builder

    val current: NavHostController get() = instance.navHost

    @Throws(IllegalStateException::class)
    fun setup(navHost: NavHostController) {
        if (::instance.isInitialized) {
            throw IllegalStateException("LocalNavController already initialized")
        } else {
            instance = Builder(navHost)
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationRoute() {
    val navController = rememberAnimatedNavController()
    LocalNavController.setup(navController)

    AnimatedNavHost(
        navController = navController,
        startDestination = Routes.LAUNCHER_ROUTES.name,
        enterTransition = {
            expandHorizontally(animationSpec = tween(easing = LinearEasing))
        },
        exitTransition = {
            shrinkOut(animationSpec = tween(easing = FastOutLinearInEasing))
        },
        popEnterTransition = {
            expandHorizontally(animationSpec = tween(easing = LinearEasing))
        },
        popExitTransition = {
            shrinkOut(animationSpec = tween(easing = FastOutLinearInEasing))
        }
    ) {
        composable(route = Routes.LAUNCHER_ROUTES.name) {
            LauncherScreen()
        }


    }
}