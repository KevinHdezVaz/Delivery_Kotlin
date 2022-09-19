package com.optic.kev.routes

import com.optic.kev.models.Category
import com.optic.kev.models.Product
import com.optic.kev.models.ResponseHttp
import com.optic.kev.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ProductsRoutes {

     @GET("/api/products/findCategory/{id_category}")
     fun finbyCategory(
         @Path("id_category") idCategory: String,
         @Header("Authorization")token:String

     ): Call<ArrayList<Product>>




    //subir imagenes
    @Multipart
    @POST("api/products/create")
    fun create(@Part images: Array<MultipartBody.Part?>,
               @Part("product") product: RequestBody,
               @Header("Authorization")token:String  ): Call<ResponseHttp>



}