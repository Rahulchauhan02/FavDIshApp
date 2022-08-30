package com.mylearninghub.favdish.view.activity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.mylearninghub.favdish.R
import com.mylearninghub.favdish.databinding.ActivityAddUpdateDishBinding
import com.mylearninghub.favdish.databinding.AddImageSelectDialogBinding
import com.mylearninghub.favdish.utils.Utility
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


class AddUpdateDishActivity : AppCompatActivity() , View.OnClickListener, EasyPermissions.PermissionCallbacks{
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
            cameraTask(dialog)
        }

        dialogBinding.gallery.setOnClickListener {
            galleryTask(dialog)
            }
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


    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size)

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms))
        {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    private fun cameraTask(dialog:Dialog){
        if(hasCameraPermission()){
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraActivityLauncher.launch(intent)
            dialog.dismiss()
        }
        else
        {
            EasyPermissions.requestPermissions(this,"This app needs access to your " +
                    "camera so you can take pictures",Utility.CAMERA_CODE,Manifest.permission.CAMERA)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
    private fun galleryTask(dialog:Dialog){
        if(hasExternalStoragePermission()){
            val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryActivityLauncher.launch(intent)
            dialog.dismiss()
        }
        else{
            EasyPermissions.requestPermissions(this,"This app needs access to your" +
                    " Storage so you can select pictures",Utility.GALLERY_CODE,Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }



    private fun hasCameraPermission():Boolean
    {
        return EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)
    }

    private fun hasExternalStoragePermission():Boolean{
        return EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)
    }
}