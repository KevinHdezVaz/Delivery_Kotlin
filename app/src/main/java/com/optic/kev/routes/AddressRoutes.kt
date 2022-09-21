package com.optic.kev.routes

import com.optic.kev.models.Address
import com.optic.kev.models.Category
import com.optic.kev.models.Product
import com.optic.kev.models.ResponseHttp
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface AddressRoutes {

    @GET("api/address/findbyUser/{id_user}")
    fun getAddress(
        @Path("id_user") idUser: String,
        @Header("Authorization") token: String
    ): Call<ArrayList<Address>>


    @POST("api/address/create")
    fun create(
        @Body address: Address,
        @Header("Authorization")token:String  ): Call<ResponseHttp>



}