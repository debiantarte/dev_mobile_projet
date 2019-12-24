package com.example.dm_project

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.tasks_fragment.view.*

class TasksFragment : Fragment(){
    private val coroutineScope = MainScope()
    private val tasksRepository = TaskRepository()
    private val tasks = mutableListOf<Task>()
    private val taskAdapter = TaskAdapter(tasks)
    private val taskViewModel by lazy { ViewModelProviders.of(this).get(TasksViewModel::class.java)}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?) : View?
    {
        val view = inflater.inflate(R.layout.tasks_fragment, container)
        view.tasks_recycler_view.adapter = taskViewModel.tasksAdapter
        view.tasks_recycler_view.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // subscribe the fragment to task update
        tasksRepository.getTasks().observe(this, Observer {
            if( it != null){
                tasks.clear()
                tasks.addAll(it)
                taskAdapter.notifyDataSetChanged()
            }
        })
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        taskViewModel.loadTasks(this)
        super.onResume()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

    /*override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
    }*/
}