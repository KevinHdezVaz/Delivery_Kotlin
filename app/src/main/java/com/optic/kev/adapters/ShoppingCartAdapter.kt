package com.optic.kev.adapters

import android.annotation.SuppressLint
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
import com.optic.kev.activities.client.ShoppingBag
import com.optic.kev.activities.client.products.list.ClientsProductListActivity
import com.optic.kev.models.Category
import com.optic.kev.models.Product

import com.optic.kev.models.Rol
import com.optic.kev.utils.SharedPref

class ShoppingCartAdapter(val context: Activity, val products: ArrayList<Product>): RecyclerView.Adapter<ShoppingCartAdapter.ShoppingBagViewholder>() {


    val sharedPref = SharedPref(context)

    init {
        (context as ShoppingBag).settotal(getTotal())
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingBagViewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_bag_shop, parent, false)
        return ShoppingBagViewholder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ShoppingBagViewholder, position: Int) {

        val product = products[position] // CADA UNA DE LAS CATEGORIAS

        holder.textviewName.text = product.name
        holder.textviewCounter.text = "${product.quantity}"

        holder.textviewPrice.text = "${(product.quantity ?: 0) * product.price }$"
        Glide.with(context).load(product.image1).into(holder.imagaviewProduct)



         holder.imageviewAdd.setOnClickListener { Additem(product,holder) }
        holder.imageviewRemove.setOnClickListener { removeItem(product,holder) }
    holder.imageviewDelete.setOnClickListener{deleteiTem(position)}


    }
private fun getTotal():Double{

    var total = 0.0
    for(p in products)
    {
        total = total+  ((p.quantity ?: 0) * p.price)
    }
return total
}
    private fun deleteiTem(position: Int){

        products.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(products.size, products.size)
        sharedPref.save("order",products)
        (context as ShoppingBag).settotal(getTotal())

    }
    private fun Additem(product: Product, holder: ShoppingBagViewholder){

        val index = getIndexOf(product.id!!)
        product.quantity = product.quantity!! + 1
            products[index].quantity = product.quantity


        holder.textviewCounter.text = "${product.quantity}"
        holder.textviewPrice.text = "${(product.quantity ?: 0) * product.price}"

        sharedPref.save("order",products)

        (context as ShoppingBag).settotal(getTotal())

    }

    //metodo para encontrar o comaprar si un producto ya existe en sharedpreferences y asi
    //poder editar la cantidad y no almacenar ambas ordenes
    private fun getIndexOf(idProduct:String): Int{
        var pos =  0
        for(p in products)
        {
            if(p.id == idProduct)
            {
                return pos
            }
            pos++
        }
        return -1
    }

    private fun removeItem(product: Product, holder: ShoppingBagViewholder){


        if(product.quantity!! >1 ){
            val index = getIndexOf(product.id!!)
            product.quantity = product.quantity!! - 1
            products[index].quantity = product.quantity


            holder.textviewCounter.text = "${product.quantity}"
            holder.textviewPrice.text = "${(product.quantity ?: 0) * product.price}"

            sharedPref.save("order",products)
            (context as ShoppingBag).settotal(getTotal())

        }

    }
    private fun gotoProducts(category: Category) {
         val i = Intent(context, ClientsProductListActivity::class.java)
        i.putExtra("idCategory",category.id)
        context.startActivity(i)
     }

    class ShoppingBagViewholder(view: View): RecyclerView.ViewHolder(view) {

        val textviewName: TextView
        val textviewPrice: TextView
        val textviewCounter: TextView
        val imagaviewProduct: ImageView
        val imageviewAdd: ImageView
        val imageviewRemove: ImageView
        val imageviewDelete: ImageView

        init {
            textviewName = view.findViewById(R.id.tetviewname)
            textviewPrice = view.findViewById(R.id.textview_price)
            textviewCounter = view.findViewById(R.id.textview_contador)
            imagaviewProduct = view.findViewById(R.id.imageview_product)
            imageviewAdd = view.findViewById(R.id.imageview_add)

            imageviewRemove = view.findViewById(R.id.imageview_remove)
            imageviewDelete = view.findViewById(R.id.imageview_delete)

        }

    }

}