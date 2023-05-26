package com.aisle.testapp.data

import com.aisle.testapp.others.NetworkResult
import com.google.gson.JsonObject
import retrofit2.Response

interface LoginRepository {

    suspend fun login(numJson:JsonObject):NetworkResult<Any>

    suspend fun verifyOtp(otpJson:JsonObject):NetworkResult<Any>
}