package com.optic.kev.fragment.restaurant

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.optic.kev.R
import com.optic.kev.models.Category
import com.optic.kev.models.ResponseHttp
import com.optic.kev.models.User
import com.optic.kev.providers.CategoryProvider
import com.optic.kev.utils.SharedPref
import com.tommasoberlose.progressdialog.ProgressDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RestaurantCategoryFragment : Fragment() {

    var myview : View? = null
    var imageViewCategory: ImageView? = null
            var edittextCategory : EditText?= null
    var buttonCategory: Button?= null
    private var imageFile: File? = null
    var categoriesProvider : CategoryProvider?=null

    var sharedPref: SharedPref?=  null
    var toolbar: Toolbar?= null
    var user: User?= null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myview= inflater.inflate(R.layout.fragment_restaurant_category, container, false)
        imageViewCategory = myview?.findViewById(R.id.imageview_category)
        edittextCategory = myview?.findViewById(R.id.edditext_categori)
        buttonCategory = myview?.findViewById(R.id.btn_crear_categoria)


sharedPref = SharedPref(requireActivity())

        getUserFromSession()
        categoriesProvider = CategoryProvider(user?.sessionToken!!)
        imageViewCategory?.setOnClickListener{
            selectImage()
        }
        buttonCategory?.setOnClickListener{
            createCategory()
        }
        return myview
    }

    private fun createCategory() {
         val name = edittextCategory?.text.toString()
        ProgressDialogFragment.showProgressBar(requireActivity())

        if(imageFile != null){

            val category = Category(name = name)
            categoriesProvider?.create(imageFile!!, category!!)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    ProgressDialogFragment.hideProgressBar(requireActivity())

                    Log.d("TAG", "RESPONSE: $response")
                    Log.d("TAG", "BODY: ${response.body()}")
                    Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_LONG).show()

        if(response.body()?.isSuccess== true){
            limpiarForm()
        }

                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    ProgressDialogFragment.hideProgressBar(requireActivity())
                    Log.d("TAG", "Error: ${t.message}")
                }

            })

        }else
        {
            Toast.makeText(requireContext(), "Selecciona una imagen para agregar categoria", Toast.LENGTH_LONG).show()

        }
    }

    private fun limpiarForm() {
        edittextCategory?.setText("")
        imageFile= null
        imageViewCategory?.setImageResource(R.drawable.ic_baseline_image_24)
    }

    private fun getUserFromSession() {

        val gson = Gson()

        if (!sharedPref?.getData("user").isNullOrBlank()) {
            // SI EL USARIO EXISTE EN SESION
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }

    }
    private val startImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data
                imageFile = File(fileUri?.path) // EL ARCHIVO QUE VAMOS A GUARDAR COMO IMAGEN EN EL SERVIDOR
                imageViewCategory?.setImageURI(fileUri)
            }
            else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(requireContext(), "Tarea se cancelo", Toast.LENGTH_LONG).show()
            }

        }

    private fun selectImage() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .createIntent { intent ->
                startImageForResult.launch(intent)
            }
    }
}