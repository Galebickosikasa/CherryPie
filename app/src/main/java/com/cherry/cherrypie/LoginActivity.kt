package com.cherry.cherrypie

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        register_link_button.setOnClickListener {
            startActivityForResult(Intent(Constants.REGISTER_ACTIVITY_PATH), 2)
        }

        login_button.setOnClickListener {
            loginUser()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        finish()
    }

    private fun loginUser() {
        val userEmail = email_login.text.toString()
        val userPassword = password_login.text.toString()

        if (TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword)) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
        } else {

            val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

            mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener { task ->
                    task.addOnSuccessListener {
                        Toast.makeText(this, "Вход выполнен успешно )", Toast.LENGTH_SHORT).show()
                        finish()
                    }.addOnFailureListener {
                        when (it) {
                            is FirebaseAuthInvalidUserException -> {
                                Toast.makeText(this, "Нет такого пользователя (", Toast.LENGTH_SHORT).show()
                            } else -> {
                                Log.e("kek", it.toString())
                                Toast.makeText(this, "Что-то пошло не так (", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
        }
    }
}
