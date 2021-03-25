package com.pierre.yugiohkotlinapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pierre.yugiohkotlinapp.databinding.ActivityDetailsBinding
import com.pierre.yugiohkotlinapp.recycler.CardListAdapter

class DetailsActivity : AppCompatActivity()  {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}