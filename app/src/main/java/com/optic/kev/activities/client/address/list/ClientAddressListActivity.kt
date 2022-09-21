package com.optic.kev.activities.client.address.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.optic.kev.R
import com.optic.kev.activities.client.address.create.ClientAddresCreate
import com.optic.kev.adapters.AdrressAdapter
import com.optic.kev.databinding.ActivityClientAddressListBinding
 import com.optic.kev.models.Address
import com.optic.kev.models.Product
import com.optic.kev.models.ResponseHttp
import com.optic.kev.models.User
import com.optic.kev.providers.AddressProvider
import com.optic.kev.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientAddressListActivity : AppCompatActivity() {

    var fabCreateAddress: FloatingActionButton? = null

    var recyclerViewAddress: RecyclerView? = null
    var buttonNext: Button? = null
    var adapter: AdrressAdapter? = null
    var addressProvider: AddressProvider? = null
   // var ordersProvider: OrdersProvider? = null
    var sharedPref: SharedPref? = null
    var user: User? = null

    var address = ArrayList<Address>()

    val gson = Gson()

    var selectedProducts = ArrayList<Product>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_address_list)

        sharedPref = SharedPref(this)

        getProductsFromSharedPref()

        fabCreateAddress = findViewById(R.id.fab_address_create)

         buttonNext = findViewById(R.id.btn_next)
        recyclerViewAddress = findViewById(R.id.recyclerview_address)

        recyclerViewAddress?.layoutManager = LinearLayoutManager(this)



        getUserFromSession()

        addressProvider = AddressProvider(user?.sessionToken!!)
       // ordersProvider = OrdersProvider(user?.sessionToken!!)

        fabCreateAddress?.setOnClickListener { goToAddressCreate() }

        getAddress()

        buttonNext?.setOnClickListener { getAddressFromSession() }
    }

    private fun getProductsFromSharedPref() {

        if (!sharedPref?.getData("order").isNullOrBlank()) { // EXISTE UNA ORDEN EN SHARED PREF
            val type = object: TypeToken<ArrayList<Product>>() {}.type
            selectedProducts = gson.fromJson(sharedPref?.getData("order"), type)

        }

    }
/*
    private fun createOrder(idAddress: String) {
        val order = Order(
            products = selectedProducts,
            idClient = user?.id!!,
            idAddress = idAddress
        )

        ordersProvider?.create(order)?.enqueue(object: Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {

                if (response.body() != null) {
                    Toast.makeText(this@ClientAddressListActivity, "${response.body()?.message}", Toast.LENGTH_LONG).show()
                }
                else {
                    Toast.makeText(this@ClientAddressListActivity, "Ocurrio un error en la peticion", Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Toast.makeText(this@ClientAddressListActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })


    }*/

    private fun getAddressFromSession() {

        if (!sharedPref?.getData("address").isNullOrBlank()) {
            val a = gson.fromJson(sharedPref?.getData("address"), Address::class.java) // SI EXISTE
         //   createOrder(a.id!!)
//            goToPaymentsForm()
        }
        else {
            Toast.makeText(this, "Selecciona una direccion para continuar", Toast.LENGTH_LONG).show()
        }

    }
/*
    private fun goToPaymentsForm() {
        val i = Intent(this, ClientPaymentFormActivity::class.java)
        startActivity(i)
    }


 */
    fun resetValue(position: Int) {
        val viewHolder = recyclerViewAddress?.findViewHolderForAdapterPosition(position) // UNA DIRECCION
        val view = viewHolder?.itemView
        val imageViewCheck = view?.findViewById<ImageView>(R.id.imageview_check)
        imageViewCheck?.visibility = View.GONE
    }

    private fun getAddress() {
        addressProvider?.getAddress(user?.id!!)?.enqueue(object: Callback<ArrayList<Address>> {
            override fun onResponse(call: Call<ArrayList<Address>>, response: Response<ArrayList<Address>>) {

                if (response.body() != null) {
                    address = response.body()!!
                    adapter = AdrressAdapter(this@ClientAddressListActivity, address)
                    recyclerViewAddress?.adapter = adapter

                }

            }

            override fun onFailure(call: Call<ArrayList<Address>>, t: Throwable) {
                Toast.makeText(this@ClientAddressListActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
             }

        })
    }

    private fun getUserFromSession() {

        val gson = Gson()

        if (!sharedPref?.getData("user").isNullOrBlank()) {
            // SI EL USARIO EXISTE EN SESION
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }

    }

    private fun goToAddressCreate() {
        val i = Intent(this, ClientAddresCreate::class.java)
        startActivity(i)
    }
}