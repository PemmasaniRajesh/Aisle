package com.aisle.testapp.data

import com.aisle.testapp.others.NetworkResult

interface NotesRepository {

    suspend fun getNotesList(authToken:String):NetworkResult<Any>
}