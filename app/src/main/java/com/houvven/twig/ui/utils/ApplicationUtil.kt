package com.houvven.twig.ui.utils

// import com.houvven.androidc.ktx.ifFalse
// import com.houvven.androidc.ktx.ifTrue
// import com.houvven.twig.ui.LocalAppState
// import com.houvven.twig.ui.components.application.AppInfoSortType
// import com.houvven.twig.ui.components.application.AppInfoWithCompose
// import java.text.Collator
// import java.util.Locale
//
//
// fun generateAppList(apps: List<AppInfoWithCompose>): List<AppInfoWithCompose> {
//     var result = apps
//     LocalAppState.displaySystemApps.value ifFalse {
//         result = apps.filter { !it.info.isSystemApp }
//     }
//
//     result = when (LocalAppState.appsSortType.value) {
//         AppInfoSortType.NAME -> result.sortedBy {
//             Collator.getInstance(Locale.CHINA).getCollationKey(it.info.label)
//         }
//
//         AppInfoSortType.INSTALL_TIME -> {
//             result.sortedBy { it.info.firstInstallTime }
//         }
//
//         AppInfoSortType.UPDATE_TIME -> {
//             result.sortedBy { it.info.lastUpdateTime }
//         }
//     }
//
//     LocalAppState.appsReversed.value ifTrue {
//         result = result.reversed()
//     }
//
//     return result
// }