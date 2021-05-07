package com.example.passecure.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavHost
import com.example.passecure.R
import com.example.passecure.util.ThemeManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var themeManager: ThemeManager

    private var singleItems: Array<String> = arrayOf()

    private var pressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHost).navController
        themeManager = ThemeManager(this)

        if (themeManager.shouldShowSystemMode){
            singleItems = arrayOf("Battery", "Light", "Dark", "System")
        } else {
            singleItems = arrayOf("Battery", "Light", "Dark")
        }
    }

    override fun onRestart() {
        super.onRestart()
        val intent = Intent(this, BiometricActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
        } else {
            notifyUser("Press back again to exit.")
        }
        pressedTime = System.currentTimeMillis()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.newItem -> {
                navController.navigate(R.id.action_global_newItemFragment)
                true
            }
            R.id.toggleTheme -> {
                showThemeSelector()
                //navController.navigate(R.id.action_listFragment_to_settingsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showThemeSelector(){
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.themeSelectTitle))
            .setNeutralButton(resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.select)) { dialog, _ ->
                themeManager.applyCurrent()
                dialog.dismiss()
            }
            .setSingleChoiceItems(singleItems, themeManager.getThemePosition()) { dialog, which ->
                val selectedTheme = when(which){
                    0 -> ThemeManager.Theme.BATTERY
                    1 -> ThemeManager.Theme.LIGHT
                    2 -> ThemeManager.Theme.DARK
                    3 -> ThemeManager.Theme.SYSTEM
                    else -> null
                }
                themeManager.applyTheme(selectedTheme)
                notifyUser("Selected theme: ${themeManager.currentTheme.toString().toLowerCase().capitalize()}")
                dialog.dismiss()
            }
            .show()
    }

    private fun notifyUser(string: String){
        Toast.makeText(baseContext, string, Toast.LENGTH_SHORT).show()
    }
}

