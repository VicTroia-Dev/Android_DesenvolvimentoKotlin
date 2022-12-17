package com.example.androidfirebaseproject

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.androidfirebaseproject.databinding.ActivityCategoriasBinding
import com.example.androidfirebaseproject.databinding.ActivityDashboardUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CategoriasActivity : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityCategoriasBinding

    private lateinit var FirebaseAuth:FirebaseAuth

    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseAuth = com.google.firebase.auth.FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor aguarde um instante ...")
        progressDialog.setCanceledOnTouchOutside(false)

        // Botão de voltar
        binding.btnVoltar.setOnClickListener{
            onBackPressed()
        }

        // Botão de adicionar nova categoria
        binding.btnAddCategoria.setOnClickListener{
            validateData()
        }

    }

    private var categoria = ""

    private fun validateData() {
        // Obtém dados
        categoria = binding.txtCategoriaTitulo.text.toString().trim()

        //Validando dados
        if (categoria.isEmpty()){
            Toast.makeText(this, "Adicione um novo roteiro de viagem...", Toast.LENGTH_SHORT).show()

        }
        else{
            addCategoryFirebase()
        }
    }

    private fun addCategoryFirebase() {
        progressDialog.show()

        //Obtém Timestamp
        val timestamp = System.currentTimeMillis()

        //Setando os campos que o banco deverá armazenar
        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "$timestamp"
        hashMap["categoria"] = categoria
        hashMap["uid"] = "${FirebaseAuth.uid}"

        //Inclui os dados no banco no Firebase
        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Roteiro adicionado ;D", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Falha na adição ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}