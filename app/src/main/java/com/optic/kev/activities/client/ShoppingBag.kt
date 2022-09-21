package com.optic.kev.activities.client

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.optic.kev.R
import com.optic.kev.activities.client.address.list.ClientAddressListActivity
import com.optic.kev.adapters.ShoppingCartAdapter
import com.optic.kev.databinding.ActivityClientProductsDetailsBinding
import com.optic.kev.databinding.ActivityShoppingBagBinding
import com.optic.kev.models.Product
import com.optic.kev.utils.SharedPref

class ShoppingBag : AppCompatActivity() {

    private lateinit var binding: ActivityShoppingBagBinding
    var toolbar:Toolbar?=null
    var adapter:ShoppingCartAdapter?= null
    var sharedpref:SharedPref?= null
    var gson= Gson()
    var selectProducts = ArrayList<Product>()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityShoppingBagBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedpref= SharedPref(this)
        binding.recyclerviewShopping.layoutManager = LinearLayoutManager(this)

        getProductsSharedPref()

        binding.btnAccept.setOnClickListener{
            gotoAddres()
        }
    }
    private fun gotoAddres(){
        val i = Intent(this, ClientAddressListActivity::class.java)
        startActivity(i)
    }

    fun settotal(total:Double){
        binding.textviewPreciototal.text = "${total}"
    }
    private fun getProductsSharedPref(){
        if(!sharedpref?.getData("order").isNullOrBlank()){
            val type = object : TypeToken<ArrayList<Product>>() {}.type

            selectProducts= gson.fromJson(sharedpref?.getData("order"),type)
        adapter = ShoppingCartAdapter(this,selectProducts)
            binding.recyclerviewShopping.adapter = adapter

    }
}}