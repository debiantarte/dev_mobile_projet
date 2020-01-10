package com.example.dm_project


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.dm_project.network.API
import kotlinx.android.synthetic.main.item_task.*
import kotlinx.android.synthetic.main.task_form_activity.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TaskFormActivity : AppCompatActivity() {

    private val coroutineScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_form_activity)

        val button = findViewById<Button>(R.id.back)

        findViewById<EditText>(R.id.title).setText(intent.getStringExtra("title")?:"")
        findViewById<EditText>(R.id.description).setText(intent.getStringExtra("description")?:"")
        val id = intent.getStringExtra("id")
        if (id == null) {
            button.setOnClickListener { onCreateClickListener() }
        }
        else {
            button.setOnClickListener { onEditClickListener(id) }
        }
    }

    fun onEditClickListener(id: String) {
        val createActivityIntent = Intent(this, MainActivity::class.java)
        val title = findViewById<EditText>(R.id.title).text.toString()
        val description = findViewById<EditText>(R.id.description).text.toString()
        if (title != "") {
            val task = Task(id, title, description)
            MainScope().launch {
                API.INSTANCE.tasksService.updateTask(id, task)
            }
        }
        startActivity(createActivityIntent)
    }

    fun onCreateClickListener() {
        val createActivityIntent = Intent(this, MainActivity::class.java)
        val title = findViewById<TextView>(R.id.title).text.toString()
        val description = findViewById<TextView>(R.id.description).text.toString()

        coroutineScope.launch {
            API.INSTANCE.tasksService.createTask(Task("id_$title", title, description))
        }
        startActivity(createActivityIntent)
    }
}
