package com.example.passecure.util

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate

class ThemeManager (context: Context) {
    private val localStorage: LocalStorage = LocalStorage(context)
    var number: Int = 0

    enum class Theme {
        LIGHT, DARK, SYSTEM, BATTERY
    }

    private val storageKey = "theme"
    val keyThemePosition: String = "themeposition"

    val shouldShowSystemMode = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    private val nightMode = if(shouldShowSystemMode) {
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM

    } else {
        AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
    }

    val currentTheme: Theme?
        get() = localStorage.retreiveObject(storageKey)

    fun applyCurrent() {
        applyTheme(currentTheme)
    }

    fun applyTheme(theme: Theme? = null) {
        if (theme == null){
            AppCompatDelegate.setDefaultNightMode(nightMode)
            return
        }

        localStorage.save(storageKey, theme)

        when(theme) {
            Theme.LIGHT -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                number = 0
            }
            Theme.DARK -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                number = 1
            }
            Theme.SYSTEM -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                number = 2
            }
            Theme.BATTERY -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                number = 3
            }
        }

        localStorage.saveInt(keyThemePosition, number)
    }

    fun getThemePosition(): Int{
        return localStorage.loadInt(keyThemePosition)
    }
}