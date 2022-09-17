package com.optic.kev.activities.delivery.home



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.optic.kev.R

import com.optic.kev.activities.MainActivity
import com.optic.kev.fragment.clients.ClientCategoryFragment
import com.optic.kev.fragment.clients.ClientProfileFragment
import com.optic.kev.fragment.clients.ClientsOrdersFragment
import com.optic.kev.fragment.delivery.DeliveryOrdesrFragment
import com.optic.kev.fragment.restaurant.RestaurantOrdersFragment
import com.optic.kev.fragment.restaurant.RestaurantProductFragment
import com.optic.kev.models.User
import com.optic.kev.utils.SharedPref

class DeliveryHome : AppCompatActivity() {

    private val TAG = "DeliveryHome"
    var buttonLogout: Button? = null
    var sharedPref: SharedPref? = null
    var buttomNavigationView: BottomNavigationView ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_home)
        sharedPref = SharedPref(this)
        buttomNavigationView = findViewById(R.id.bottom_navigator)

        openFragments(DeliveryOrdesrFragment())
//bottom para cada rol
        buttomNavigationView?.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_orders -> {
                    openFragments(DeliveryOrdesrFragment())
                    true
                }
                R.id.item_profile -> {
                    openFragments(ClientProfileFragment())
                    true
                }


                else -> false
            }
        }
        getUserFromSession()
    }
    private fun openFragments(fragment: Fragment){

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    private fun logout() {
        sharedPref?.remove("user")
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    private fun getUserFromSession() {

        val gson = Gson()

        if (!sharedPref?.getData("user").isNullOrBlank()) {
            // SI EL USARIO EXISTE EN SESION
            val user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
            Log.d(TAG, "Usuario: $user")
        }

    }
}