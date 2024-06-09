package com.cosine.kidomo.ui.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource

import com.cosine.kidomo.R
import com.cosine.kidomo.dialog.TodoAlertDialog
import com.cosine.kidomo.ui.theme.*

@Composable
fun HomeAppBar(
    onDeleteAllConfirmed : () -> Unit
) {
    HomeTopAppBar(
        onDeleteAllConfirmed = {
            onDeleteAllConfirmed()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    onDeleteAllConfirmed: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.list_screen_title),
                color = MaterialTheme.colorScheme.topAppBarContent
            )
        },
        actions = {
            HomeAppBarActions(
                onDeleteAllConfirmed = onDeleteAllConfirmed
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.topAppBarBackground,
            titleContentColor = MaterialTheme.colorScheme.topAppBarBackground,
            actionIconContentColor = MaterialTheme.colorScheme.topAppBarBackground
        )

    )
}

@Composable
fun HomeAppBarActions(
    onDeleteAllConfirmed: () -> Unit
) {
    var isShowDialog by remember { mutableStateOf(false) }

    TodoAlertDialog(
        title = stringResource(id = R.string.delete_all_tasks),
        msg = stringResource(id = R.string.delete_all_tasks_confirmation),
        isShowDialog = isShowDialog,
        onNoClicked = { isShowDialog = false },
        onYesClicked = { onDeleteAllConfirmed() }
    )

    DeleteAllAction(onDeleteAllConfirmed = { isShowDialog = true })
}

@Composable
fun DeleteAllAction(
    onDeleteAllConfirmed: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(
        onClick = { expanded = true }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_more),
            contentDescription = stringResource(id = R.string.delete_all_action),
            tint = MaterialTheme.colorScheme.topAppBarContent
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        "Item 3"
                    )
                },
                onClick = {
                    expanded = false
                    onDeleteAllConfirmed()
                }
            )
        }
    }
}