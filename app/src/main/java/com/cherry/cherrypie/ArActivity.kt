package com.cherry.cherrypie

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.ImageFormat
import android.icu.text.SimpleDateFormat
import android.media.Image
import android.media.ImageReader
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.provider.MediaStore
import android.view.MotionEvent
import android.view.PixelCopy
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.material.snackbar.Snackbar
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.ArSceneView
import com.google.ar.sceneform.HitTestResult
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.FixedHeightViewSizer
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_ar.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.Buffer
import java.util.*
import java.util.function.Consumer

class ArActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var eliotRenderable: ViewRenderable
    lateinit var darlinRenderable: ViewRenderable
    lateinit var angelaRenderable: ViewRenderable
    lateinit var dominikaRenderable: ViewRenderable
    lateinit var misterRenderable: ViewRenderable
    lateinit var robotRenderable: ViewRenderable
    lateinit var sisterRenderable: ViewRenderable
    lateinit var dadRenderable: ViewRenderable

    lateinit var arrayView: Array<View>

    lateinit var arFragment: ArFragment
    private var selected = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)

        setupArray()
        setupClickListener()
        setupModel()

        arFragment = supportFragmentManager.findFragmentById(R.id.scene_form_fragment) as ArFragment
        arFragment.setOnTapArPlaneListener { hitResult, _, _ ->
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)

            createModel(anchorNode, selected)
        }
        fab.setOnClickListener { takePhoto() }
    }

    private fun setupModel() {

        ViewRenderable.builder()
            .setView(this, R.layout.imgboard)
            .setVerticalAlignment(ViewRenderable.VerticalAlignment.BOTTOM)
            .setSizer(FixedHeightViewSizer(1.7f))
            .build()
            .thenAccept(
                Consumer { renderable: ViewRenderable ->
                    eliotRenderable = renderable
                    val imageView: ImageView = renderable.view as ImageView
                    imageView.setImageDrawable(getDrawable(R.drawable.eliot))
                }
            )

        ViewRenderable.builder()
            .setView(this, R.layout.imgboard)
            .setVerticalAlignment(ViewRenderable.VerticalAlignment.BOTTOM)
            .setSizer(FixedHeightViewSizer(1.7f))
            .build()
            .thenAccept(
                Consumer { renderable: ViewRenderable ->
                    darlinRenderable = renderable
                    val imageView: ImageView = renderable.view as ImageView
                    imageView.setImageDrawable(getDrawable(R.drawable.darlin))
                }
            )

        ViewRenderable.builder()
            .setView(this, R.layout.imgboard)
            .setVerticalAlignment(ViewRenderable.VerticalAlignment.BOTTOM)
            .setSizer(FixedHeightViewSizer(1.7f))
            .build()
            .thenAccept(
                Consumer { renderable: ViewRenderable ->
                    angelaRenderable = renderable
                    val imageView: ImageView = renderable.view as ImageView
                    imageView.setImageDrawable(getDrawable(R.drawable.angela))
                }
            )

        ViewRenderable.builder()
            .setView(this, R.layout.imgboard)
            .setVerticalAlignment(ViewRenderable.VerticalAlignment.BOTTOM)
            .setSizer(FixedHeightViewSizer(1.7f))
            .build()
            .thenAccept(
                Consumer { renderable: ViewRenderable ->
                    dominikaRenderable = renderable
                    val imageView: ImageView = renderable.view as ImageView
                    imageView.setImageDrawable(getDrawable(R.drawable.dominika))
                }
            )

        ViewRenderable.builder()
            .setView(this, R.layout.imgboard)
            .setVerticalAlignment(ViewRenderable.VerticalAlignment.BOTTOM)
            .setSizer(FixedHeightViewSizer(1.7f))
            .build()
            .thenAccept(
                Consumer { renderable: ViewRenderable ->
                    misterRenderable = renderable
                    val imageView: ImageView = renderable.view as ImageView
                    imageView.setImageDrawable(getDrawable(R.drawable.mister))
                }
            )

        ViewRenderable.builder()
            .setView(this, R.layout.imgboard)
            .setVerticalAlignment(ViewRenderable.VerticalAlignment.BOTTOM)
            .setSizer(FixedHeightViewSizer(1.7f))
            .build()
            .thenAccept(
                Consumer { renderable: ViewRenderable ->
                    robotRenderable = renderable

                    val imageView: ImageView = renderable.view as ImageView
                    imageView.setImageDrawable(getDrawable(R.drawable.robot))
                }
            )

        ViewRenderable.builder()
            .setView(this, R.layout.imgboard)
            .setVerticalAlignment(ViewRenderable.VerticalAlignment.BOTTOM)
            .setSizer(FixedHeightViewSizer(1.7f))
            .build()
            .thenAccept(
                Consumer { renderable: ViewRenderable ->
                    sisterRenderable = renderable

                    val imageView: ImageView = renderable.view as ImageView
                    imageView.setImageDrawable(getDrawable(R.drawable.sister))
                }
            )

        ViewRenderable.builder()
            .setView(this, R.layout.imgboard)
            .setVerticalAlignment(ViewRenderable.VerticalAlignment.BOTTOM)
            .setSizer(FixedHeightViewSizer(1.7f))
            .build()
            .thenAccept(
                Consumer { renderable: ViewRenderable ->
                    dadRenderable = renderable
                    val imageView: ImageView = renderable.view as ImageView
                    imageView.setImageDrawable(getDrawable(R.drawable.dad))
                }
            )
    }

    private fun createModel(anchorNode: AnchorNode, selected: Int) {
        when (selected) {
            1 -> {
                val eliot = TransformableNode(arFragment.transformationSystem)
                eliot.localPosition = Vector3(0f, anchorNode.localPosition.y + 1.6f, 0f)
                eliot.setParent(anchorNode)
                eliot.setOnTapListener { _: HitTestResult, _: MotionEvent ->
                    anchorNode.setParent(null)
                }
                eliot.renderable = eliotRenderable
                eliot.select()
            }
            2 -> {
                val darlin = TransformableNode(arFragment.transformationSystem)
                darlin.localPosition = Vector3(0f, anchorNode.localPosition.y + 1.5f, 0f)
                darlin.setParent(anchorNode)
                darlin.setOnTapListener { _: HitTestResult, _: MotionEvent ->
                    anchorNode.setParent(null)
                }
                darlin.renderable = darlinRenderable
                darlin.select()
            }
            3 -> {
                val angela = TransformableNode(arFragment.transformationSystem)
                angela.localPosition = Vector3(0f, anchorNode.localPosition.y + 1.5f, 0f)
                angela.setParent(anchorNode)
                angela.setOnTapListener { _: HitTestResult, _: MotionEvent ->
                    anchorNode.setParent(null)
                }
                angela.renderable = angelaRenderable
                angela.select()
            }
            4 -> {
                val dominika = TransformableNode(arFragment.transformationSystem)
                dominika.localPosition = Vector3(0f, anchorNode.localPosition.y + 1.5f, 0f)
                dominika.setParent(anchorNode)
                dominika.setOnTapListener { _: HitTestResult, _: MotionEvent ->
                    anchorNode.setParent(null)
                }
                dominika.renderable = dominikaRenderable
                dominika.select()
            }
            5 -> {
                val mister = TransformableNode(arFragment.transformationSystem)
                mister.localPosition = Vector3(0f, anchorNode.localPosition.y + 1.5f, 0f)
                mister.setParent(anchorNode)
                mister.setOnTapListener { _: HitTestResult, _: MotionEvent ->
                    anchorNode.setParent(null)
                }
                mister.renderable = misterRenderable
                mister.select()
            }
            6 -> {
                val robot = TransformableNode(arFragment.transformationSystem)
                robot.localPosition = Vector3(0f, anchorNode.localPosition.y + 1.5f, 0f)
                robot.setParent(anchorNode)
                robot.setOnTapListener { hitTestResult: HitTestResult, motionEvent: MotionEvent ->
                    anchorNode.setParent(null)
                }
                robot.renderable = robotRenderable
                robot.select()
            }
            7 -> {
                val sister = TransformableNode(arFragment.transformationSystem)
                sister.localPosition = Vector3(0f, anchorNode.localPosition.y + 1.5f, 0f)
                sister.setParent(anchorNode)
                sister.setOnTapListener { hitTestResult: HitTestResult, motionEvent: MotionEvent ->
                    anchorNode.setParent(null)
                }
                sister.renderable = sisterRenderable
                sister.select()
            }
            8 -> {
                val dad = TransformableNode(arFragment.transformationSystem)
                dad.localPosition = Vector3(0f, anchorNode.localPosition.y + 1.5f, 0f)
                dad.setParent(anchorNode)
                dad.setOnTapListener { hitTestResult: HitTestResult, motionEvent: MotionEvent ->
                    anchorNode.setParent(null)
                }
                dad.renderable = dadRenderable
                dad.select()
            }
            else -> {
                val robot = TransformableNode(arFragment.transformationSystem)
                robot.localPosition = Vector3(0f, anchorNode.localPosition.y + 1.5f, 0f)
                robot.setParent(anchorNode)
                robot.setOnTapListener { hitTestResult: HitTestResult, motionEvent: MotionEvent ->
                    anchorNode.setParent(null)
                }
                robot.renderable = robotRenderable
                robot.select()
            }
        }
    }

    private fun setupArray() {
        arrayView = arrayOf(
            eliot,
            darlin,
            angela,
            dominika,
            mister,
            robot,
            sister,
            dad
        )
    }

    private fun setupClickListener() {
        for (i in arrayView.indices) {
            arrayView[i].setOnClickListener(this)
        }
    }

    override fun onClick(view: View?) {
        selected = when (view!!.id) {
            R.id.eliot -> 1
            R.id.darlin -> 2
            R.id.angela -> 3
            R.id.dominika -> 4
            R.id.mister -> 5
            R.id.robot -> 6
            R.id.sister -> 7
            R.id.dad -> 8
            else -> 8
        }
    }

    class WritingArFragment : ArFragment() {
        override fun getAdditionalPermissions(): Array<String?> {
            val additionalPermissions =
                super.getAdditionalPermissions()
            val permissionLength =
                additionalPermissions?.size ?: 0
            val permissions =
                arrayOfNulls<String>(permissionLength + 1)

            permissions[0] = Manifest.permission.WRITE_EXTERNAL_STORAGE
            if (permissionLength > 0) {
                System.arraycopy(
                    additionalPermissions,
                    0,
                    permissions,
                    1,
                    additionalPermissions!!.size
                )
            }
            return permissions
        }
    }

    private fun generateFilename(): String? {
        val date: String =
            SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
        return Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        ).toString().plus(File.separator.toString()) + "Sceneform/" + date + "_screenshot.jpg"
    }

    @Throws(IOException::class)
    private fun saveBitmapToDisk(bitmap: Bitmap, filename: String) {
        val out = File(filename)
        if (!out.parentFile.exists()) {
            out.parentFile.mkdirs()
        }
        try {
            FileOutputStream(filename).use { outputStream ->
                ByteArrayOutputStream().use { outputData ->
                    bitmap.compress(CompressFormat.JPEG, 85, outputData)
                    outputData.writeTo(outputStream)
                    MediaStore.Images.Media.insertImage(
                        contentResolver, bitmap, generateFilename(), "ar_photo"
                    )
                    outputStream.flush()
                    outputStream.close()

                }
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            throw IOException("Failed to save bitmap to disk", ex)
        }
    }

    private fun takePhoto() {
        val filename = generateFilename()
        val view: ArSceneView = arFragment.arSceneView

        val bitmap = Bitmap.createBitmap(
            view.width, view.height,
            Bitmap.Config.ARGB_8888
        )


        val handlerThread = HandlerThread("PixelCopier")
        handlerThread.start()
        PixelCopy.request(
            view, bitmap,
            { copyResult ->
                if (copyResult === PixelCopy.SUCCESS) {
                    try {
                        saveBitmapToDisk(bitmap, filename!!)
                    } catch (e: IOException) {
                        val toast = Toast.makeText(
                            this@ArActivity, e.toString(),
                            Toast.LENGTH_LONG
                        )
                        toast.show()
                        return@request
                    }
                    val snackbar = Snackbar.make(
                        findViewById(android.R.id.content),
                        "Photo saved", Snackbar.LENGTH_LONG
                    )
                    snackbar.setAction(
                        "Open in Photos"
                    ) { v: View? ->
                        val photoFile = File(filename)
                        val photoURI: Uri = FileProvider.getUriForFile(
                            this@ArActivity,
                            this@ArActivity.packageName + ".ar.codelab.name.provider",
                            photoFile
                        )
                        val intent = Intent(Intent.ACTION_VIEW, photoURI)
                        intent.setDataAndType(photoURI, "image/*")
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        startActivity(intent)
                    }
                    snackbar.show()
                } else {
                    val toast = Toast.makeText(
                        this@ArActivity,
                        "Failed to copyPixels: $copyResult", Toast.LENGTH_LONG
                    )
                    toast.show()
                }
                handlerThread.quitSafely()
            },
            Handler(handlerThread.looper))

    }
}
