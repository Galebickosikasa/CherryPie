package com.cherry.cherrypie

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.simplepass.loadingbutton.customViews.CircularProgressImageButton
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.Math.abs
import java.util.*


class AddFoodActivity : AppCompatActivity() {
    private lateinit var storageReference : StorageReference
    private lateinit var databaseReference : DatabaseReference
    private lateinit var storage : FirebaseStorage
    private lateinit var database : FirebaseDatabase
    private lateinit var mAuth : FirebaseAuth
    private lateinit var progress_btn : CircularProgressImageButton
    private lateinit var img : ImageView
    private var url : String = Constants.PIKACHU_IMAGE_PATH
    private lateinit var text : EditText
    private var num = 0L
    private lateinit var context : Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)
        progress_btn = findViewById (R.id.progress_circular)
        progress_btn.setImageResource (R.drawable.ic_baseline_check_24)
        progress_btn.background = getDrawable (R.drawable.circle_shape)
        progress_btn.isClickable = true
        context = this
        img = findViewById (R.id.food_img)
        text = findViewById (R.id.food_name)
        Glide.with (this).load (Constants.PIKACHU_IMAGE_PATH).into (img)
        storage = FirebaseStorage.getInstance ()
        database = FirebaseDatabase.getInstance ()
        mAuth = FirebaseAuth.getInstance ()

        img.setOnClickListener {
            CropImage.activity().setAspectRatio (1, 1)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this)
        }

        progress_btn.setOnClickListener {
            if (text.text.isEmpty ()) {
                Toast.makeText (this, "Заполните все поля", Toast.LENGTH_SHORT).show ()
            } else {
                putFood ()
                finish ()
            }
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri: Uri = result.uri
                var bitmap: Bitmap? = null
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(this).contentResolver, resultUri)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                img.setImageBitmap (bitmap)
                progress_btn.startAnimation ()
                progress_btn.setImageResource (R.drawable.ic_baseline_arrow_downward_24)
                progress_btn.isClickable = false

                val original = bitmap!!
                val out = ByteArrayOutputStream()
                original.compress(Bitmap.CompressFormat.JPEG, 20, out)
                val decoded =
                    BitmapFactory.decodeStream(ByteArrayInputStream(out.toByteArray()))
                val uri = getImageUri (this, decoded)
                uploadImage (uri)
            }
        }
    }

    private fun uploadImage (uri: Uri) {
//        Log.e("kek", uri.toString())
        storageReference = storage.getReference ("${mAuth.currentUser!!.uid}/${Constants.FOOD_PATH}/${abs (uri.hashCode())}")
        storageReference.putFile (uri).addOnSuccessListener {
            progress_btn.revertAnimation ()
            progress_btn.setImageResource (R.drawable.ic_baseline_check_24)
            progress_btn.isClickable = true
            storageReference.downloadUrl.addOnSuccessListener { uri ->
                url = uri.toString ()
//                Log.e ("kek", url)
            }
        }
    }

    private fun putFood () {
//        Log.e ("kek", "put")
//        Log.e ("kek", "usr ${mAuth.currentUser!!.uid}")
        databaseReference = database.getReference ("${mAuth.currentUser!!.uid}/${Constants.FOOD_PATH}")
        databaseReference.addChildEventListener (object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                ++num
//                Log.e ("kek", snapshot.toString ())
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {}
        })
        databaseReference = database.getReference ("${mAuth.currentUser!!.uid}/${Constants.FOOD_PATH}")
        databaseReference.addListenerForSingleValueEvent (object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                databaseReference = databaseReference.child(num.toString ())
                val item = FoodItem (url, text.text.toString())
                databaseReference.setValue (item)
//                Log.e ("kek", num.toString ())
            }
        })



    }
}