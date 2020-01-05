package com.example.dm_project

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.example.dm_project.network.API
import com.example.dm_project.network.UserInfo
import kotlinx.android.synthetic.main.authentication_fragment.*
import kotlinx.android.synthetic.main.signup_fragment.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class SignupFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {
        return inflater.inflate(R.layout.signup_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button3.setOnClickListener {

            trySignup()
        }
    }

    private fun trySignup() {
        if (firstname.text.isEmpty() || lastname.text.isEmpty() || email.text.isEmpty()
            || password.text.isEmpty() || password_confirmation.text.isEmpty())
            return

        MainScope().launch {
            val response = API.INSTANCE.userService.signup(
                SignupForm(firstname.text.toString(), lastname.text.toString(), email.text.toString(),
                    password.text.toString(), password_confirmation.text.toString()))
            if (response.isSuccessful)
            {
                displayTasks(response.toString())
            }
            else
            {
                displayError(response.message())
            }
        }
    }

    private fun storeToken(token: String)
    {
        PreferenceManager.getDefaultSharedPreferences(context).edit {
            putString(SHARED_PREF_TOKEN_KEY, token)
        }
    }

    private fun displayTasks(token: String)
    {
        storeToken(token)
        Toast.makeText(context, "Ahuhauogh3", Toast.LENGTH_LONG).show()
        val intent = Intent(activity?.applicationContext, MainActivity::class.java)
        startActivity(intent)
    }

    private fun displayError(message: String)
    {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        println(message)
    }
}