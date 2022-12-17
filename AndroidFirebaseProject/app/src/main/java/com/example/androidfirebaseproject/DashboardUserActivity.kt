package com.example.androidfirebaseproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidfirebaseproject.databinding.ActivityDashboardUserBinding
import com.google.firebase.auth.FirebaseAuth

class DashboardUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardUserBinding

    private lateinit var FirebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseAuth = com.google.firebase.auth.FirebaseAuth.getInstance()
        checkUser()

        binding.btnLogout.setOnClickListener{
            FirebaseAuth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun checkUser() {
        val FirebaseUser = FirebaseAuth.currentUser
        if (FirebaseUser == null){
            binding.subtitleDashUser.text = "Não está logado"
        }
        else{
            val email = FirebaseUser.email
            binding.subtitleDashUser.text = email
        }
    }
}