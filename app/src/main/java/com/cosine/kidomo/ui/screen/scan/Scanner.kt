package com.cosine.kidomo.ui.screen.scan

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScannerScreen(
    onBackButtonPressed: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Scanner")
                },
                navigationIcon = {
                    IconButton(onClick = onBackButtonPressed) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            )
            {
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {
                    var imageUri by remember { mutableStateOf(EMPTY_IMAGE_URI) }
                    if (imageUri != EMPTY_IMAGE_URI) {
                        Box() {
                            Image(
                                modifier = Modifier.fillMaxSize(),
                                painter = rememberAsyncImagePainter(imageUri),
                                contentDescription = "Captured image"
                            )
                            Button(
                                modifier = Modifier.align(Alignment.BottomCenter),
                                onClick = {
                                    imageUri = EMPTY_IMAGE_URI
                                }
                            ) {
                                Text("Remove image")
                            }
                        }
                    } else {
                        var showGallerySelect by remember { mutableStateOf(false) }
                        if (showGallerySelect) {
                            GallerySelect(
                                onImageUri = { uri ->
                                    showGallerySelect = false
                                    imageUri = uri
                                }
                            )
                        } else {
                            Box() {
                                CameraCapture(
                                    onImageFile = { file ->
                                        imageUri = file.toUri()
                                    }
                                )
                                Button(
                                    modifier = Modifier
                                        .align(Alignment.TopCenter)
                                        .padding(4.dp),
                                    onClick = {
                                        showGallerySelect = true
                                    }
                                ) {
                                    Text("Select from Gallery")
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

val EMPTY_IMAGE_URI: Uri = Uri.parse("file://dev/null")