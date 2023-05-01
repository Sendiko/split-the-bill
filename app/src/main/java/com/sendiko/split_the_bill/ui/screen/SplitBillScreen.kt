package com.sendiko.split_the_bill.ui.screen

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sendiko.split_the_bill.ui.components.BillsItem
import com.sendiko.split_the_bill.ui.components.CustomOutlinedTextFields
import com.sendiko.split_the_bill.ui.events.SplitBillEvent
import com.sendiko.split_the_bill.ui.state.SplitBillState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplitBillScreen(
    state: SplitBillState,
    onEvent: (SplitBillEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    fun isNotEmpty(list: List<*>?): Boolean {
        return list != null && list.isNotEmpty()
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Split The Bill!")
                },
                scrollBehavior = scrollBehavior
            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.End,
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
                                if(it.length <= state.maxBillDigits){
                                    onEvent(SplitBillEvent.SetBill(it))
                                } else {
                                    onEvent(SplitBillEvent.ExceedMaxDigits)
                                }
                            },
                            hint = " 000",
                            isError = state.errorBillInput,
                            modifier = Modifier.fillMaxSize(),
                            leadingIcon = {
                                Text(
                                    text = "Rp.",
                                    style = TextStyle(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            },
                            errorMessage = when(state.errorBillInput){
                                true -> state.errorMessage
                                else -> "please do not use '.' or ',' i'm still working on currency formatting :D"
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
                            errorMessage = if(state.errorPersonInput){
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
                            if (state.finalBill != "") {
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    ), modifier = Modifier
                                        .weight(
                                            1f
                                        )
                                        .padding(end = 4.dp)
                                        .fillMaxHeight()
                                ) {
                                    Text(
                                        text = "Splitted bill: Rp.${state.finalBill}",
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                            if (state.finalBill != "") {
                                Button(
                                    onClick = {
                                        onEvent(SplitBillEvent.ClearState)
                                    },
                                    content = {
                                        Text("Clear")
                                    },
                                    modifier = Modifier.weight(1f)
                                        .padding(start = 8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.error,
                                        contentColor = MaterialTheme.colorScheme.onError
                                    )
                                )
                            } else {
                                Button(
                                    onClick = {
                                        onEvent(SplitBillEvent.SaveSplitBill)
                                    },
                                    content = {
                                        Text("Split!")
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                    item {
                        if (isNotEmpty(state.bills)) {
                            Text(
                                text = "History", style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp, horizontal = 8.dp)
                            )
                        }
                    }
                    items(state.bills) {
                        BillsItem(
                            bills = it,
                            onClick = {
                                onEvent(
                                    SplitBillEvent.DeleteSplitBill(
                                        it
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                contentPadding = PaddingValues(
                    top = paddingValues.calculateTopPadding(),
                    start = 8.dp,
                    end = 8.dp
                ),
            )
        }
    )
}
