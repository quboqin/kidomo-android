package com.cosine.kidomo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainViewModel(
) : ViewModel() {

    fun nativeTask(arg: Map<String, Any>): String {
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
            }
        }
        return jsonString
    }

}