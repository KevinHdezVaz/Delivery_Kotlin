package com.optic.kev.activities.client.products.detail

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.gson.Gson
import com.optic.kev.R
import com.optic.kev.databinding.ActivityClientProductsDetailsBinding
import com.optic.kev.databinding.ActivityClientsProductListBinding
import com.optic.kev.models.Product

class ClientProductsDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClientProductsDetailsBinding
    var product:Product?=null
var counter =1
    var productPrice =0.0

    var gson  = Gson()
     @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_products_details)

        binding = ActivityClientProductsDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        product = gson.fromJson(intent.getStringExtra("product"), Product::class.java)
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(product?.image1, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(product?.image2, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(product?.image3, ScaleTypes.CENTER_CROP))

        binding.imageSlider.setImageList(imageList)
        binding.tetxviewNameProduct.text = product?.name
        binding.tetxviewDescripcionProduct.text = product?.description
        binding.precio.text = "$"+product?.price.toString()

    binding.imageviewAdd.setOnClickListener{Additem()}
         binding.imageviewRemove.setOnClickListener{removeItem()}
    }

    private fun Additem(){
        counter++
        productPrice = product?.price!! * counter
        product?.quantity = counter
        binding.textviewContador.text = "${product?.quantity}"
        binding.precio.text ="$productPrice"
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