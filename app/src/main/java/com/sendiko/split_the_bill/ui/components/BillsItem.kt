package com.sendiko.split_the_bill.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sendiko.split_the_bill.repository.models.Bills
import java.text.NumberFormat
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BillsItem(
    bills: Bills,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Card(
                modifier =  Modifier.padding(start = 8.dp),
                colors = CardDefaults.cardColors(
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                val localDate = bills.date.split(" ")
                val date = localDate[0]
                val month = localDate[1].substring(0, 3)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = date,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    )
                    Text(
                        text = month,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                        ),
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(text = "Total bill: ${bills.bill.formatToRupiah()}")
                Text(text = "Amount of person: ${bills.person}")
                Text(text = "Splitted bill: ${bills.splittedBill.formatToRupiah()}")
            }
        }
        IconButton(onClick = onClick) {
            Card(
                colors = CardDefaults.cardColors(
                    contentColor = MaterialTheme.colorScheme.onError,
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "delete",
                    modifier = Modifier.padding(8.dp),
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun BillsItemPrev() {
    Surface {
        BillsItem(
            Bills(
                id = 1, bill = "12390123", person = "8", splittedBill = "34862", date = "26 January 2005"
            ), modifier = Modifier.fillMaxWidth(), onClick = {})
    }
}

fun String.formatToRupiah(): String {
    val number = this.toDoubleOrNull()
    val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    return if (number == null) "" else formatRupiah.format(number)
}



