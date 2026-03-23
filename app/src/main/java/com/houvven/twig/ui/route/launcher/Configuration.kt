package com.houvven.twig.ui.route.launcher

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.houvven.twig.R
import com.houvven.twig.TwigStore
import com.houvven.twig.config.TwigConfig
import com.houvven.twig.ui.LocalAppState
import com.houvven.twig.ui.components.ActivateInfoCard
import com.houvven.twig.ui.components.LottieEmptyAnimate
import com.houvven.twig.ui.components.application.AppInfoCardContent
import com.houvven.twig.ui.components.application.AppInfoWithCompose
import com.houvven.twig.ui.components.application.AppListPlaceholder
import com.houvven.twig.ui.components.application.AppListView
import com.houvven.twig.ui.route.Routes
import com.houvven.twig.ui.route.deploy.DeployScreen
import com.houvven.twig.ui.route.deploy.DSD
import com.houvven.twig.ui.utils.antiShakeNavOption
import androidx.compose.ui.res.stringResource

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguredLauncher() {
    val isGlobalPattern = LocalAppState.isGlobalPattern.value

    @Composable
    fun InactivatedScreen() {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Shield,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .size(26.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = stringResource(R.string.title_config_inactivated),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        ) {
            Column(modifier = Modifier.padding(top = it.calculateTopPadding())) {
                Spacer(modifier = Modifier.height(20.dp))
                ActivateInfoCard()
            }
        }
    }


    @Composable
    fun ConfiguredScreen() {

        if (LocalAppState.Apps.appsIsLoading.value)
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = stringResource(id = R.string.title_config_configured),
                                fontWeight = FontWeight.Bold
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }
            ) { paddingValues ->
                AppListPlaceholder(modifier = Modifier.padding(top = paddingValues.calculateTopPadding()))
            }
        else Scaffold(
            topBar = {
                val title =
                    if (LocalAppState.noRootWork.value) stringResource(id = R.string.title_config_no_root)
                    else stringResource(id = R.string.title_config_configured)
                TopAppBar(
                    title = {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { antiShakeNavOption(route = Routes.NOT_CONFIGURED.name) },
                    shape = RoundedCornerShape(16.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                }
            },
            floatingActionButtonPosition = FabPosition.End,
        ) { it ->
            val predicate: (AppInfoWithCompose) -> Boolean =
                { TwigStore.APP.all.value.contains(it.info.packageName) }
            val apps = LocalAppState.Apps.current.value.filter(predicate)
            if (apps.isEmpty() && LocalAppState.Apps.appsIsLoaded.value) {
                Column(
                    modifier = Modifier
                        .padding(top = it.calculateTopPadding())
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LottieEmptyAnimate()
                }
                return@Scaffold
            }
            AppListView(
                modifier = Modifier.padding(top = it.calculateTopPadding()),
                apps = apps
            ) {
                val title = stringResource(
                    id = R.string.title_config_edit
                )
                Card(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                        .clickable {
                            antiShakeNavOption(
                                route = "${Routes.CONFIGURATION_EDIT.name}/$title/${it.info.label}/${it.info.packageName}",
                            )
                        },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35F)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppInfoCardContent(app = it)
                    }
                }
            }
        }
    }


    @Composable
    fun GlobalPatternScreen() {
        val config = TwigStore.APP.getGlobalFromSp()
        val manager = remember { TwigConfig.Manager(config) }
        DeployScreen(manager = manager) {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.title_config_global),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                actions = {
                    DSD.ClearButton(manager = this@DeployScreen)
                    DSD.SaveButton(manager = this@DeployScreen)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    }


    if (LocalAppState.isHooked.not()) InactivatedScreen()
    else if (isGlobalPattern) GlobalPatternScreen()
    else ConfiguredScreen()

}
