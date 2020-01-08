package com.example.dm_project


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.dm_project.network.API
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TaskFormActivity : AppCompatActivity() {

    private val coroutineScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_form_activity)

        val button = findViewById<Button>(R.id.back)
        button.setOnClickListener{
            val createActivityIntent : Intent = Intent(this, MainActivity::class.java)
            val title = findViewById<TextView>(R.id.title).text.toString()
            val description = findViewById<TextView>(R.id.description).text.toString()

            coroutineScope.launch {
                API.INSTANCE.tasksService.createTask(Task("id_$title", title, description))
            }
            startActivity(createActivityIntent)
        }
    }
}
