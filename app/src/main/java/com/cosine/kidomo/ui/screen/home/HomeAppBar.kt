package com.cosine.kidomo.ui.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.*
import com.cosine.kidomo.R
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
                text = stringResource(id = R.string.home)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary, // Background color
            titleContentColor = MaterialTheme.colorScheme.onPrimary // Title text color
        )

    )
}

