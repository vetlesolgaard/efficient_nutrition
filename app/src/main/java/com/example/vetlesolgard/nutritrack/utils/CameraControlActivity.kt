package com.example.vetlesolgard.nutritrack.utils

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.widget.ImageView

class CameraControlActivity: Activity() {

    override fun onStart() {
        super.onStart()
        dispatchTakePictureIntent()
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data.extras.get("data") as Bitmap
            val scaledBitMap = Bitmap.createScaledBitmap(imageBitmap, 224, 224, false)
            val intentResult = Intent()
            intentResult.putExtra("bitmap", scaledBitMap)
            setResult(REQUEST_IMAGE_CAPTURE, intentResult)
            finish()
        }
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }
}