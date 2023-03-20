package com.houvven.twig.ui.route

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.houvven.twig.ui.route.screen.About
import com.houvven.twig.ui.route.screen.HelpScreen
import com.houvven.twig.ui.route.screen.NotConfiguredScreen
import com.houvven.twig.ui.route.screen.OpenSourceReference
import com.houvven.twig.ui.route.screen.TencentMapPointSelection
import com.houvven.twig.ui.route.screen.RewardScreen
import com.houvven.twig.ui.route.screen.configuration.EditConfiguration
import com.houvven.twig.ui.utils.antiShakeNavigatePopBackStack


@Suppress("unused")
enum class Routes {
    LAUNCHER_ROUTES,
    CONFIGURATION_EDIT,
    NOT_CONFIGURED,
    MAP_POINT_SELECTION,
    HELP,
    ABOUT,
    REWARD,
    LICENSE,
}


object LocalNavController {
    private class Builder(val navHost: NavHostController)

    private lateinit var instance: Builder

    val current: NavHostController get() = instance.navHost

    @Throws(IllegalStateException::class, IllegalStateException::class)
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
            expandHorizontally(
                animationSpec = tween(durationMillis = 200, easing = LinearEasing),
                clip = false
            )
        },
        exitTransition = {
            shrinkHorizontally(
                animationSpec = tween(durationMillis = 200, easing = FastOutLinearInEasing),
                clip = false
            )
        },
        popEnterTransition = {
            expandHorizontally(
                animationSpec = tween(durationMillis = 200, easing = LinearEasing),
                clip = false
            )
        },
        popExitTransition = {
            shrinkHorizontally(
                animationSpec = tween(durationMillis = 200, easing = FastOutLinearInEasing),
                clip = false
            )
        }
    ) {
        composable(route = Routes.LAUNCHER_ROUTES.name) { LauncherScreen() }

        composable(
            route = Routes.CONFIGURATION_EDIT.name + "/{title}/{name}/{packageName}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType },
                navArgument("packageName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title")
                ?: throw IllegalStateException("title not found")
            val name = backStackEntry.arguments?.getString("name")
                ?: throw IllegalStateException("name not found")
            val packageName = backStackEntry.arguments?.getString("packageName")
                ?: throw IllegalStateException("packageName not found")
            EditConfiguration(title, name, packageName)
        }

        composable(route = Routes.NOT_CONFIGURED.name) { NotConfiguredScreen() }

        composable(route = Routes.MAP_POINT_SELECTION.name) { TencentMapPointSelection() }

        composable(route = Routes.HELP.name) { HelpScreen() }

        composable(route = Routes.ABOUT.name) { About() }

        composable(route = Routes.REWARD.name) { RewardScreen() }

        composable(route = Routes.LICENSE.name) { OpenSourceReference() }
    }
}


@Composable
fun NavBackButton() {
    IconButton(onClick = { antiShakeNavigatePopBackStack() }) {
        Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
    }
}

// fun buildConfigurationRoute(title: String, app: AppInfoWithCompose): String {
//     return buildString {
//         append(Routes.CONFIGURATION_EDIT.name)
//         append("/")
//         append(title)
//         append("/")
//         append(app.info.label)
//         append("/")
//         append(app.info.packageName)
//     }
// }