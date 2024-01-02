package be.heh.qrscannerproject

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import be.heh.qrscannerproject.databinding.ActivityScannerBinding
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONException
import org.json.JSONObject
import kotlin.math.log

class ScannerActivity : AppCompatActivity() {
        private lateinit var binding: ActivityScannerBinding
        private var qrScanIntegrator: IntentIntegrator? = null
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_scanner)
            binding = ActivityScannerBinding.inflate(layoutInflater)
            val view = binding.root
            setContentView(view)
            setupScanner()
            binding.btnScan.setOnClickListener { xmlClickEvent(it) }
        }
        private fun setupScanner() {
            qrScanIntegrator = IntentIntegrator(this)
        }

        fun xmlClickEvent(v: View) {
            Log.d("ScannerActivity", "Button Clicked")
            when (v) {
                binding.btnScan -> performAction()
            }
        }

        private fun performAction() {
            // Code to perform action when button is clicked.
            qrScanIntegrator?.initiateScan()
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                // If QRCode has no data.
                if (result.contents == null) {
                    Toast.makeText(this, getString(R.string.result_not_found), Toast.LENGTH_LONG).show()
                } else {
                    // If QRCode contains data.
                    try {
                        // Converting the data to json format
                        val obj = JSONObject(result.contents)

                        // Show values in UI.
                        binding.name.text = obj.getString("name")
                        binding.siteName.text = obj.getString("value")

                    } catch (e: JSONException) {
                        e.printStackTrace()

                        // Data not in the expected format. So, whole object as toast message.
                        Toast.makeText(this, result.contents, Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
}