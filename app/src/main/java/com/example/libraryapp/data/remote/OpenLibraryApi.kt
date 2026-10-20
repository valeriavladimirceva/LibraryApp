package com.example.libraryapp.data.remote

import com.example.libraryapp.data.remote.dto.SearchResponseDto
import com.example.libraryapp.data.remote.dto.WorkDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenLibraryApi {
    @GET("search.json")
    suspend fun search(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("fields") fields: String =
            "key,title,author_name,cover_i,first_publish_year,publisher,isbn"
    ) : SearchResponseDto

    @GET("works/{id}.json")
    suspend fun getWork(
        @Path("id") id: String
    ) : WorkDto
}