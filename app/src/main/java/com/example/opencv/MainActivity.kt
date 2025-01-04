package com.example.opencv

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.OpenCVLoader
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.CascadeClassifier
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class MainActivity : ComponentActivity(), CameraBridgeViewBase.CvCameraViewListener2 {

    private lateinit var cameraView: CameraBridgeViewBase
    private var faceDetector: CascadeClassifier? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Check and request camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission()
        } else {
            initializeOpenCV()
        }
    }

    private fun requestCameraPermission() {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                initializeOpenCV()
            } else {
                Log.e("OpenCV", "Camera permission denied")
            }
        }

        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun initializeOpenCV() {
        // Initialize OpenCV
        if (OpenCVLoader.initLocal()) {
            Log.d("OpenCV", "OpenCV initialized successfully")
            setupFaceDetector()
            setupCamera()
        } else {
            Log.e("OpenCV", "OpenCV initialization failed")
        }
    }

    private fun setupFaceDetector() {
        try {
            // Copy the Haar Cascade model file from raw resources
            val inputStream: InputStream = resources.openRawResource(R.raw.haarcascade_frontalface_default)
            val cascadeFile = File(getDir("cascade", MODE_PRIVATE), "haarcascade_frontalface_default.xml")
            val outputStream = FileOutputStream(cascadeFile)

            val buffer = ByteArray(4096)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
            inputStream.close()
            outputStream.close()

            // Load the CascadeClassifier
            faceDetector = CascadeClassifier(cascadeFile.absolutePath)
            if (faceDetector?.empty() == true) {
                faceDetector = null
                Log.e("OpenCV", "Failed to load CascadeClassifier")
            } else {
                Log.d("OpenCV", "CascadeClassifier loaded successfully")
            }
        } catch (e: Exception) {
            Log.e("OpenCV", "Error loading CascadeClassifier", e)
        }
    }

    private fun setupCamera() {
        cameraView = findViewById(R.id.cameraPreview)
        cameraView.setCameraPermissionGranted() // Inform that permission has been granted
        cameraView.visibility = CameraBridgeViewBase.VISIBLE
        cameraView.setCvCameraViewListener(this)
        cameraView.enableView() // Start camera preview
    }

    override fun onResume() {
        super.onResume()
        if (::cameraView.isInitialized) {
            cameraView.enableView()
        }
    }

    override fun onPause() {
        super.onPause()
        if (::cameraView.isInitialized) {
            cameraView.disableView()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::cameraView.isInitialized) {
            cameraView.disableView()
        }
    }

    // CvCameraViewListener2 Methods
    override fun onCameraViewStarted(width: Int, height: Int) {
        Log.d("OpenCV", "Camera view started with resolution: $width x $height")
    }

    override fun onCameraViewStopped() {
        Log.d("OpenCV", "Camera view stopped")
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {
        val rgbaFrame = inputFrame?.rgba()
        val grayFrame = inputFrame?.gray()

        // Perform face detection
        val faces = MatOfRect()
        faceDetector?.detectMultiScale(
            grayFrame,
            faces,
            1.1, // Scale factor
            2,   // Min neighbors
            0,   // Flags
            Size(50.0, 50.0), // Min size
            Size() // Max size
        )

        // Draw rectangles around detected faces
        for (rect in faces.toArray()) {
            Imgproc.rectangle(
                rgbaFrame,
                Point(rect.x.toDouble(), rect.y.toDouble()),
                Point((rect.x + rect.width).toDouble(), (rect.y + rect.height).toDouble()),
                Scalar(255.0, 0.0, 0.0, 255.0), // Color (red)
                3 // Thickness
            )
        }

        return rgbaFrame ?: Mat()
    }
}
