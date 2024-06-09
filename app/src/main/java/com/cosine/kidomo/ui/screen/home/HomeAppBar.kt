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
) {
    HomeTopAppBar(
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.home),
                color = MaterialTheme.colorScheme.topAppBarContent
            )
        }
    )
}

