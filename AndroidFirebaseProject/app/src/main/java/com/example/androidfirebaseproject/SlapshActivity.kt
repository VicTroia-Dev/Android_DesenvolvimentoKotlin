package com.example.androidfirebaseproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.androidfirebaseproject.databinding.ActivityDashboardAdminBinding
import com.example.androidfirebaseproject.databinding.ActivityDashboardUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SlapshActivity : AppCompatActivity() {

    private lateinit var FirebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        FirebaseAuth =  com.google.firebase.auth.FirebaseAuth.getInstance()

        Handler().postDelayed(Runnable {
            checkUser()
        }, 1000) // tempo de delay de exibição dessa tela (1s)
    }

    private fun checkUser() {
        val FirebaseUser = FirebaseAuth.currentUser
        if (FirebaseUser == null){
            startActivity(Intent(this, MainActivity::class.java))
        }
        else{

            val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
            ref.child(FirebaseUser.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener{

                    override fun onDataChange(snapshot: DataSnapshot) {

                        val tipo = snapshot.child("tipo").value

                        if (tipo == "user"){
                            startActivity(Intent(this@SlapshActivity, ActivityDashboardUserBinding::class.java))
                            finish()
                        }
                        else if (tipo == "admin"){
                            startActivity(Intent(this@SlapshActivity, ActivityDashboardAdminBinding::class.java))
                            finish()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        }
    }
}