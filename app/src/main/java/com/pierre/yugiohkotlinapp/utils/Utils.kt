package com.pierre.yugiohkotlinapp.utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

object Utils {
    fun showAlert(title: String, context: Context, activity: AppCompatActivity) {
        val builder = AlertDialog.Builder(activity)
        Toast.makeText(
            context,
            title, Toast.LENGTH_SHORT
        ).show()
        builder.show()
    }
}