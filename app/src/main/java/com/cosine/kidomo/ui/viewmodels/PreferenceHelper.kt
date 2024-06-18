package com.cosine.kidomo.ui.viewmodels

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class PreferenceHelper<T>(private val context: Context, private val prefName: String, private val keyName: String) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    private val gson: Gson = GsonBuilder().create()

    fun saveObject(obj: T) {
        val jsonString = gson.toJson(obj)
        val editor = sharedPreferences.edit()
        editor.putString(keyName, jsonString)
        editor.apply()
    }

    fun getObject(clazz: Class<T>): T? {
        val jsonString = sharedPreferences.getString(keyName, null)
        return gson.fromJson(jsonString, clazz)
    }

    fun deleteObject() {
        val editor = sharedPreferences.edit()
        editor.remove(keyName)
        editor.apply()
    }

    fun saveString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun deleteString(key: String) {
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }
}