package com.example.vetlesolgard.nutritrack

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.example.vetlesolgard.nutritrack.recommendations.RecommendationActivity
import com.example.vetlesolgard.nutritrack.snapmeal.SnapMealActivity

class MainMenuFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(com.example.vetlesolgard.nutritrack.R.layout.fragment_mainmenu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenuNavigation()
    }

    private fun setupMenuNavigation() {
        val snapMealButton = view!!.findViewById<Button>(R.id.snap_meal_button)
        snapMealButton.setOnClickListener {
            val intent = Intent(context, SnapMealActivity::class.java)
            startActivityForResult(intent, 0)
        }

        val recommendationButton = view!!.findViewById<Button>(R.id.recommendations)
        recommendationButton.setOnClickListener {
            val intent = Intent(context, RecommendationActivity::class.java)
            context?.startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        val menuImage = view!!.findViewById<ImageView>(R.id.top_image)
        menuImage.setImageResource(R.drawable.graph1)
    }
}