package com.optic.kev.activities.client.address.create

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.optic.kev.R
import com.optic.kev.activities.client.address.list.ClientAddressListActivity
import com.optic.kev.activities.client.address.map.ClientMapActivity
import com.optic.kev.databinding.ActivityClientAddresCreateBinding
import com.optic.kev.databinding.ActivityClientMapBinding
import com.optic.kev.models.Address
import com.optic.kev.models.ResponseHttp
import com.optic.kev.models.User
import com.optic.kev.providers.AddressProvider
import com.optic.kev.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientAddresCreate : AppCompatActivity() {

    val TAG = "ClientAddressCreate"

     var editTextRefPoint: EditText? = null
    var editTextAddress: EditText? = null
    var editTextNeighborhood: EditText? = null
    var buttonCreateAddress: Button? = null

    var addressLat = 0.0
    var addressLng = 0.0

    var addressProvider: AddressProvider? = null
    var sharedPref: SharedPref? = null
    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_addres_create)

        sharedPref = SharedPref(this)

        getUserFromSession()
        addressProvider = AddressProvider(user?.sessionToken!!)

         editTextRefPoint = findViewById(R.id.edittext_ref_point)
        editTextAddress = findViewById(R.id.edittext_address)
        editTextNeighborhood = findViewById(R.id.edittext_neighborhood)
        buttonCreateAddress = findViewById(R.id.btn_create_address)



        editTextRefPoint?.setOnClickListener { goToAddressMap() }
        buttonCreateAddress?.setOnClickListener { createAddress() }

    }

    private fun getUserFromSession() {

        val gson = Gson()

        if (!sharedPref?.getData("user").isNullOrBlank()) {
            // SI EL USARIO EXISTE EN SESION
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }

    }

    private fun createAddress() {

        val address = editTextAddress?.text.toString()
        val neighborhood = editTextNeighborhood?.text.toString()

        if (isValidForm(address, neighborhood)) {
            // Lanzar la peticion

            val addressModel = Address(
                address = address,
                neighborhood = neighborhood,
                idUser = user?.id!!,
                lat = addressLat,
                lng = addressLng
            )

            addressProvider?.create(addressModel)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {

                    if (response.body() != null) {
                        Toast.makeText(this@ClientAddresCreate, response.body()?.message, Toast.LENGTH_LONG).show()
                        goToAddressList()
                    }
                    else {
                        Toast.makeText(this@ClientAddresCreate, "Ocurrio un error en la peticion", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Toast.makeText(this@ClientAddresCreate, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }

            })

        }
    }

    private fun goToAddressList() {
        val i = Intent(this, ClientAddressListActivity::class.java)
        startActivity(i)
    }

    private fun isValidForm(address: String, neighborhood: String): Boolean {

        if (address.isNullOrBlank()) {
            Toast.makeText(this, "Ingresa la direccion", Toast.LENGTH_SHORT).show()
            return false
        }
        if (neighborhood.isNullOrBlank()) {
            Toast.makeText(this, "Ingresa el barrio o residencia", Toast.LENGTH_SHORT).show()
            return false
        }
        if (addressLat == 0.0) {
            Toast.makeText(this, "Selecciona el punto de referencia", Toast.LENGTH_SHORT).show()
            return false
        }
        if (addressLng == 0.0) {
            Toast.makeText(this, "Selecciona el punto de referencia", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val city = data?.getStringExtra("city")
            val address = data?.getStringExtra("address")
            val country = data?.getStringExtra("country")
            addressLat = data?.getDoubleExtra("lat", 0.0)!!
            addressLng = data?.getDoubleExtra("lng", 0.0)!!

            editTextRefPoint?.setText("$address $city")

            Log.d(TAG, "City: $city")
            Log.d(TAG, "Address: $address")
            Log.d(TAG, "Country: $country")
            Log.d(TAG, "Lat: $addressLat")
            Log.d(TAG, "Lng: $addressLng")
        }

    }

    private fun goToAddressMap() {
        val i = Intent(this, ClientMapActivity::class.java)
        resultLauncher.launch(i)
    }
}