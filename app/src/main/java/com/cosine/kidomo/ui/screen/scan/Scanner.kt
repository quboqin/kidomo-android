package com.cosine.kidomo.ui.screen.scan

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.BitmapFactory
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
import com.cosine.kidomo.ui.theme.MEDIUM_PADDING
import com.cosine.kidomo.ui.viewmodels.MainViewModel
import com.cosine.kidomo.util.getImageUriFromBitmap
import com.cosine.kidomo.util.resizeImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScannerScreen(
    onBackButtonPressed: () -> Unit,
    mainViewModel: MainViewModel
) {
    val imageUri by mainViewModel.imageUri
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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var showGallerySelect by remember { mutableStateOf(false) }
                    if (showGallerySelect) {
                        GallerySelect(
                            onImageUri = { uri ->
                                showGallerySelect = false
                                mainViewModel.updateImageUri(uri)
                            }
                        )
                    } else {
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
                                        mainViewModel.updateImageUri(EMPTY_IMAGE_URI)
                                    }
                                ) {
                                    Text("Remove image")
                                }
                            }
                        } else {
                            Box() {
                                CameraCapture(
                                    onImageFile = { file ->
                                        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                                        val resizedImage = resizeImage(bitmap, 200, 200)
                                        // Convert the resized image to a URI and update the image URI
                                        val resizedImageUri =
                                            getImageUriFromBitmap(context, resizedImage)
                                        mainViewModel.updateImageUri(resizedImageUri)
                                    }
                                )
                                Button(
                                    modifier = Modifier.align(Alignment.BottomStart).padding(MEDIUM_PADDING),
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
