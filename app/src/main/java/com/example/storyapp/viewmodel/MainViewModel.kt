package com.example.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.database.ListStoryDetail
import com.example.storyapp.dataclass.LoginDataAccount
import com.example.storyapp.dataclass.RegisterDataAccount
import com.example.storyapp.dataclass.ResponseLogin
import com.example.storyapp.repository.MainRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    fun setMessageValue(value: String) {
        messageLiveData.value = value
    }

    val stories: LiveData<List<ListStoryDetail>> = mainRepository.stories

    private val messageLiveData = MutableLiveData<String>()
    var message: LiveData<String> = mainRepository.message

    var isLoading: LiveData<Boolean> = mainRepository.isLoading

    val userlogin: LiveData<ResponseLogin> = mainRepository.userlogin

    fun login(loginDataAccount: LoginDataAccount) {
        mainRepository.getResponseLogin(loginDataAccount)
    }

    fun register(registDataUser: RegisterDataAccount) {
        mainRepository.getResponseRegister(registDataUser)
    }

    fun upload(
        photo: MultipartBody.Part,
        des: RequestBody,
        lat: Double?,
        lng: Double?,
        token: String
    ) {
        mainRepository.upload(photo, des, lat, lng, token)
    }

    @ExperimentalPagingApi
    fun getPagingStories(token: String): LiveData<PagingData<ListStoryDetail>> {
        return mainRepository.getPagingStories(token).cachedIn(viewModelScope)
    }

    fun getStories(token: String) {
        mainRepository.getStories(token)
    }
}
