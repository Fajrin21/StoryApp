package com.example.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.api.APIConfig
import com.example.storyapp.dataclass.ResponseStory
import com.example.storyapp.dataclass.DetailStory
import retrofit2.Call
import retrofit2.Response

class HomePageViewModel : ViewModel() {

    var stories: List<DetailStory> = listOf()

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    var isError: Boolean = false

    fun getStories(token: String) {
        _isLoading.value = true
        val api = APIConfig.getApiService().getStory("Bearer $token")
        api.enqueue(object : retrofit2.Callback<ResponseStory> {
            override fun onResponse(call: Call<ResponseStory>, response: Response<ResponseStory>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    isError = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        stories = responseBody.listStory
                    }
                    _message.value = responseBody?.message.toString()

                } else {
                    isError = true
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<ResponseStory>, t: Throwable) {
                _isLoading.value = false
                isError = true
                _message.value = t.message.toString()
            }
        })
    }
}
