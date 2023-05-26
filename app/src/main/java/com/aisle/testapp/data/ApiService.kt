package com.aisle.testapp.data

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("users/phone_number_login")
    suspend fun login(@Body numJson:JsonObject):Response<JsonObject>


    @POST("users/verify_otp")
    suspend fun verifyOtp(@Body otpJson:JsonObject):Response<JsonObject>


    @GET("users/test_profile_list")
    suspend fun getNotesList(@Header("Authorization") authToken:String):Response<JsonObject>

}