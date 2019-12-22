package com.example.dm_project

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

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
    }

    /*override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        val removedArray : IntArray? = outState.getIntArray("removed_list")
        outState.putIntArray("removed_list", removedArray)
        super.onSaveInstanceState(outState, outPersistentState)
    }*/
}
