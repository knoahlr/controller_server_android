package com.example.server_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

import com.example.server_1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example of a call to a native method
        binding.sampleText.text = thestringFromJNI()
    }

    /**
     * A native method that is implemented by the 'server_1' native library,
     * which is packaged with this application.
     */
    external fun thestringFromJNI(): String

    companion object {
        // Used to load the 'server_1' library on application startup.
        init {
            System.loadLibrary("server_1")
        }
    }
}