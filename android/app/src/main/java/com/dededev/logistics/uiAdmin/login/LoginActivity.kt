package com.dededev.logistics.uiAdmin.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dededev.logistics.MainActivity
import com.dededev.logistics.database.user.User
import com.dededev.logistics.databinding.ActivityLoginBinding
import com.dededev.logistics.uiNonAdmin.MainActivityNonAdmin
import com.dededev.logistics.utils.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var pref: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        pref = SessionManager(this)

        if (pref.isLoggedIn() && pref.getJabatan() == "admin") {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else if (pref.isLoggedIn() && pref.getJabatan() == "nonadmin") {
            val intent = Intent(this, MainActivityNonAdmin::class.java)
            startActivity(intent)
        } else {
            binding.btnLogin.setOnClickListener {
                val email = binding.loginEmail.text.toString()
                val password = binding.loginPassword.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val currentUser = mAuth.currentUser
                            val userId = currentUser?.uid
                            if (userId != null) {
                                val userDocument = db.collection("users").document(userId)

                                userDocument.get().addOnCompleteListener { documentTask ->
                                    if (documentTask.isSuccessful) {
                                        val user = documentTask.result?.toObject(User::class.java)
                                        val jabatan = user?.jabatan
                                        if (jabatan != null) {
                                            pref.setLoggedIn(true, jabatan)
                                            val intent = Intent(this, MainActivity::class.java)
                                            startActivity(intent)
                                        }
                                    }
                                    else {
                                        Toast.makeText(this, documentTask.exception?.message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }


                        }
                        else {
                            Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }


    }
}