package com.example.vetlesolgard.nutritrack.snapmeal

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.example.vetlesolgard.nutritrack.R

class SnapMealActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snapmeal)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}