package com.example.passecure.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun<reified T> LocalStorage.retreiveObject(key: String): T? {
    this.load<String>(key)?.let {
        return Gson().fromJson(it, object: TypeToken<T>() {}.type)
    }
    return null
}

class LocalStorage(context: Context) {

    private val preferencesName = context.applicationInfo.name
    private val sharedPreferences = context.getSharedPreferences(
            preferencesName,
            Context.MODE_PRIVATE
    )

    fun<T> save(key: String, value: T) {
        val editor = sharedPreferences.edit()

        when(value) {
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Float -> editor.putFloat(key, value)
            is Long -> editor.putLong(key, value)
            is Boolean -> editor.putBoolean(key, value)
            else -> {
                val json = Gson().toJson(value)
                editor.putString(key, json)
            }
        }

        editor.apply()
    }

    fun<T> load(key: String): T? {
        return sharedPreferences.all[key] as? T
    }

    fun clearAll(){
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    fun saveInt(key: String, number: Int){
        val editor = sharedPreferences.edit()

        editor.putInt(key, number)

        editor.apply()
    }

    fun loadInt(key: String): Int {
        return sharedPreferences.getInt(key, 0)
    }
}