package com.aisle.testapp.data

import com.aisle.testapp.others.NetworkResult
import com.google.gson.JsonObject
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val apiService: ApiService):LoginRepository {

    override suspend fun login(numJson: JsonObject): NetworkResult<Any> {
        return try {
            val response = apiService.login(numJson)
            if(response.isSuccessful){
                val body = response.body()
                NetworkResult.Success(body)
            }else{
                NetworkResult.Failure(response.message())
            }
        }catch (e:Exception){
            NetworkResult.Failure(e.message.toString())
        }
    }

    override suspend fun verifyOtp(otpJson: JsonObject): NetworkResult<Any>  {
        return try {
            val response = apiService.verifyOtp(otpJson)
            if(response.isSuccessful){
                NetworkResult.Success(response.body())
            }else{
                NetworkResult.Failure(response.message())
            }
        }catch (e:Exception){
            NetworkResult.Failure(e.message.toString())
        }
    }
}