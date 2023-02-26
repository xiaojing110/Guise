package com.houvven.guise.config.xposed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Stable
@Serializable
data class GuiseConfig constructor(
    @Transient var packageName: String = "",
    var brand: String = "",
    var model: String = "",
    var product: String = "",
    var device: String = "",
    var board: String = "",
    var hardware: String = ""
) {
    val isEnable: Boolean get() = this == GuiseConfig(this.packageName)
}

@Stable
@Composable
fun rememberGuiseConfigState(config: GuiseConfig): GuiseConfig {
    return rememberSaveable { config }
}
