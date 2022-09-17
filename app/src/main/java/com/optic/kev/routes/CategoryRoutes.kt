package com.optic.kev.routes

import com.optic.kev.models.Category
import com.optic.kev.models.ResponseHttp
import com.optic.kev.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface CategoryRoutes {
     @GET("api/categories/getAll")
     fun getAll(
         @Header("Authorization")token:String

     ): Call<ArrayList<Category>>


    //subir imagenes
    @Multipart
    @POST("api/categories/create")
    fun create(@Part image: MultipartBody.Part,
               @Part("category") user: RequestBody, @Header("Authorization")token:String  ): Call<ResponseHttp>



}