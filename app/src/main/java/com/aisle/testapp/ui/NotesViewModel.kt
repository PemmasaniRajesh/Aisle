package com.aisle.testapp.ui

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aisle.testapp.data.NotesRepository
import com.aisle.testapp.others.AppConstants
import com.aisle.testapp.others.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
    private val sharedPreferences: SharedPreferences
) :
    ViewModel() {

    private val _notesResult: MutableLiveData<NetworkResult<Any>> = MutableLiveData()
    val notesResult: LiveData<NetworkResult<Any>> = _notesResult

    init {
        getNotesList()
    }

    private fun getNotesList() = viewModelScope.launch(Dispatchers.IO) {
         val authToken = sharedPreferences.getString(AppConstants.USER_AUTH_TOKEN,"")
        _notesResult.postValue(NetworkResult.Loading(true))
        _notesResult.postValue(notesRepository.getNotesList(authToken!!))
    }
}