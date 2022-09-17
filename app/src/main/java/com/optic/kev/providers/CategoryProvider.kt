package com.optic.kev.providers

import com.optic.kev.api.ApiRoutes
import com.optic.kev.models.Category
import com.optic.kev.models.ResponseHttp
import com.optic.kev.models.User
import com.optic.kev.routes.CategoryRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class CategoryProvider (  val token:String ){
    private var categoriesRoutes:CategoryRoutes?= null
    //constructor dfe la clase es init
    init{
        val api = ApiRoutes()
        categoriesRoutes= api.getCategoryRoutes(token)
    }
    fun create(file: File, category: Category): Call<ResponseHttp>? {
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        val image = MultipartBody.Part.createFormData("image", file.name, reqFile)
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), category.toJson())
        return categoriesRoutes?.create(image,requestBody,token)
    }

    fun getAll( ): Call<ArrayList<Category>>? {
        return categoriesRoutes?.getAll(token)
    }
}