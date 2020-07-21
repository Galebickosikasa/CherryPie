package com.cherry.cherrypie

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_register.*
import java.io.ByteArrayOutputStream
import java.net.URL

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button.setOnClickListener {
            createUserAccount()
        }
    }

    fun encodeTobase64(image: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        val imageEncoded: String = Base64.encodeToString(b, Base64.DEFAULT)
        Log.d("Image Log:", imageEncoded)
        return imageEncoded
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
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail, userPassword).addOnSuccessListener {
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
                        }
                    }.addOnFailureListener {
                        Log.e("kek", it.toString())
                        Toast.makeText(this, "Что-то пошло не так (", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun saveUserInfo(userName: String, userEmail: String) {
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val usersReference : DatabaseReference = FirebaseDatabase.getInstance ().getReference (Constants.USERS_PATH)

        val userMap = HashMap<String, Any> ()
        userMap["uid"] = currentUserID
        userMap["username"] = userName
        userMap["email"] = userEmail
        userMap["image"] = Constants.PIKACHU_IMAGE_PATH

        usersReference.child(currentUserID).setValue(userMap).addOnCompleteListener { task ->
                task.addOnSuccessListener {
                    Toast.makeText (this, getString(R.string.registration_is_successful), Toast.LENGTH_SHORT).show()
                    finish ()
                }.addOnFailureListener {
                    Log.e("kek", it.toString())
                    Toast.makeText(this, getString(R.string.fail_message), Toast.LENGTH_SHORT).show()
                }
            }

    }
}
