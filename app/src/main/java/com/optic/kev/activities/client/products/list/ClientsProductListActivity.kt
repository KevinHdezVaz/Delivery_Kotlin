package com.optic.kev.activities.client.products.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.optic.kev.R
import com.optic.kev.adapters.ProductsAdapter
import com.optic.kev.models.Category
import com.optic.kev.models.Product
import com.optic.kev.models.User
import com.optic.kev.providers.ProductsProvider
import com.optic.kev.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientsProductListActivity : AppCompatActivity() {


    var recyclerView: RecyclerView?= null
    var adapter : ProductsAdapter?= null
    var user: User?= null
    var productsProvider:ProductsProvider?= null
    var products: ArrayList <Product> = ArrayList()
var idCategory : String?=null
var sharedPref: SharedPref?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clients_product_list)
sharedPref = SharedPref(this)
    recyclerView = findViewById(R.id.recyclerview_products)
        recyclerView?.layoutManager = GridLayoutManager(this,2)
        getUserFromSession()

        idCategory = intent.getStringExtra("idCategory")
productsProvider = ProductsProvider(user?.sessionToken!!)
         getProducts()

    }

    private fun getProducts(){
        productsProvider?.findbyCategory(idCategory!!)?.enqueue(object : Callback<ArrayList<Product>>{
            override fun onResponse(
                call: Call<ArrayList<Product>>,
                response: Response<ArrayList<Product>>
            ) {
if(response.body() != null){
    products = response.body()!!
    adapter = ProductsAdapter(this@ClientsProductListActivity, products)
    recyclerView?.adapter = adapter
}

            }

            override fun onFailure(call: Call<ArrayList<Product>>, t: Throwable) {
                Toast.makeText(this@ClientsProductListActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()


            }

        })
    }  private fun getUserFromSession() {

        val sharedPref = SharedPref(this)
        val gson = Gson()

        if (!sharedPref.getData("user").isNullOrBlank()) {
            // SI EL USARIO EXISTE EN SESION
            user = gson.fromJson(sharedPref.getData("user"), User::class.java)
        }

    }
}