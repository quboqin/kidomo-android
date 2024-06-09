package com.cosine.kidomo.ui.screen.splash

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cosine.kidomo.R
import com.cosine.kidomo.util.AppHolder.SPLASH_DELAY
import com.cosine.kidomo.ui.theme.*
import kotlinx.coroutines.delay


@Composable
fun Splash(
    gotoHomeScreen: () -> Unit
) {
    var start by remember { mutableStateOf(false) }
    val offset by animateDpAsState(
        targetValue = if (start) 0.dp else 100.dp,
        animationSpec = tween(
            durationMillis = 1000
        ), label = ""
    )
    val alpha by animateFloatAsState(
        targetValue = if (start) 1f else 0f,
        animationSpec = tween(
            durationMillis = 2000
        ), label = ""
    )

    LaunchedEffect(key1 = Unit) {
        start = true
        delay(SPLASH_DELAY)
        gotoHomeScreen()
    }

    Splash(offsetState = offset, alphaState = alpha)
}

@Composable
fun Splash(offsetState: Dp, alphaState: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.splashBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .size(LOGO_HEIGHT)
                    .alpha(alpha = alphaState),
                painter = painterResource(id = getLogo()),
                contentDescription = stringResource(id = R.string.kidomo_logo)
            )

            Text(
                modifier = Modifier
                    .offset(y = offsetState)
                    .alpha(alpha = alphaState),
                text = stringResource(id = R.string.app_name),
                color = MaterialTheme.colorScheme.splashText,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
            )
        }
    }
}

@Composable
fun getLogo(): Int {
    return if (isSystemInDarkTheme()) {
        R.drawable.kidomo_logo
    } else {
        R.drawable.kidomo_logo
    }
}

@Composable
@Preview
private fun SplashScreenPreview() {
    Splash(offsetState = 0.dp, alphaState = 1f)
}