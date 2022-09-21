package com.optic.kev.providers

import com.optic.kev.api.ApiRoutes
import com.optic.kev.models.Address
import com.optic.kev.models.Category
import com.optic.kev.models.Product
import com.optic.kev.models.ResponseHttp
import com.optic.kev.routes.AddressRoutes
import com.optic.kev.routes.CategoryRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class AddressProvider (  val token:String ){



    private var addressRoutes: AddressRoutes? = null

    init {
        val api = ApiRoutes()
        addressRoutes = api.getAddressRoutes(token)
    }

    fun getAddress(idUser: String): Call<ArrayList<Address>>? {
        return addressRoutes?.getAddress(idUser, token)
    }

    fun create(address: Address): Call<ResponseHttp>? {
        return addressRoutes?.create(address, token)
    }

}