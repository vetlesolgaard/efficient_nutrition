package com.example.vetlesolgard.nutritrack.snapmeal

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.vetlesolgard.nutritrack.R
import com.example.vetlesolgard.nutritrack.utils.CameraControlActivity

class SnapMealFragment : Fragment() {

    private var imageBitmap: Bitmap? = null
    private var nutritionMap: MutableMap<String, List<String>> = mutableMapOf()

    private lateinit var imageClassifier: ImageClassifier
    private lateinit var mealTitleView: TextView
    private lateinit var mealImageView: ImageView
    private lateinit var nutritionTable: TableLayout

    private lateinit var caloriesTextView: TextView
    private lateinit var proteinsTextView: TextView
    private lateinit var fatsTextView: TextView
    private lateinit var carbohydratesTextView: TextView

    private var caloriesNumber: Float = 0f
    private var proteinsNumber: Float = 0f
    private var fatsNumber: Float = 0f
    private var carbohydratesNumber: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nutritionMap["spaghetti bolognese"] = listOf("667", "35", "22", "84")
        nutritionMap["french fries"] = listOf("480", "5.3", "23", "64")
        nutritionMap["hamburger"] = listOf("540", "34", "27", "40")
        nutritionMap["hot dog"] = listOf("155", "5.6", "14", "1.3")
        nutritionMap["caesar salad"] = listOf("481", "10", "40", "23")

        imageClassifier = ImageClassifier(activity!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_snapmeal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        caloriesTextView = view.findViewById(R.id.calories)
        proteinsTextView = view.findViewById(R.id.proteins)
        fatsTextView = view.findViewById(R.id.fats)
        carbohydratesTextView = view.findViewById(R.id.carbohydrates)

        mealTitleView = view.findViewById(R.id.snap_meal_title)
        mealImageView = view.findViewById(R.id.meal_image_view)

        val snapMealButton = view.findViewById<Button>(R.id.snap_meal_button)
        snapMealButton.setOnClickListener {
            val intent = Intent(context, CameraControlActivity::class.java)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }

        val addButton = view.findViewById<Button>(R.id.add_button)
        addButton.setOnClickListener {
            val intentResult = Intent()
            intentResult.putExtra("", 0)
            activity!!.setResult(0, intentResult)
            activity!!.onBackPressed()
        }

        val input = EditText(context)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp
        val dialog = AlertDialog.Builder(context)
            .setTitle("Type in weight of meal")
            .setView(input)
            .setPositiveButton("Submit") { dialog, _ ->
                multiplyNutritionValues(input)
            }
            .create()

        val typeInWeightButton = view.findViewById<Button>(R.id.type_in_weight)
        typeInWeightButton.setOnClickListener {
            dialog.show()
        }

        nutritionTable = view.findViewById(R.id.nutrition_table)
    }

    override fun onResume() {
        super.onResume()
        if (imageBitmap != null) {
            mealImageView.setImageBitmap(imageBitmap)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == CameraControlActivity.REQUEST_IMAGE_CAPTURE) {
            imageBitmap = data.extras.get("bitmap") as Bitmap
            val label = imageClassifier.classifyFrame(imageBitmap as Bitmap)

            mealTitleView.text = label.capitalize()
            setCurrentNutritionTable(label)
        }
    }

    private fun multiplyNutritionValues(input: EditText) {
        val numberToMultiply = input.text.toString().toFloat() / ONE_SERVING_SPAGHETTI
        val calories = caloriesNumber * numberToMultiply
        val proteins= proteinsNumber * numberToMultiply
        val fats = fatsNumber * numberToMultiply
        val carbohydrates = carbohydratesNumber * numberToMultiply

        setTextInNutritionTable(listOf(calories, proteins, fats, carbohydrates))
    }

    /**
     * Sets nutritiontable value for current meal
     * Values should be postfixed with g or (kJ/kcal).
     * Need to make sure nutritionValues list are the
     * same length as the rows.
     */
    private fun setCurrentNutritionTable(label: String) {
        val currentNutrition = nutritionMap[label]!!
        caloriesNumber = currentNutrition[0].toFloat()
        proteinsNumber = currentNutrition[1].toFloat()
        fatsNumber = currentNutrition[2].toFloat()
        carbohydratesNumber = currentNutrition[3].toFloat()
        setTextInNutritionTable(listOf(caloriesNumber, proteinsNumber, fatsNumber, carbohydratesNumber))
    }

    private fun setTextInNutritionTable(nutritionList: List<Float>) {
        val nutritionStrings = nutritionList.map {
            "%.1f".format(it)
        }
        caloriesTextView.text = "${nutritionStrings[0]} kcal"
        proteinsTextView.text = "${nutritionStrings[1]} (g)"
        fatsTextView.text = "${nutritionStrings[2]} (g)"
        carbohydratesTextView.text = "${nutritionStrings[3]} (g)"
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        const val ONE_SERVING_SPAGHETTI = 660
    }
}