package com.example.dm_project

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.INTERNET),
                resources.getIdentifier(
                    "MY_PERMISSIONS_REQUEST_INTERNET",
                    packageName,
                    "Integer"))
        }

        setContentView(R.layout.activity_main)
        val taskFormIntent = Intent(this, TaskFormActivity::class.java)

        val button = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        button.setOnClickListener {
            startActivity(taskFormIntent)
        }
    }
}
