package com.example.passecure

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

class BiometricActivity : AppCompatActivity() {
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biometric)
        authenticateUser()
    }

    override fun onResume() {
        super.onResume()
        authenticateUser()
    }

    private fun authenticateUser(){
        executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                val intent = Intent(this@BiometricActivity, MainActivity::class.java)
                startActivity(intent)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(resources.getString(R.string.biometricTitle))
            .setSubtitle(resources.getString(R.string.biometricSubtitle))
            .setNegativeButtonText(resources.getString(R.string.exit))
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun notifyUser(string: String){
        Toast.makeText(baseContext, string, Toast.LENGTH_SHORT).show()
    }
}