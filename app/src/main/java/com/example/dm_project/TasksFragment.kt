package com.example.dm_project

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dm_project.network.TasksRepository
import kotlinx.android.synthetic.main.tasks_fragment.view.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

class TasksFragment : Fragment(){
    private val coroutineScope = MainScope()
    private val tasksRepository = TasksRepository()
    private val tasks = mutableListOf<Task>()
    private lateinit var taskAdapter : TasksAdapter
    private val taskViewModel by lazy { ViewModelProviders.of(this).get(TasksViewModel::class.java)}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?) : View?
    {
        taskAdapter = TasksAdapter(tasks, context!!)
        val view = inflater.inflate(R.layout.tasks_fragment, container)

        view.tasks_recycler_view.adapter = taskAdapter
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
        //taskViewModel.loadTasks(this)
        tasksRepository.getTasks().observe(this, Observer {
            if( it != null){
                tasks.clear()
                tasks.addAll(it)
                taskAdapter.notifyDataSetChanged()
            }
        })
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