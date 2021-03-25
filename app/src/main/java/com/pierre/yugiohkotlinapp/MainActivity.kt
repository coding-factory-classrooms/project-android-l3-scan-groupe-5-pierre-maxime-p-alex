package com.pierre.yugiohkotlinapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.pierre.yugiohkotlinapp.recycler.CardsListActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun goToList(view: View) {
        val intent = Intent(this, CardsListActivity::class.java)
        startActivity(intent)
    }

    fun goToScan(view: View) {
        val intent = Intent(this, ScanActivity::class.java)
        startActivity(intent)

    }

    fun goToManual(view: View) {
        val intent = Intent(this, ManualActivity::class.java)
        startActivity(intent)
    }

}
