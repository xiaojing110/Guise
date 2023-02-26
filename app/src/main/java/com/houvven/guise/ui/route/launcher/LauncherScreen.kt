package com.houvven.guise.ui.route.launcher

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LauncherScreen() {

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(selected = true, onClick = { }, icon = {  })
                NavigationBarItem(selected = true, onClick = { }, icon = {  })
                NavigationBarItem(selected = true, onClick = { }, icon = {  })
                NavigationBarItem(selected = true, onClick = { }, icon = {  })
            }
        }
    ) {

    }

}