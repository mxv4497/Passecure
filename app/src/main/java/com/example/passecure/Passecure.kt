package com.example.passecure

import android.app.Application
import com.example.passecure.util.ThemeManager

class Passecure: Application() {
    override fun onCreate() {
        super.onCreate()
        ThemeManager(this).applyCurrent()
    }
}