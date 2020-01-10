package com.example.dm_project.network

import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class UserRepository {
    private val userService = API.INSTANCE.userService
    private val coroutineScope = MainScope()

    fun getAvatarUri(): LiveData<Uri?> {
        val uri = MutableLiveData<Uri?>()
        coroutineScope.launch {
            uri.postValue( loadAvatarUri())
        }
        return uri
    }

    private suspend fun loadAvatarUri(): Uri? {
        val avatarResponse =  userService.getInfo()
        return if (avatarResponse.isSuccessful) avatarResponse.body()?.avatar?.toUri() else null
    }
}