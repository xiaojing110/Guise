package com.houvven.guise.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.houvven.guise.R


/**
 * 加载动画
 * [origin|preview](https://lottiefiles.com/9844-loading-40-paperplane)
 */
@Composable
fun LottieLoadingAnimate(modifier: Modifier = Modifier, alignment: Alignment = Alignment.Center) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(resId = R.raw.loading_light))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier,
        alignment = alignment
    )
}