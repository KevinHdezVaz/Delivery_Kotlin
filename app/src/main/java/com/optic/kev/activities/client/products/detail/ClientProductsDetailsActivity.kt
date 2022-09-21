package com.optic.kev.activities.client.products.detail

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.optic.kev.R
import com.optic.kev.activities.client.address.list.ClientAddressListActivity
import com.optic.kev.activities.client.home.ClientHomeActivity
import com.optic.kev.databinding.ActivityClientProductsDetailsBinding
import com.optic.kev.databinding.ActivityClientsProductListBinding
import com.optic.kev.models.Product
import com.optic.kev.utils.SharedPref

class ClientProductsDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClientProductsDetailsBinding
    var product:Product?=null
var counter =1
     var productPrice =0.0

    var sharedpref:SharedPref?= null
    var selectProducts = ArrayList<Product>()
    var gson  = Gson()
     @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_products_details)

        binding = ActivityClientProductsDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

         sharedpref = SharedPref(this)
        product = gson.fromJson(intent.getStringExtra("product"), Product::class.java)
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(product?.image1, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(product?.image2, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(product?.image3, ScaleTypes.CENTER_CROP))

        binding.imageSlider.setImageList(imageList)
        binding.tetxviewNameProduct.text = product?.name
        binding.tetxviewDescripcionProduct.text = product?.description
        binding.precio.text = "$"+product?.price.toString()
         getProductsSharedPref()
         binding.btnAgregarProduct.setOnClickListener{
             addtoBag()

         }
    binding.imageviewAdd.setOnClickListener{Additem()}
         binding.imageviewRemove.setOnClickListener{removeItem()}
    }

private fun addtoBag(){
val index = getIndexOf(product?.id!!)
     sharedpref?.save("order",selectProducts)

    if(index == -1){
        if(product?.quantity==0){
            product?.quantity = 1
        }
        selectProducts.add(product!!)
    }
    else{
        selectProducts[index].quantity= counter

    }
    Toast.makeText(this,"Producto agregado",Toast.LENGTH_LONG).show()
}

    @SuppressLint("SetTextI18n")
    private fun getProductsSharedPref(){
        if(!sharedpref?.getData("order").isNullOrBlank()){
            val type = object : TypeToken<ArrayList<Product>>() {}.type

            selectProducts= gson.fromJson(sharedpref?.getData("order"),type)

            val index = getIndexOf(product?.id!!)
            //Si el producto existe en sahredPref3

            //TODO: aqui se tiejne que dar 2 veces clic para que se agregue
            if (index != -1){
                //Desde la lista en sharedPref asignamos la cantidad a la BD
                product?.quantity = selectProducts[index].quantity
                //Desde la BD asignamos la cantidad al textview
               var cantidad = product?.quantity
                binding.textviewContador.text = "$cantidad"
                //Desde la BD asignamos la cantidad al contador
                counter = cantidad!!
                productPrice = product?.price!! * product?.quantity!!
                binding.precio.text = "${productPrice}"

            }

            for(p in selectProducts)
            {
                Log.d("VARTAG","Shared Pref: $p")
            }}
    }
    private fun Additem(){
        counter++
        productPrice = product?.price!! * counter
        product?.quantity = counter
        binding.textviewContador.text = "${product?.quantity}"
        binding.precio.text ="$productPrice"
    }

    //metodo para encontrar o comaprar si un producto ya existe en sharedpreferences y asi
    //poder editar la cantidad y no almacenar ambas ordenes
    private fun getIndexOf(idProduct:String): Int{
        var pos =  0
        for(p in selectProducts)
        {
            if(p.id == idProduct)
            {
                return pos
            }
            pos++
        }
return -1
    }

    private fun removeItem(){
        if(counter >1 ){
            counter--
            productPrice = product?.price!! * counter
            product?.quantity = counter
            binding.textviewContador.text = "${product?.quantity}"
            binding.precio.text ="$productPrice"
        }

    }
}