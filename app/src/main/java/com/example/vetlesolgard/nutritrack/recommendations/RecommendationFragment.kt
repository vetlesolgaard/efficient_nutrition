package com.example.vetlesolgard.nutritrack.recommendations

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.vetlesolgard.nutritrack.R

class RecommendationFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recommendations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backToMenuButton = view.findViewById<Button>(R.id.back_to_menu)
        backToMenuButton.setOnClickListener {
            activity!!.onBackPressed()
        }
    }
}