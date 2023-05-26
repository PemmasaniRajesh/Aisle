package com.aisle.testapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aisle.testapp.data.LoginRepository
import com.aisle.testapp.others.NetworkResult
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: LoginRepository):ViewModel() {

    private val _loginResponse:MutableLiveData<NetworkResult<Any>> = MutableLiveData()
    val loginResponse:LiveData<NetworkResult<Any>> = _loginResponse

    private val _verifyOtpResponse:MutableLiveData<NetworkResult<Any>> = MutableLiveData()
    val verifyOtpResponse:LiveData<NetworkResult<Any>> = _verifyOtpResponse

    fun login(jsonObject: JsonObject) = viewModelScope.launch(Dispatchers.IO) {
        _loginResponse.postValue(NetworkResult.Loading(true))
        _loginResponse.postValue(repository.login(jsonObject))
    }

    fun verifyOtp(jsonObject: JsonObject) = viewModelScope.launch(Dispatchers.IO) {
        _verifyOtpResponse.postValue(NetworkResult.Loading(true))
        _verifyOtpResponse.postValue(repository.verifyOtp(jsonObject))
    }
}