package com.example.dm_project

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.dm_project.network.API
import com.example.dm_project.network.UserInfo
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class HeaderFragment : Fragment() {
    private val coroutineScope = MainScope()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?) : View?
    {
        val view = inflater.inflate(R.layout.header_fragment, container)
        coroutineScope.launch {
            val name: String? = Api.userService.getInfo().body()?.firstname
            view.user_name.text = name
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        coroutineScope.launch {
            val info = API.userService.getInfo()
            view?.findViewById<TextView>(R.id.headerText)?.text = info.body()?.email.toString()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}