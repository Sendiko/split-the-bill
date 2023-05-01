package com.sendiko.split_the_bill

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import com.sendiko.split_the_bill.repository.viewmodels.SplitBillViewModel
import com.sendiko.split_the_bill.repository.viewmodels.ViewModelFactory
import com.sendiko.split_the_bill.ui.screen.SplitBillScreen
import com.sendiko.split_the_bill.ui.theme.SplitthebillTheme

class MainActivity : ComponentActivity() {

    private fun obtainViewModel(application: Application): SplitBillViewModel{
        val factory = ViewModelFactory.getInstance(application)
        return ViewModelProvider(this, factory)[SplitBillViewModel::class.java]
    }

    private val viewModel: SplitBillViewModel by lazy {
        obtainViewModel(requireNotNull(application))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplitthebillTheme {
                val state by viewModel.state.collectAsState()
                SplitBillScreen(state = state, onEvent = viewModel::onEvent)
            }
        }
    }
}