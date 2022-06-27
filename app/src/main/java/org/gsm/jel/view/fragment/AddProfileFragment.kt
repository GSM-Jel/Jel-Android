package org.gsm.jel.view.fragment

import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import org.gsm.jel.R
import org.gsm.jel.databinding.FragmentAddProfileBinding

class AddProfileFragment : Fragment()    {

    private val binding by lazy { FragmentAddProfileBinding.inflate(layoutInflater) }
    private lateinit var requestActivity : ActivityResultLauncher<Intent>
    private var imageUri : Uri? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = with(binding) {
        lifecycleOwner = viewLifecycleOwner
        fragment = this@AddProfileFragment
        resultActivity()

        return root
    }


    fun getProfile() = with(binding) {
        val intentImage = Intent(Intent.ACTION_PICK)
        intentImage.type = MediaStore.Images.Media.CONTENT_TYPE
        requestActivity.launch(intentImage)

    }

    private fun resultActivity(){
        requestActivity = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){ res ->
            if(res.resultCode == RESULT_OK){
                try {
                    imageUri = res.data?.data
                    binding.setProfile.setImageURI(imageUri)
                    binding.selectProfile.visibility = View.GONE
                }catch(e: Exception){
                    Log.d(TAG, "resultActivity: $e")
                }
            }
        }
    }


}