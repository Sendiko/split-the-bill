package com.sendiko.split_the_bill.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.sendiko.split_the_bill.R

@Composable
fun CustomDialog(
    image: Int? = null,
    title: String,
    description: String,
    onConfirmAction: () -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
        content = {
            Card(
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (image != null) {
                        Image(
                            modifier = Modifier.fillMaxWidth(),
                            painter = painterResource(id = image),
                            contentDescription = title
                        )
                    }
                    Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(text = description, textAlign = TextAlign.Justify)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { onConfirmAction() }) {
                            Text(text = "OK")
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun CustomDropDown(
    expanded: Boolean,
    items: List<String>,
    onDismissRequest: () -> Unit,
    onClickAction: (Int) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismissRequest() },
        content = {
            items.forEachIndexed { index, name ->
                DropdownMenuItem(
                    text = { Text(text = name) },
                    onClick = { onClickAction(index) }
                )
            }
        }
    )
}
@Preview
@Composable
fun DialogPrev() {
    CustomDialog(
        title = "About us",
        image = R.drawable.logo_long,
        description = poweredBy,
        onConfirmAction = { /*TODO*/ }) {

    }
}

const val poweredBy = "This software is made and supported by Sendiko Software Studio." +
        " You can support us by following us on Github and Instagram"

const val PRIVACY_POLICY =
    "Data Collection" +
            "\n" +
            "\nOur split bill app does not collect any personal information or data from its users. We do not track user behavior, nor do we use cookies or any other tracking technology. The only data that we collect is related to the bills that users input into the app. This data is stored locally on the user's device using the Room database." +
            "\n" +
            "\nData Protection" +
            "\nWe take the privacy and security of our users very seriously, and we use industry-standard measures to protect the data that is stored on our users' devices." +
            "\n" +
            "\nContact Me" +
            "\nIf you had any question or issues you can always reach me at rizkysendiko7@gmail.com or file an issue here: https://github.com/Sendiko/split-the-bill/issues, i'll help you with your issues and/or questions!"
