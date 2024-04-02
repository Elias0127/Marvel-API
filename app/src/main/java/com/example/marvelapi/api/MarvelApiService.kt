package com.example.marvelapp.network

import com.example.marvelapi.model.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

interface MarvelApiService {
    @GET("v1/public/characters")
    suspend fun getCharacters(
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String,
        @Query("ts") timestamp: String
    ): Response<CharacterResponse>
}

