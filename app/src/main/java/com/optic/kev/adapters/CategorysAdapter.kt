package com.optic.kev.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.optic.kev.activities.client.home.ClientHomeActivity
import com.optic.kev.activities.delivery.home.DeliveryHome
import com.optic.kev.activities.restaurente.home.RestaurantHome
import com.optic.kev.R
import com.optic.kev.models.Category

import com.optic.kev.models.Rol
import com.optic.kev.utils.SharedPref

class CategorysAdapter(val context: Activity, val categories: ArrayList<Category>): RecyclerView.Adapter<CategorysAdapter.CategoriesViewHolder>() {


    val sharedPref = SharedPref(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_category, parent, false)
        return CategoriesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {

        val category = categories[position] // CADA UNA DE LAS CATEGORIAS

        holder.textViewCategory.text = category.name
        Glide.with(context).load(category.image).into(holder.imageViewCategory)


//        holder.itemView.setOnClickListener { goToRol(rol) }
    }

//    private fun goToRol(rol: Rol) {
//        val i = Intent(context, RestaurantHomeActivity::class.java)
//        context.startActivity(i)
//    }

    class CategoriesViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textViewCategory: TextView
        val imageViewCategory: ImageView

        init {
            textViewCategory = view.findViewById(R.id.textview_category)
            imageViewCategory = view.findViewById(R.id.imageview_category)
        }

    }

}