package com.sendiko.split_the_bill

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.hilt.navigation.compose.hiltViewModel
import com.sendiko.split_the_bill.ui.screen.SplitBillScreen
import com.sendiko.split_the_bill.ui.screen.SplitBillViewModel
import com.sendiko.split_the_bill.ui.theme.SplitthebillTheme
import dagger.hilt.android.AndroidEntryPoint

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = hiltViewModel<SplitBillViewModel>()
            SplitthebillTheme(
                darkTheme = viewModel.state.collectAsState().value.isDarkTheme
            ) {
                val state by viewModel.state.collectAsState()
                SplitBillScreen(state = state, onEvent = viewModel::onEvent)
            }
        }
    }
}