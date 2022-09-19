package com.optic.kev.api

import com.optic.kev.routes.CategoryRoutes
import com.optic.kev.routes.ProductsRoutes
import com.optic.kev.routes.UsersRoutes


class ApiRoutes {

    val apiUrl =  "http://192.168.11.16:3000/"
    val retrofit = RetrofitClient()

    fun getUsersRoutes(): UsersRoutes {
        return retrofit.getClient(apiUrl).create(UsersRoutes::class.java)
    }
    fun getUsersRouteswithToken(token:String): UsersRoutes {
        return retrofit.getclientWithToken(apiUrl, token).create(UsersRoutes::class.java)
    }

    fun getCategoryRoutes(token:String): CategoryRoutes {
        return retrofit.getclientWithToken(apiUrl, token).create(CategoryRoutes::class.java)
    }
    fun getProductsroutes(token:String): ProductsRoutes {
        return retrofit.getclientWithToken(apiUrl, token).create(ProductsRoutes::class.java)
    }
}