package com.sendiko.split_the_bill.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sendiko.split_the_bill.ui.components.CustomOutlinedTextFields
import com.sendiko.split_the_bill.viewmodels.SplitBillViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplitBillScreen(
    viewModel: SplitBillViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
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
                            value = viewModel.bill.collectAsState().value,
                            onNewValue = {
                                viewModel.setBill(it)
                            },
                            hint = " 000",
                            isError = viewModel.emptyBill.collectAsState().value,
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
                            errorMessage = viewModel.errorMessage.collectAsState().value
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
                            value = viewModel.person.collectAsState().value,
                            onNewValue = {
                                viewModel.setPerson(it)
                            },
                            hint = "0",
                            isError = viewModel.emptyPerson.collectAsState().value,
                            modifier = Modifier.fillMaxSize(),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "person"
                                )
                            },
                            errorMessage = viewModel.errorMessage.collectAsState().value
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (viewModel.splittedBill.collectAsState().value != "") {
                                Text(
                                    text = "Splitted bill: Rp.${viewModel.splittedBill.collectAsState().value}",
                                    modifier = Modifier.weight(
                                        1f
                                    )
                                )
                            }
                            if(viewModel.splittedBill.collectAsState().value != ""){
                                Button(
                                    onClick = {
                                        viewModel.clearState()
                                    },
                                    content = {
                                        Text("Clear")
                                    }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.error,
                                        contentColor = MaterialTheme.colorScheme.onError
                                    )
                                )
                            } else {
                                Button(
                                    onClick = {
                                        viewModel.splitTheBill(
                                            bill = viewModel.bill.value,
                                            person = viewModel.person.value
                                        )
                                    },
                                    content = {
                                        Text("Split!")
                                    }, modifier = Modifier.weight(1f)
                                )
                            }
                        }
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
