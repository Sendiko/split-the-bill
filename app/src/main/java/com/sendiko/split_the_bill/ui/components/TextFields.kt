package com.sendiko.split_the_bill.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlinedTextFields(
    value: String,
    onNewValue: (String) -> Unit,
    hint: String,
    isError: Boolean,
    leadingIcon: @Composable () -> Unit,
    errorMessage: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onNewValue,
        shape = RoundedCornerShape(100),
        modifier = modifier,
        placeholder = {
            Text(text = hint)
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            placeholderColor = MaterialTheme.colorScheme.inversePrimary,
            cursorColor = MaterialTheme.colorScheme.primary,
        ),
        isError = isError,
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        leadingIcon = leadingIcon,
        supportingText = {
            Text(text = errorMessage)
        }
    )
}