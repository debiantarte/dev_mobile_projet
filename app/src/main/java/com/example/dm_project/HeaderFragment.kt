package com.example.dm_project

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.dm_project.network.API
import kotlinx.android.synthetic.main.header_fragment.*
import kotlinx.android.synthetic.main.header_fragment.view.*
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
            val name: String? = API.INSTANCE.userService.getInfo().body()?.firstName
            view.headerText.text = name
            val url = API.INSTANCE.userService.getInfo().body()?.avatar?: "https://goo.gl/gEgYUd"
            Glide.with(this@HeaderFragment).load(url).fitCenter().circleCrop().into(user_avatar)
        }
        return view
    }

    override fun onResume() {
        coroutineScope.launch {
            val info = API.INSTANCE.userService.getInfo()
            view?.findViewById<TextView>(R.id.headerText)?.text = info.body()?.email.toString()
            val url = info.body()?.avatar?: "https://goo.gl/gEgYUd"
            Glide.with(this@HeaderFragment).load(url).fitCenter().circleCrop().into(user_avatar)
        }

        user_avatar.isClickable = true
        user_avatar.setOnClickListener {
            val selectAvatarIntent = Intent(activity?.baseContext, UserInfoActivity::class.java)
            startActivity(selectAvatarIntent)
        }

        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}