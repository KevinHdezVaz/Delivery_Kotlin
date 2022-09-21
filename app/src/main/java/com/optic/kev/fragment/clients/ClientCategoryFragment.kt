package com.optic.kev.fragment.clients

 import android.content.Intent
 import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.optic.kev.R
import com.optic.kev.activities.MainActivity
import com.optic.kev.activities.client.ShoppingBag
 import com.optic.kev.activities.client.home.ClientHomeActivity
 import com.optic.kev.adapters.CategorysAdapter
import com.optic.kev.models.Category
import com.optic.kev.models.User
import com.optic.kev.providers.CategoryProvider
import com.optic.kev.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ClientCategoryFragment : Fragment() {

    val TAG = "CategoriesFragment"
    var myView: View? = null
    var recyclerViewCategories: RecyclerView? = null
    var categoriesProvider: CategoryProvider? = null
    var adapter: CategorysAdapter? = null
    var user: User? = null
    var sharedPref: SharedPref? = null
    var categories = ArrayList<Category>()

    var toolbar: Toolbar?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_client_category, container, false)

        setHasOptionsMenu(true)

        recyclerViewCategories = myView?.findViewById(R.id.recyclerview_category)
        recyclerViewCategories?.layoutManager = LinearLayoutManager(requireContext()) // ELEMENTOS SE MOSTRARAN DE MANERA VERTICAL
        toolbar = myView?.findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(requireContext(),R.color.white))

        toolbar?.title=" Categorias"
         (getActivity() as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        sharedPref = SharedPref(requireActivity())

        getUserFromSession()

        //TODO: quitar esto despues
         categoriesProvider = CategoryProvider(user?.sessionToken!!)

        getCategories()

        return myView
    }

    private fun getCategories() {
        categoriesProvider?.getAll()?.enqueue(object: Callback<ArrayList<Category>> {
            override fun onResponse(call: Call<ArrayList<Category>>, response: Response<ArrayList<Category>>
            ) {

                if (response.body() != null) {

                    categories = response.body()!!
                    adapter = CategorysAdapter(requireActivity(), categories)
                    recyclerViewCategories?.adapter = adapter
                }

            }

            override fun onFailure(call: Call<ArrayList<Category>>, t: Throwable) {
                Log.d(TAG, "Error: ${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.item_shopping_bag){
            gotoShopBag()

        }
        return super.onOptionsItemSelected(item)
    }
private fun gotoShopBag(){


    val ix = Intent(requireContext(), ShoppingBag::class.java)
     startActivity(ix)
}
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_shopping_bag, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}