package com.example.androidfirebaseproject

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.androidfirebaseproject.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityRegisterBinding

    //Firebase Autentication
    private lateinit var FirebaseAuth: FirebaseAuth

    private lateinit var ProgressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseAuth = com.google.firebase.auth.FirebaseAuth.getInstance()

        ProgressDialog = ProgressDialog(this)
        ProgressDialog.setTitle("Por favor aguarde")
        ProgressDialog.setCanceledOnTouchOutside(false)

        // Ação do botão voltar
        binding.btnBack.setOnClickListener{
            onBackPressed()
        }

        // Ações do botão de registro
        binding.btnRegistro.setOnClickListener{
            //Passos:
            //1) Imputar o dado
            //2) Validar o dado
            //3) Criar a conta registrando no Firebase
            //4) Salvar a informação do usuario no Firebase em tempo real

            validateData()
        }
    }

    private var nome = ""
    private var email = ""
    private var senha = ""

    private fun validateData() {
        //1) Imputar o dado
        nome = binding.txtNomeCadastro.text.toString().trim()
        email = binding.txtEmailCadastro.text.toString().trim()
        senha = binding.txtSenhaCadastro.text.toString().trim()
        val confirmaSenha = binding.txtSenha2Cadastro.text.toString().trim()

        //2) Validar o dado
        if(nome.isEmpty()){
            // caso o campo de nome esteja vazio
            Toast.makeText(this, "Digite seu nome..", Toast.LENGTH_SHORT).show()
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            // para e-mail inválidos
            Toast.makeText(this, "Endereço de e-mail inválido...", Toast.LENGTH_SHORT).show()
        }
        else if (senha.isEmpty()){
            // campo senha vazio
            Toast.makeText(this, "Digite uma senha..", Toast.LENGTH_SHORT).show()
        }
        else if (confirmaSenha.isEmpty()){
            // campo senha vazio
            Toast.makeText(this, "Confirme sua senha..", Toast.LENGTH_SHORT).show()
        }
        else if (senha != confirmaSenha){
            Toast.makeText(this, "Senha incorreta..", Toast.LENGTH_SHORT).show()
        }
        else{
            createUserAccount()
        }
    }

    private fun createUserAccount() {
        //3) Criar a conta registrando no Firebase

        ProgressDialog.setMessage("Criando sua conta...")
        ProgressDialog.show()

        // criando autinticação no firebase
        FirebaseAuth.createUserWithEmailAndPassword(email, senha)
            .addOnSuccessListener {
                // conta criada e adicionada no banco de dados
                updateUserInfo()
            }
            .addOnFailureListener{ e->
                // falha na criação da conta
                ProgressDialog.dismiss()
                Toast.makeText(this, "Falha na criação da conta ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUserInfo() {
        //4) Salvar a informação do usuario no Firebase em tempo real
        ProgressDialog.setMessage("Salvando informações do usuário...")

        // Timestamp
        val timestamp = System.currentTimeMillis()

        val uid = FirebaseAuth.uid

        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["uid"] = uid
        hashMap["email"] = email
        hashMap["nome"] = nome
        hashMap["fotoCapa"] = ""
        hashMap["tipo"] = "user"
        hashMap["timestamp"] = timestamp

        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(uid!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                // informações do usuários salvas
                ProgressDialog.dismiss()
                Toast.makeText(this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@RegisterActivity, DashboardUserActivity::class.java))
                finish()
            }
            .addOnFailureListener{e->
                //falha na adição de dados no banco
                ProgressDialog.dismiss()
                Toast.makeText(this, "Falha no salvamento de dados ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}