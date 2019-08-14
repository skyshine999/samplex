package com.pro.simpsons.networkrepo

import com.pro.simpsons.home.models.Character
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {
    @GET("/")
    fun getCharacter(@Query("q") q: String, @Query("format") format: String): Call<Character>


}
