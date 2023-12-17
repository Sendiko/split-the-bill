package com.sendiko.split_the_bill.ui.screen

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sendiko.split_the_bill.R
import com.sendiko.split_the_bill.helper.formatToRupiah
import com.sendiko.split_the_bill.helper.isNotEmpty
import com.sendiko.split_the_bill.ui.components.BillsItem
import com.sendiko.split_the_bill.ui.components.CustomDialog
import com.sendiko.split_the_bill.ui.components.CustomDropDown
import com.sendiko.split_the_bill.ui.components.CustomOutlinedTextFields
import com.sendiko.split_the_bill.ui.components.PRIVACY_POLICY
import com.sendiko.split_the_bill.ui.components.poweredBy
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarPosition
import com.stevdzasan.messagebar.rememberMessageBarState

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun SplitBillScreen(
    state: SplitBillState, onEvent: (SplitBillEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val messageBarState = rememberMessageBarState()
    ContentWithMessageBar(
        messageBarState = messageBarState,
        position = MessageBarPosition.BOTTOM,
        errorContainerColor = MaterialTheme.colorScheme.error,
        errorContentColor = MaterialTheme.colorScheme.onError,
        errorIcon = Icons.Default.Help,
    ) {
        val listState = rememberLazyListState()
        LaunchedEffect(key1 = state, key2 = listState.isScrollInProgress) {
            when {
                state.isStupid -> {
                    messageBarState.addError(Exception("you just pay full price dummy"))
                    Handler(Looper.myLooper()!!).postDelayed({
                        onEvent(SplitBillEvent.ClearMessageState)
                    }, 1000)
                }

                state.isShowingSuccessMessage -> {
                    messageBarState.addSuccess("Bill successfully splitted!")
                    Handler(Looper.myLooper()!!).postDelayed({
                        onEvent(SplitBillEvent.ClearMessageState)
                    }, 1000)
                }
            }
            when (listState.isScrollInProgress) {
                true -> onEvent(SplitBillEvent.ClearMessageState)
                false -> null
            }
        }
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "Split The Bill!")
                    },
                    scrollBehavior = scrollBehavior,
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        scrolledContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    actions = {
                        IconButton(
                            onClick = {
                                onEvent(SplitBillEvent.SetShowDropDown(!state.isShowingDropDown))
                                Log.i(
                                    "DROPDOWN_STATE",
                                    "SplitBillScreen: ${state.isShowingDropDown}"
                                )
                            }
                        ) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More")
                        }
                        CustomDropDown(
                            expanded = state.isShowingDropDown,
                            items = listOf("Privacy Policy", "About Us"),
                            onDismissRequest = { onEvent(SplitBillEvent.SetShowDropDown(!state.isShowingDropDown)) },
                            onClickAction = {
                                onEvent(SplitBillEvent.SetShowDropDown(!state.isShowingDropDown))
                                when (it) {
                                    0 -> onEvent(SplitBillEvent.SetShowDialog(1))
                                    1 -> onEvent(SplitBillEvent.SetShowDialog(2))
                                }
                            }
                        )
                    }
                )
            }
        ) { paddingValues ->
            when (state.dialog) {
                1 -> CustomDialog(
                    title = "Privacy Policy",
                    description = PRIVACY_POLICY,
                    onConfirmAction = { onEvent(SplitBillEvent.SetShowDialog(0)) },
                    onDismissRequest = {
                        onEvent(SplitBillEvent.SetShowDialog(0))
                    }
                )

                2 -> {
                    CustomDialog(
                        title = "About Us",
                        image = R.drawable.logo_long,
                        description = poweredBy,
                        onConfirmAction = { onEvent(SplitBillEvent.SetShowDialog(0)) },
                        onDismissRequest = {
                            onEvent(SplitBillEvent.SetShowDialog(0))
                        }
                    )
                }
            }
            LazyColumn(modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize(),
                horizontalAlignment = Alignment.End,
                contentPadding = PaddingValues(
                    top = paddingValues.calculateTopPadding(), start = 8.dp, end = 8.dp
                ),
                state = listState,
                content = {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Price to split",
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        CustomOutlinedTextFields(
                            value = state.bill,
                            onNewValue = {
                                if (it.length <= state.maxBillDigits) {
                                    onEvent(SplitBillEvent.SetBill(it))
                                } else {
                                    onEvent(SplitBillEvent.ExceedMaxDigits)
                                }
                            },
                            hint = "000.000",
                            isError = state.errorBillInput,
                            modifier = Modifier.fillMaxSize(),
                            leadingIcon = {
                                Text(
                                    text = "Rp.", style = TextStyle(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            },
                            errorMessage = when (state.errorBillInput) {
                                true -> state.errorMessage
                                else -> ""
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Amount of person",
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        CustomOutlinedTextFields(
                            value = state.person,
                            onNewValue = {
                                onEvent(SplitBillEvent.SetPerson(it))
                            },
                            hint = "0",
                            isError = state.errorPersonInput,
                            modifier = Modifier.fillMaxSize(),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "person"
                                )
                            },
                            errorMessage = if (state.errorPersonInput) {
                                state.errorMessage
                            } else ""
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            AnimatedVisibility(
                                visible = state.finalBill != "",
                                enter = slideInHorizontally(),
                                exit = slideOutHorizontally()
                            ) {
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    ), modifier = Modifier
                                        .padding(end = 4.dp)
                                        .fillMaxHeight()
                                ) {
                                    Text(
                                        text = "Splitted bill: ${state.finalBill.formatToRupiah()}",
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                            AnimatedContent(
                                targetState = state.finalBill != "",
                                modifier = Modifier.weight(1f)
                            ) { finalBillEmpty ->
                                when (finalBillEmpty) {
                                    true -> Button(
                                        onClick = {
                                            onEvent(SplitBillEvent.ClearState)
                                        },
                                        content = {
                                            Text("Clear")
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(start = 8.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.error,
                                            contentColor = MaterialTheme.colorScheme.onError
                                        )
                                    )

                                    false -> Button(
                                        onClick = {
                                            onEvent(SplitBillEvent.SaveSplitBill)
                                        },
                                        content = {
                                            Text("Split!")
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                    )
                                }

                            }
                        }
                    }
                    item {
                        AnimatedVisibility(visible = isNotEmpty(state.bills)) {
                            Text(
                                text = "History",
                                style = TextStyle(
                                    fontSize = 20.sp, fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp, horizontal = 8.dp)
                            )
                        }
                    }
                    items(state.bills) {
                        AnimatedVisibility(visible = isNotEmpty(state.bills)) {
                            BillsItem(
                                bills = it,
                                onClick = {
                                    onEvent(
                                        SplitBillEvent.DeleteSplitBill(
                                            it
                                        )
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateItemPlacement()
                            )
                        }
                    }
                    item {
                        AnimatedVisibility(visible = isNotEmpty(state.bills)) {
                            Card(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 16.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "You reach the end of the list",
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}
