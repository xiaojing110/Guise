package com.houvven.twig.ui.route.screen

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.houvven.twig.R
import com.houvven.twig.ui.components.OneBtnAlterDialog
import com.houvven.twig.ui.components.layout.ScrollColumn
import com.houvven.twig.ui.route.NavBackButton
import com.houvven.twig.ui.utils.mailto

data class Reward(val donor: String, val money: Double)


@Suppress("unused", "SpellCheckingInspection")
internal val rewards = arrayOf(
    Reward(donor = "小港", money = 1.68),
    Reward(donor = "红烧茄子", money = 2.00),
    Reward(donor = "*G", money = 5.00),
    Reward(donor = "*!", money = 0.10),
    Reward(donor = "*归", money = 18.88),
    Reward(donor = "]*[", money = 1.00),
    Reward(donor = "*阻", money = 1.00),
    Reward(donor = "*龍", money = 3.00),
    Reward(donor = "啦啦啦", money = 5.00),
    Reward(donor = "littlehappy", money = 1.00),
    Reward(donor = "*🦖", money = 1.00),
    Reward(donor = "*篷", money = 19.00),
    Reward(donor = "**锋", money = 100.00),
    Reward(donor = "新年快乐", money = 0.04),
    Reward(donor = "**环", money = 3.00),
    Reward(donor = "分赃, 哈哈", money = 10.98)
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardScreen() {
    var showThanksDialog by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.setting_reward)) },
                navigationIcon = { NavBackButton() })
        }
    ) {
        ScrollColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val drawable =
                AppCompatResources.getDrawable(LocalContext.current, R.drawable.reward_wechat)
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = rememberDrawablePainter(drawable = drawable),
                contentDescription = null,
                alignment = Alignment.Center,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(175.dp)
                    .clip(RoundedCornerShape(50))
                    .border(2.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(50))
                    .shadow(15.dp, RoundedCornerShape(50))
                    .clickable { }
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(text = "感谢各位的支持！😘")
            Spacer(modifier = Modifier.height(30.dp))
            OutlinedCard(
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            ) {
                Column(
                    modifier = Modifier.padding(30.dp)
                ) {
                    Text(
                        "打赏作者后你可以获得以下特权: ",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("1. 你的名字将会出现在这里", style = MaterialTheme.typography.labelLarge)
                    // Text("2. 云端同步/备份功能", style = MaterialTheme.typography.labelLarge)
                    // Text("3. 享受云端预设配置功能", style = MaterialTheme.typography.labelLarge)
                    // Text("4. 和其他用户一起共享模板", style = MaterialTheme.typography.labelLarge)
                    // Spacer(modifier = Modifier.height(3.dp))
                    // Text(
                    //     "备注: 2, 3, 4需要打赏两元或以上",
                    //     style = MaterialTheme.typography.labelMedium,
                    //     color = MaterialTheme.colorScheme.onSurface.copy(alpha = .9F)
                    // )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "请备注你的邮箱地址，以便我们联系你。",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = "请备注你想要展示的昵称，以便我们展示。",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            Spacer(modifier = Modifier.height(35.dp))
            Row {
                Button(onClick = { showThanksDialog = true }) {
                    Text(text = "感谢名单")
                }
                Spacer(modifier = Modifier.width(15.dp))
                Button(onClick = { uriHandler mailto "2960267005@qq.com" }) {
                    Text(text = "联系作者")
                }
            }
        }
    }

    if (showThanksDialog) OneBtnAlterDialog(
        shape = MaterialTheme.shapes.medium,
        onDismissRequest = { showThanksDialog = false },
        button = { TextButton(onClick = { showThanksDialog = false }) { Text(text = "关闭") } },
        title = { Text(text = "感谢名单") },
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95F),
        tonalElevation = 0.dp
    ) {
        LazyColumn {
            items(rewards) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .requiredWidth(175.dp)
                        .padding(vertical = 5.dp)
                ) {
                    Text(text = it.donor, style = MaterialTheme.typography.labelLarge)
                    Text(text = it.money.toString(), style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }

}