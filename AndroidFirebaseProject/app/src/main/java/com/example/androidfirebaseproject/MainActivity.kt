package com.example.androidfirebaseproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidfirebaseproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //view binding
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // ativando o botão de login
        binding.btnLoginScreen1.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // ativando o botão de skip
        binding.btnSkipScreen1.setOnClickListener{
            startActivity(Intent(this, DashboardUserActivity::class.java))
        }


    }
}