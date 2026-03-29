package com.example.taskmanagerapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope

val Context.dataStore by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()

            lifecycleScope.launch {
                dataStore.edit { preferences ->
                    preferences[stringPreferencesKey("username")] = username
                }
            }

            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }
    }
}
