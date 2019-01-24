package com.example.vetlesolgard.nutritrack

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.content.Intent
import com.example.vetlesolgard.nutritrack.snapmeal.SnapMealActivity

class MainMenuFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(com.example.vetlesolgard.nutritrack.R.layout.fragment_mainmenu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val snapMealButton = view.findViewById<Button>(R.id.snap_meal_button)
        snapMealButton.setOnClickListener {
            val intent = Intent(context, SnapMealActivity::class.java)
            context?.startActivity(intent)
        }

        val recommendationButton = view.findViewById<Button>(R.id.recommendations)
        recommendationButton.setOnClickListener {

        }

    }
}