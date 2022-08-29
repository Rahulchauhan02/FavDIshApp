package com.mylearninghub.favdish.view.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mylearninghub.favdish.R
import com.mylearninghub.favdish.databinding.ActivityAddUpdateDishBinding
import com.mylearninghub.favdish.databinding.AddImageSelectDialogBinding


class AddUpdateDishActivity : AppCompatActivity() , View.OnClickListener{
    private lateinit var addUpdateDishBinding: ActivityAddUpdateDishBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        addUpdateDishBinding = ActivityAddUpdateDishBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
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
        addUpdateDishBinding.openDialogButton.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
     if(view!=null)
     {
         when(view.id)
         {
             R.id.openDialogButton->{  showCustomDialog() }
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
            dialog.dismiss()
            if(Build.VERSION.SDK_INT>=28)
            {
                Dexter.withContext(this)
                    .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                    ).withListener(object:MultiplePermissionsListener{
                        override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {

                            if(p0!!.areAllPermissionsGranted())
                            {
                               val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                               cameraActivityLauncher.launch(intent)
                                dialog.dismiss()
                            }
                            else
                            {
                                showRationalDialog()
                            }
                        }
                        override fun onPermissionRationaleShouldBeShown(
                            p0: MutableList<PermissionRequest>?,
                            p1: PermissionToken?
                        ) {
                            p1?.continuePermissionRequest()
                            showRationalDialog()
                        }
                    }).onSameThread().check()
            }
            else
            {
                Dexter.withContext(this)
                    .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                    ).withListener(object:MultiplePermissionsListener{
                        override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {

                            if(p0!!.areAllPermissionsGranted())
                            {
                                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                cameraActivityLauncher.launch(intent)
                                dialog.dismiss()
                            }
                            else
                            {
                                showRationalDialog()
                            }
                        }
                        override fun onPermissionRationaleShouldBeShown(
                            p0: MutableList<PermissionRequest>?,
                            p1: PermissionToken?
                        ) {
                            p1?.continuePermissionRequest()
                            showRationalDialog()
                        }
                    }).onSameThread().check()
            }
        }

        dialogBinding.gallery.setOnClickListener {
            if(Build.VERSION.SDK_INT>=28)
            {
                Dexter.withContext(this)
                    .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                    ).withListener(object:MultiplePermissionsListener{
                        override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {

                            if(p0!!.areAllPermissionsGranted())
                            {
                                val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                                galleryActivityLauncher.launch(intent)
                                dialog.dismiss()
                            }
                            else
                            {
                                showRationalDialog()
                            }
                        }
                        override fun onPermissionRationaleShouldBeShown(
                            p0: MutableList<PermissionRequest>?,
                            p1: PermissionToken?
                        ) {
                            p1?.continuePermissionRequest()
                            showRationalDialog()
                        }
                    }).onSameThread().check()
            }
            else
            {
                Dexter.withContext(this)
                    .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                    ).withListener(object:MultiplePermissionsListener{
                        override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {

                            if(p0!!.areAllPermissionsGranted())
                            {
                                val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                                galleryActivityLauncher.launch(intent)
                                dialog.dismiss()
                            }
                            else
                            {
                                showRationalDialog()
                            }
                        }
                        override fun onPermissionRationaleShouldBeShown(
                            p0: MutableList<PermissionRequest>?,
                            p1: PermissionToken?
                        ) {
                            p1?.continuePermissionRequest()
                            showRationalDialog()
                        }
                    }).onSameThread().check()
            }
            dialog.dismiss()
        }
    }


   private fun showRationalDialog(){
            AlertDialog.Builder(this)
           .setMessage("You have denied the permission to use this feature you have to give permission")
           .setPositiveButton("Go to Settings"){_,_ ->
               run {
                   try {
                       val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package",packageName,null)
                       intent.data= uri
                       startActivity(intent)
                   } catch (ex: Exception) {
                       ex.printStackTrace()
                   }
               }
           }
           .setNegativeButton("Cancel"){alertDialog,_-> run { alertDialog.dismiss() } }
           .show()
   }

    var  cameraActivityLauncher =registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
        if(it.resultCode== Activity.RESULT_OK)
        {
            it.data?.let { result->addUpdateDishBinding.addDishImage.setImageBitmap(result.extras?.get("data") as Bitmap) }
        }
    }

    var galleryActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
        if(it.resultCode== Activity.RESULT_OK)
        {
            it.data?.let { result->addUpdateDishBinding.addDishImage.setImageURI( result.data) }
        }
    }
}