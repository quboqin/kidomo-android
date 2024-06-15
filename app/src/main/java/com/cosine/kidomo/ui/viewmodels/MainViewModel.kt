package com.cosine.kidomo.ui.viewmodels

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cosine.kidomo.ui.screen.scan.EMPTY_IMAGE_URI
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainViewModel(
) : ViewModel() {

    val imageUri: MutableState<Uri> = mutableStateOf(EMPTY_IMAGE_URI)

    fun updateImageUri(newImageUri: Uri)  {
        imageUri.value = newImageUri
    }

    fun nativeTask(arg: Map<String, Any>, onBackButtonPressed: () -> Unit): String {
        var jsonString: String = ""
        viewModelScope.launch {
            val action = arg["action"] as? String

            if (action == "send_credentials") {
                // Assuming credentials have been sent successfully
                val jsonObject = JSONObject()
                jsonObject.put("key1", "value1")
                jsonObject.put("key2", 12345)
                jsonObject.put("key3", true)

                // 将 JSON 对象转换为字符串
                jsonString = jsonObject.toString()
            } else if (action == "back") {
                onBackButtonPressed()
            }
        }
        return jsonString
    }

}