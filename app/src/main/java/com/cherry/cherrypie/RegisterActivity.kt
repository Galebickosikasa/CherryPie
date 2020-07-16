package com.cherry.cherrypie

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button.setOnClickListener {
            createUserAccount()
        }
    }

    private fun createUserAccount() {
        val userName = username_register.text.toString()
        val userEmail = email_register.text.toString()
        val userPassword = password_register.text.toString()

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword)) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
        } else {
            val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
            mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener { task ->
                    task.addOnSuccessListener {
                        saveUserInfo(userName, userEmail)
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail, userPassword)
                    }.addOnFailureListener {
                        Log.e("kek", it.toString())
                        Toast.makeText(this, "Что-то пошло не так (", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun saveUserInfo(userName: String, userEmail: String) {
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val usersReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference().child("Users")

        val userMap = HashMap<String, Any>()
        userMap["uid"] = currentUserID
        userMap["username"] = userName
        userMap["email"] = userEmail
        userMap["image"] = Constants.PIKACHU_IMAGE_PATH

        usersReference.child(currentUserID).setValue(userMap)
            .addOnCompleteListener { task ->
                task.addOnSuccessListener {
                    Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show()
                    finish()
                }.addOnFailureListener {
                    Log.e("kek", it.toString())
                    Toast.makeText(this, "Что-то пошло не так (", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
