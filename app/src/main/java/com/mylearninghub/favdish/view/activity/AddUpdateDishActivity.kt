package com.mylearninghub.favdish.view.activity

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.mylearninghub.favdish.R
import com.mylearninghub.favdish.databinding.ActivityAddUpdateDishBinding
import com.mylearninghub.favdish.databinding.AddImageSelectDialogBinding

class AddUpdateDishActivity : AppCompatActivity() , View.OnClickListener{
    private lateinit var addUpdateDishBinding: ActivityAddUpdateDishBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addUpdateDishBinding = ActivityAddUpdateDishBinding.inflate(layoutInflater)
        setContentView(addUpdateDishBinding.root)
        setUpActionBar()
    }


    private fun setUpActionBar()
    {
        addUpdateDishBinding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        addUpdateDishBinding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }


        addUpdateDishBinding.addDishImage.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
     if(view!=null)
     {
         when(view.id)
         {
             R.id.addDishImage->{  showCustomDialog() }
         }
     }
    }



    private fun showCustomDialog()
    {
        val dialog = Dialog(this)
        val dialogBinding = AddImageSelectDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.show()
        dialogBinding.camera.setOnClickListener{
            Toast.makeText(this,"Camera is clicked",Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialogBinding.gallery.setOnClickListener {
            Toast.makeText(this,"Gallery is clicked",Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
    }
}