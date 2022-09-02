package com.mylearninghub.favdish.view.activity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mylearninghub.favdish.R
import com.mylearninghub.favdish.databinding.ActivityAddUpdateDishBinding
import com.mylearninghub.favdish.databinding.AddImageSelectDialogBinding
import com.mylearninghub.favdish.utils.Utility
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.FileOutputStream
import java.util.*


class AddUpdateDishActivity : AppCompatActivity() , View.OnClickListener, EasyPermissions.PermissionCallbacks{
    private lateinit var addUpdateDishBinding: ActivityAddUpdateDishBinding
    private var imagePath=""
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
             R.id.openDialogButton->{  showCustomDialog()
                                        return }

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

    private var  cameraActivityLauncher =registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
        if(it.resultCode== Activity.RESULT_OK)
        {
            it.data?.let { result-> val thumbnail =result.extras?.get("data") as Bitmap
                Glide.with(this)
                    .load(thumbnail)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(addUpdateDishBinding.addDishImage)
                imagePath=saveImageToInternalStorage(thumbnail)
                Log.i("IMAGEPATH",imagePath)
            }
        }
    }

    private var galleryActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
        if(it.resultCode== Activity.RESULT_OK)
        {
            it.data?.let { result-> val data=result.data
                 Glide.with(this)
                .load(data)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(object :RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                       Log.e(TAG,e?.message.toString())
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                      resource?.let {
                      imagePath=   saveImageToInternalStorage(resource.toBitmap())
                          Log.i("IMAGEPATH",imagePath)
                      }
                        return false
                    }
                })
                .into(addUpdateDishBinding.addDishImage)
            }
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

    private  fun saveImageToInternalStorage(bitmap: Bitmap):String{
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(Utility.IMAGE_DIRECTORY,Context.MODE_PRIVATE)
        file = File(file ,"${UUID.randomUUID()}.jpeg")
        try
        {
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            stream.flush()
            stream.close()
        }
        catch(e:Exception)
        {
            e.printStackTrace()
        }
        return file.absolutePath
    }
}