package com.cosine.kidomo.ui.viewmodels

import android.net.Uri
import android.os.Bundle
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cosine.kidomo.ui.screen.scan.EMPTY_IMAGE_URI
import kotlinx.coroutines.launch

class MainViewModel(
) : ViewModel() {

    val imageUri: MutableState<Uri> = mutableStateOf(EMPTY_IMAGE_URI)
    val taskId: MutableState<String> = mutableStateOf("0")

    var isWebViewLoaded = MutableLiveData<Boolean>(false)

    // Add webViewState to hold the WebView state
    var webViewState: Bundle? = null

    fun updateImageUri(newImageUri: Uri) {
        imageUri.value = newImageUri
    }

    fun updateTaskId(newTaskId: String) {
        taskId.value = newTaskId
    }

    fun nativeTask(onBackButtonPressed: () -> Unit) {
        viewModelScope.launch {
            onBackButtonPressed()
        }
    }
}