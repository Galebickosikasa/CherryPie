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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

class ProfileFragment : Fragment() {
    private lateinit var container: ViewGroup
    private lateinit var userImage: CircleImageView
    private lateinit var username: TextView
    private lateinit var userEmail: TextView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userInfo: HashMap<String, Any>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.container = container!!
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onStart() {
        super.onStart()
        userImage = container.findViewById(R.id.userImage)
        username = container.findViewById(R.id.username)
        userEmail = container.findViewById(R.id.email)
        mAuth = FirebaseAuth.getInstance()
        user = mAuth.currentUser!!
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference(Constants.USERS_PATH + user.uid)

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                userInfo = snapshot.value as HashMap<String, Any>
                try {
                    username.text = userInfo["username"] as CharSequence?
                    Glide.with(activity!!).load(userInfo["image"]).into(userImage)
                } catch (e: NullPointerException) {
                    Log.e("kek", "ooops")
                }
            }
        })

        userImage.setOnClickListener {
            CropImage.activity().setCropShape(CropImageView.CropShape.OVAL).setAspectRatio(1, 1)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(activity!!)
        }

        userEmail.text = user.email

        sign_out_button.setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()

        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
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

    fun photoCrop(resultCode: Int, data: Intent?) {
        val result = CropImage.getActivityResult(data)
        if (resultCode == Activity.RESULT_OK) {
            val resultUri: Uri = result.uri
            var bitmap: Bitmap? = null
            try {
                bitmap = MediaStore.Images.Media.getBitmap(
                    Objects.requireNonNull(activity!!).contentResolver,
                    resultUri
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val original = bitmap!!
            val out = ByteArrayOutputStream()
            original.compress(Bitmap.CompressFormat.JPEG, 20, out)
            val decoded =
                BitmapFactory.decodeStream(ByteArrayInputStream(out.toByteArray()))
            val uri = getImageUri(activity!!, decoded)
            uploadImage(uri)
        }
    }

    private fun uploadImage(uri: Uri) {
        storageReference = storage.getReference("${user.uid}/${Constants.PHOTO_PATH}")
        storageReference.putFile(uri).addOnSuccessListener {
            storageReference.downloadUrl.addOnSuccessListener { uri ->
                val url = uri.toString()
                userInfo["image"] = url
                databaseReference.setValue(userInfo).addOnSuccessListener {
                    Glide.with(activity!!).load(url).into(userImage)
                    Toast.makeText(activity, getString(R.string.uploading_is_successful), Toast.LENGTH_SHORT)
                        .show()
                }.addOnFailureListener {
                    Log.e("kek", it.toString())
                    Toast.makeText(activity, getString(R.string.fail_message), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
