package com.optic.kev.routes


import com.optic.kev.models.ResponseHttp
import com.optic.kev.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface UsersRoutes {

    @POST("api/users/create")
    fun register(@Body user:User): Call<ResponseHttp>

    @FormUrlEncoded
    @POST("api/users/login")
    fun login(@Field("email") email: String, @Field("password") password: String): Call<ResponseHttp>


    //subir imagenes
    @Multipart
    @PUT("api/users/update")
    fun update(@Part image: MultipartBody.Part,
        @Part ("user") user: RequestBody, @Header("Authorization")token:String  ): Call<ResponseHttp>

    //subir imagenes

    @PUT("api/users/updatewithoutImage")
    fun updateWithoutImage(@Body user: User, @Header("Authorization")token:String  ): Call<ResponseHttp>


}