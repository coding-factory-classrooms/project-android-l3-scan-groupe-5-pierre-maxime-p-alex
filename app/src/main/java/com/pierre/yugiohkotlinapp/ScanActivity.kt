package com.pierre.yugiohkotlinapp

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import kotlinx.android.synthetic.main.activity_scan.*
import kotlinx.coroutines.NonCancellable.start

class ScanActivity : AppCompatActivity() {

    private val MY_PERMISSIONS_REQUEST_CAMERA: Int = 101
    private lateinit var cameraSource: CameraSource
    private lateinit var textRecognizer: TextRecognizer



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        requestForPermission()

//  Create text Recognizer
        textRecognizer = TextRecognizer.Builder(this).build()
        if (!textRecognizer.isOperational) {
            Toast.makeText(
                this,
                "Dependencies are not loaded yet...please try after few moment!!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        //  Init camera source to use high resolution and auto focus
        cameraSource = CameraSource.Builder(applicationContext, textRecognizer)
            .setFacing(CameraSource.CAMERA_FACING_BACK)
            .setRequestedPreviewSize(1280, 1024)
            .setAutoFocusEnabled(true)
            .setRequestedFps(3.0f)
            .build()


        surface_camera_preview.holder.addCallback(object : SurfaceHolder.Callback {

            override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
            }

            override fun surfaceDestroyed(p0: SurfaceHolder) {
                cameraSource.stop()
            }

            override fun surfaceCreated(p0: SurfaceHolder) {
                try {
                    if (isCameraPermissionGranted()) {
                        if (ActivityCompat.checkSelfPermission(
                                this@ScanActivity,
                                Manifest.permission.CAMERA
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            requestForPermission()
                            return
                        }
                        cameraSource.start(surface_camera_preview.holder)
                    } else {
                        requestForPermission()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@ScanActivity, "Error :" + e.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }

        })

        textRecognizer.setProcessor(object : Detector.Processor<TextBlock> {
            override fun release() {}

            override fun receiveDetections(detections: Detector.Detections<TextBlock>) {
                val items = detections.detectedItems

                if (items.size() <= 0) {
                    return
                }

                tv_result.post {
                    val stringBuilder = StringBuilder()
                    for (i in 0 until items.size()) {
                        val item = items.valueAt(i)
                        stringBuilder.append(item.value)
                        stringBuilder.append("\n")
                    }
                    tv_result.text = stringBuilder.toString()
                }
            }
        })
    }

    private fun requestForPermission() {
        if(ContextCompat.checkSelfPermission(this@ScanActivity,
            Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED
        )   {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@ScanActivity,
                    Manifest.permission.CAMERA
                )
            ) {

            } else {

            ActivityCompat.requestPermissions(
                this@ScanActivity,
            arrayOf(
                Manifest.permission.CAMERA
            ),
                MY_PERMISSIONS_REQUEST_CAMERA
            )
            }
        } else {
            // Permission has already been granted

        }
    }

    private fun isCameraPermissionGranted(): Boolean {

        return ContextCompat.checkSelfPermission(
            this@ScanActivity,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_CAMERA -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                } else {
                    requestForPermission()
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }
}
