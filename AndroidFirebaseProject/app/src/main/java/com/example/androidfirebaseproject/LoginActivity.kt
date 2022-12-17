package com.example.androidfirebaseproject

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.FrameLayout
import android.widget.Toast
import com.example.androidfirebaseproject.databinding.ActivityDashboardAdminBinding
import com.example.androidfirebaseproject.databinding.ActivityDashboardUserBinding
import com.example.androidfirebaseproject.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.zip.Inflater

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var FirebaseAuth: FirebaseAuth

    private lateinit var ProgressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseAuth = com.google.firebase.auth.FirebaseAuth.getInstance()

        ProgressDialog = ProgressDialog(this)
        ProgressDialog.setTitle("Por favor aguarde")
        ProgressDialog.setCanceledOnTouchOutside(false)

        // Configuração do botão não tenho conta
        binding.txtNovoUsuario.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // Configuração do botão de login
        binding.btnLoginScreen2.setOnClickListener{
            //Passos:
            //1) Imputar os dados
            //2) Validar os dados
            //3) Autenticação de Login no Firebase
            //4) Checagem de tipo de usuario
                // Se usuário -> dash usuario
                // Se administrador -> dash admin

            validateData()

        }
    }

    private var email = ""
    private var senha = ""

    private fun validateData() {
        //1) Imputar os dados
        email = binding.txtEmail.text.toString().trim()
        senha = binding.txtSenha.text.toString().trim()

        //2) Validar os dados
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Formato de e-mail inválido.", Toast.LENGTH_SHORT).show()
        }
        else if (senha.isEmpty()){
            Toast.makeText(this, "Digite uma senha...", Toast.LENGTH_SHORT).show()
        }
        else{
            loginUser()
        }
    }

    private fun loginUser() {
        //3) Autenticação de Login no Firebase
        ProgressDialog.setMessage("Carregando...")
        ProgressDialog.show()

        FirebaseAuth.signInWithEmailAndPassword(email, senha)
            .addOnSuccessListener {
                // login executado com sucesso
                checkUser()
            }
            .addOnFailureListener{e->
                // falha na autenticação do login
                ProgressDialog.dismiss()
                Toast.makeText(this, "Falha no acesso ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    private fun checkUser() {
        //4) Checagem de tipo de usuario
            // Se usuário -> dash usuario
            // Se administrador -> dash admin

        ProgressDialog.setMessage("Checando o tipo...")

        val FirebaseUser = FirebaseAuth.currentUser!!

        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(FirebaseUser.uid)

            .addListenerForSingleValueEvent(object : ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {

                    ProgressDialog.dismiss()

                    val tipo = snapshot.child("tipo").value


                    if (tipo == "user"){
                      startActivity(Intent(this@LoginActivity, DashboardUserActivity::class.java))
                      finish()
                    }

                    else if (tipo == "admin"){
                        startActivity(Intent(this@LoginActivity, DashboardAdminActivity::class.java))
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}