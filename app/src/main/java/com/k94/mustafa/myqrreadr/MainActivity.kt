package com.k94.mustafa.myqrreadr

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.Manifest.permission_group
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.TabWidget
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_main.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    lateinit var svQRcode: SurfaceView
    lateinit var tvQRcode: TextView

    lateinit var detector: BarcodeDetector
    lateinit var cameraSource: CameraSource //to link surfaceView with barcodeDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        svQRcode = findViewById(R.id.SVBarcode)
        tvQRcode = findViewById(R.id.TVBarcode)

        detector = BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build() //to select a format
        detector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}

            override fun receiveDetections(detections: Detector.Detections<Barcode>?) {

                var QRcodes = detections?.detectedItems
                if (QRcodes!!.size() > 0) {
                    TVBarcode.post { }
                    TVBarcode.text = QRcodes.valueAt(0).displayValue
                }
            }

        })

        cameraSource = CameraSource.Builder(this, detector).setRequestedPreviewSize(1024, 768)
                .setRequestedFps(25f).setAutoFocusEnabled(true).build()

        SVBarcode.holder.addCallback(object : SurfaceHolder.Callback2 {
            override fun surfaceRedrawNeeded(holder: SurfaceHolder?) {}

            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, widget: Int, height: Int) {}

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                cameraSource.stop()
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                if (ContextCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                    cameraSource.start(holder)
                else
                    ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.CAMERA), 123) //any numbers
                //this condition if the user don't give a permission,so it will call onRequestPermissionsResult
            }
        })
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 123) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                cameraSource.start(SVBarcode.holder)
            else
                Toast.makeText(this, "Scanner won't work  without permission", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        detector.release()
        cameraSource.stop()
        cameraSource.release()
    }
}
