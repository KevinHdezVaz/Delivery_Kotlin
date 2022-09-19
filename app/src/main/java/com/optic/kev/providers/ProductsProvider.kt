package com.optic.kev.providers

import com.optic.kev.api.ApiRoutes
import com.optic.kev.models.Category
import com.optic.kev.models.Product
import com.optic.kev.models.ResponseHttp
import com.optic.kev.models.User
import com.optic.kev.routes.CategoryRoutes
import com.optic.kev.routes.ProductsRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class ProductsProvider (val token:String ){

    private var productsRoutes:ProductsRoutes?= null
    //constructor dfe la clase es init
    init{
        val api = ApiRoutes()
        productsRoutes= api.getProductsroutes(token)
    }
    fun findbyCategory(idCategory:String ): Call<ArrayList<Product>>? {
        return productsRoutes?.finbyCategory(idCategory, token)
    }

    fun create(files: List<File>, product: Product): Call<ResponseHttp>? {

        val images = arrayOfNulls<MultipartBody.Part>(files.size)

        for (i in 0 until files.size){

            val reqFile = RequestBody.create(MediaType.parse("image/*"), files[i])
            images[i] = MultipartBody.Part.createFormData("image", files[i].name, reqFile)
        }


        val requestBody = RequestBody.create(MediaType.parse("text/plain"), product.toJson())
        return productsRoutes?.create(images,requestBody,token)
    }

}