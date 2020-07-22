package com.cherry.cherrypie

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*
import java.io.ByteArrayOutputStream
import java.net.URL

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

    fun encodeTobase64(image: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        val imageEncoded: String = Base64.encodeToString(b, Base64.DEFAULT)
        Log.d("Image Log:", imageEncoded)
        return imageEncoded
    }

    private fun loginUser() {
        val userEmail = email_login.text.toString()
        val userPassword = password_login.text.toString()

        if (TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword)) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
        } else {

            val mAuth: FirebaseAuth = FirebaseAuth.getInstance ()

            mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener { task ->
                    task.addOnSuccessListener {

                        val user = mAuth.currentUser!!
                        val database = FirebaseDatabase.getInstance()
                        val databaseReference = database.getReference(Constants.USERS_PATH + user.uid)
                        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {}

                            override fun onDataChange(snapshot: DataSnapshot) {
                                val t = Thread(Runnable {
                                    val userInfo = snapshot.value as HashMap<String, Any>
                                    val username = userInfo["username"] as String
                                    val url = URL(userInfo["image"] as String)
                                    val bitmap = BitmapFactory.decodeStream (url.openConnection ().getInputStream ())
                                    val sp = getSharedPreferences ("userInfo", Context.MODE_PRIVATE)
                                    sp.edit ().putString ("username", username).putString ("image", encodeTobase64 (bitmap)).apply ()
                                    finish ()
                                })
                                t.start ()
                            }
                        })

                        Toast.makeText (this, "Вход выполнен успешно )", Toast.LENGTH_SHORT).show ()
                        Log.e ("kek", "log-in")
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
