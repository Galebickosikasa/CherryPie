package com.cherry.cherrypie

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
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
import java.net.URL
import java.util.*

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
    private lateinit var progress1 : ProgressBar
    private lateinit var progress2 : ProgressBar
    private lateinit var progress3 : ProgressBar
    private lateinit var progress4 : ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.container = container!!
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    @ExperimentalStdlibApi
    override fun onStart() {
        super.onStart()
        userImage = container.findViewById(R.id.userImage)
        username = container.findViewById(R.id.username)
        userEmail = container.findViewById(R.id.email)
        progress1 = container.findViewById (R.id.progress_1)
        progress2 = container.findViewById (R.id.progress_2)
        progress3 = container.findViewById (R.id.progress_3)
        progress4 = container.findViewById (R.id.progress_4)
        mAuth = FirebaseAuth.getInstance()
        user = mAuth.currentUser!!
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference(Constants.USERS_PATH + user.uid)

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                userInfo = snapshot.value as HashMap<String, Any>
            }
        })

        progress1.max = 10
        progress2.max = 12
        progress3.max = 10
        progress4.max = 13

        val hm = (activity as MainActivity).progressMap as HashMap<String, Int>
        progress1.progress = hm["1"]!!.countOneBits ()
        progress2.progress = hm["2"]!!.countOneBits ()
        progress3.progress = hm["3"]!!.countOneBits ()
        progress4.progress = hm["4"]!!.countOneBits ()

        val sp = activity!!.getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val nickname = sp.getString("username", "")
        val smth = sp.getString("image", "")
        userImage.setImageBitmap(decodeBase64(smth))
        username.text = nickname

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

    fun encodeTobase64(image: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        val imageEncoded = Base64.encodeToString(b, Base64.DEFAULT)
        Log.d("Image Log:", imageEncoded)
        return imageEncoded
    }

    fun decodeBase64(input: String?): Bitmap? {
        val decodedByte: ByteArray = Base64.decode(input, 0)
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
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
            val decoded = BitmapFactory.decodeStream(ByteArrayInputStream(out.toByteArray()))
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

                val t = Thread(Runnable {
                    val _url = URL(url)
                    val bitmap = BitmapFactory.decodeStream(_url.openConnection().getInputStream())
                    val sp = activity!!.getSharedPreferences("userInfo", Context.MODE_PRIVATE)
                    sp.edit().putString("image", encodeTobase64(bitmap)).apply()
                })
                t.start()

                databaseReference.setValue(userInfo).addOnSuccessListener {
                    Glide.with(activity!!).load(url).into(userImage)
                    Toast.makeText(
                        activity,
                        getString(R.string.uploading_is_successful),
                        Toast.LENGTH_SHORT
                    ).show()
                }.addOnFailureListener {
                    Log.e("kek", it.toString())
                    Toast.makeText(activity, getString(R.string.fail_message), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}
