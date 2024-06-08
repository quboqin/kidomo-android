package com.cosine.kidomo.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.cosine.kidomo.R

import com.cosine.kidomo.ui.theme.*

@Composable
fun TodoAlertDialog(
    title: String,
    msg: String,
    isShowDialog: Boolean,
    onNoClicked: () -> Unit,
    onYesClicked: () -> Unit,
) {
    if (isShowDialog) {
        AlertDialog(
            title = {
                Text(
                    text = title,
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = msg,
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    fontWeight = FontWeight.Normal
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onYesClicked()
                        onNoClicked()
                    })
                {
                    Text(text = stringResource(id = R.string.yes))
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { onNoClicked() })
                {
                    Text(text = stringResource(id = R.string.no))
                }
            },
            onDismissRequest = { onNoClicked() }
        )
    }
}