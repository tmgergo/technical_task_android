package uk.co.tmgergo.userstechtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uk.co.tmgergo.userstechtest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}