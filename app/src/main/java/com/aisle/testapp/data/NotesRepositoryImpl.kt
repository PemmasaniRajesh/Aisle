package com.aisle.testapp.data

import com.aisle.testapp.others.NetworkResult
import java.lang.Exception
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    NotesRepository {
    override suspend fun getNotesList(authToken:String): NetworkResult<Any> {
        return try {
            val response = apiService.getNotesList(authToken)
            if (response.isSuccessful) {
                val body = response.body()
                NetworkResult.Success(body)
            } else {
                NetworkResult.Failure(response.message())
            }
        } catch (e: Exception) {
            NetworkResult.Failure(e.message.toString())
        }
    }
}