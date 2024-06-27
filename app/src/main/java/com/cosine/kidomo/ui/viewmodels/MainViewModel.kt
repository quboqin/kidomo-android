package com.cosine.kidomo.ui.viewmodels

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.cosine.kidomo.ui.screen.scan.EMPTY_IMAGE_URI

class MainViewModel(
) : ViewModel() {

    val imageUri: MutableState<Uri> = mutableStateOf(EMPTY_IMAGE_URI)
    val taskId: MutableState<Int> = mutableIntStateOf(0)

    fun updateImageUri(newImageUri: Uri) {
        imageUri.value = newImageUri
    }

    fun updateTaskId(newTaskId: Int) {
        taskId.value = newTaskId
    }
}