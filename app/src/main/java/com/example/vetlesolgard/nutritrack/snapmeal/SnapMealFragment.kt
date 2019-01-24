package com.example.vetlesolgard.nutritrack.snapmeal

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.RectF
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.vetlesolgard.nutritrack.R
import com.example.vetlesolgard.nutritrack.utils.CameraControlActivity
import com.example.vetlesolgard.nutritrack.utils.CameraControlActivity.Companion.REQUEST_IMAGE_CAPTURE

class SnapMealFragment : Fragment() {

    private var imageBitmap: Bitmap? = null

    lateinit var imageClassifier: ImageClassifier
    lateinit var mealTitleView: TextView
    lateinit var mealImageView: ImageView
    lateinit var nutritionTable: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageClassifier = ImageClassifier(activity!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_snapmeal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealTitleView = view.findViewById(R.id.snap_meal_title)
        mealImageView = view.findViewById(R.id.meal_image_view)

        val snapMealButton = view.findViewById<Button>(R.id.snap_meal_button)
        snapMealButton.setOnClickListener {
            val intent = Intent(context, CameraControlActivity::class.java)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }

        val addButton = view.findViewById<Button>(R.id.add_button)
        addButton.setOnClickListener {
            activity!!.onBackPressed()
        }

        nutritionTable = view.findViewById(R.id.nutrition_table)
    }

    override fun onResume() {
        super.onResume()
        if (imageBitmap != null) {
            initializeNutritionTable(NUTRITION_LIST)
            mealImageView.setImageBitmap(imageBitmap)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == CameraControlActivity.REQUEST_IMAGE_CAPTURE) {
            imageBitmap = data.extras.get("bitmap") as Bitmap
            val label = imageClassifier.classifyFrame(imageBitmap as Bitmap)
            mealTitleView.text = label
        }
    }

    /**
     * Sets nutritiontable value for current meal
     * Values should be postfixed with g or (kJ/kcal).
     * Need to make sure nutritionValues list are the
     * same length as the rows.
     */
    private fun initializeNutritionTable(nutritionValues: List<String>) {
        for (i in 0 until nutritionTable.childCount) {
            val row = nutritionTable.getChildAt(i) as TableRow
            for (j in 0 until row.childCount) {
                if (j == 1) {
                    val text = row.getChildAt(j) as TextView
                    if (i == 0) {
                        text.setText("${nutritionValues[i]} kJ/kcal")
                    } else {
                        text.setText("${nutritionValues[i]} g")
                    }
                }
            }
        }
    }

    /**
     * Configures the necessary [android.graphics.Matrix] transformation to `textureView`. This
     * method should be called after the camera preview size is determined in setUpCameraOutputs and
     * also the size of `textureView` is fixed.
     *
     * @param viewWidth The width of `textureView`
     * @param viewHeight The height of `textureView`
     */
//    private fun configureTransform(viewWidth: Int, viewHeight: Int) {
//        val activity = activity
//        if (null == textureView || null == previewSize || null == activity) {
//            return
//        }
//        val rotation = activity.windowManager.defaultDisplay.rotation
//        val matrix = Matrix()
//        val viewRect = RectF(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat())
//        val bufferRect = RectF(0f, 0f, previewSize.getHeight().toFloat(), previewSize.getWidth().toFloat())
//        val centerX = viewRect.centerX()
//        val centerY = viewRect.centerY()
//        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
//            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY())
//            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL)
//            val scale = Math.max(
//                viewHeight.toFloat() / previewSize.getHeight(),
//                viewWidth.toFloat() / previewSize.getWidth()
//            )
//            matrix.postScale(scale, scale, centerX, centerY)
//            matrix.postRotate((90 * (rotation - 2)).toFloat(), centerX, centerY)
//        } else if (Surface.ROTATION_180 == rotation) {
//            matrix.postRotate(180f, centerX, centerY)
//        }
//        textureView.setTransform(matrix)
//    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        val NUTRITION_LIST = listOf("346", "20.3", "3.5", "2.6", "13.6")
    }
}