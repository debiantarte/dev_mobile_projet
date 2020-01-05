package com.example.dm_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.td2.R
import kotlinx.android.synthetic.main.fragment_authentication.*


class AuthenticationFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signin.setOnClickListener{
            findNavController().navigate(R.id.go_to_loginFragment)
        }

        signup.setOnClickListener{
            findNavController().navigate(R.id.go_to_signupFragment)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.authentication_fragment, container, false)
    }


}
