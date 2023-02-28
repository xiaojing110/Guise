package com.houvven.guise.ui.route.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.houvven.guise.R
import com.houvven.guise.ui.AppState
import com.houvven.guise.ui.components.LottieLoadingAnimate
import com.houvven.guise.ui.components.application.AppInfoListView
import com.houvven.guise.ui.route.Routes
import com.houvven.guise.ui.utils.antiShakeNavOption
import com.houvven.guise.ui.utils.antiShakeNavigatePopBackStack


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguredScreen() {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(state = topAppBarState)

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                topAppBarState.heightOffset += available.y
                return Offset.Zero
            }
        }
    }

    if (AppState.appsIsLoading.value) {
        LottieLoadingAnimate()
        return
    }

    val topBar = @Composable {
        LargeTopAppBar(
            title = { Text(text = stringResource(id = R.string.configuration_configured)) },
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            scrollBehavior = scrollBehavior
        )
    }

    val floatingActionButton = @Composable {
        FloatingActionButton(onClick = {
            val route = Routes.CONFIGURATION_ADD.name
            antiShakeNavOption(key = route, delay = 500) { navigate(route) }
        }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }

    Scaffold(
        topBar = topBar,
        floatingActionButton = floatingActionButton
    ) {
        AppInfoListView(
            apps = AppState.apps.value.filter { true /* todo */ },
            modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .nestedScroll(nestedScrollConnection),
            itemOnclick = {}
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotConfigurationScreen() {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(state = topAppBarState)

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                topAppBarState.heightOffset += available.y
                return Offset.Zero
            }
        }
    }

    if (AppState.appsIsLoading.value) {
        LottieLoadingAnimate()
        return
    }

    val topBar = @Composable {
        MediumTopAppBar(
            title = { Text(text = stringResource(id = R.string.configuration_not_configured)) },
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            navigationIcon = {
                IconButton(onClick = { antiShakeNavigatePopBackStack() }) {
                    Icon(imageVector = Icons.Outlined.ArrowBackIos, contentDescription = null)
                }
            },
            scrollBehavior = scrollBehavior
        )
    }

    Scaffold(topBar = topBar) {
        AppInfoListView(
            apps = AppState.apps.value.filter { true /* todo */ },
            modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .nestedScroll(nestedScrollConnection),
            itemOnclick = {}
        )
    }
}