package be.heh.qrscannerproject

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import be.heh.qrscannerproject.database.AppDatabase
import be.heh.qrscannerproject.database.Devices
import be.heh.qrscannerproject.databinding.ActivityMenuPrincipalBinding
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONException
import org.json.JSONObject

class MenuPrincipal : AppCompatActivity() {
    private lateinit var binding: ActivityMenuPrincipalBinding
    private var qrScanIntegrator: IntentIntegrator? = null
    private var scannedData: String? = null
    private var scannedDevice: Devices? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuPrincipalBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val userRole = intent.getSerializableExtra("userRole") as String?
        reveal_btnConsultUsers_for_admin(userRole)
        reveal_btnScanAdd_for_admin(userRole)
    }

    fun xmlClickEvent(v: View) {
        when (v.id) {
            binding.btnConsultProducts.id -> consultProducts()
            binding.btnConsultUsers.id -> consultUsers()
            binding.btnScanAdd.id -> scanAdd()
        }
    }

    private fun scanAdd() {
        setupScanner()
        performAction()
    }

    private fun setupScanner() {
        qrScanIntegrator?.setOrientationLocked(false)
        qrScanIntegrator = IntentIntegrator(this)
    }

    private fun performAction() {
        qrScanIntegrator?.initiateScan()
    }

    private fun showConfirmationDialog(device: Devices) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmation")
            .setMessage("Voulez-vous ajouter ces données dans la base de données?")
            .setPositiveButton("Oui") { _, _ ->
                // Ajoutez vos données dans la base de données ici
                addOrUpdateProduct(device)
                Toast.makeText(this, "Données ajoutées avec succès!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Non") { _, _ ->
                // Réinitialisez les données si nécessaire
                scannedData = null
            }
            .show()
    }

    private fun addOrUpdateProduct(device: Devices) {
        AsyncTask.execute {
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "MyDataBase"
            ).build()
            val dao = db.devicesDao()

            // Vérifier si le produit existe déjà dans la base de données
            val existingDevice = device.produit?.let { dao.findByProduit(it) }

            if (existingDevice != null) {
                // Si le produit existe, mettre à jour l'état d'emprunt
                existingDevice.produit = device.produit
                updateProductEmpruntState(existingDevice)
            } else {
                // Si le produit n'existe pas, l'ajouter
                dao.insertAll(device)
            }
        }
    }

    private fun updateProductEmpruntState(device: Devices) {
        AsyncTask.execute {
            val emprunteur = binding.etEmprunteur.text.toString()
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "MyDataBase"
            ).build()
            val dao = db.devicesDao()
            if (device.emprunt == true) {
                device.emprunt = false
            } else {
                device.emprunt = true
                device.dernier_emprunt = emprunteur
            }
            dao.update(device)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, getString(R.string.result_not_found), Toast.LENGTH_LONG).show()
            } else {
                try {
                    val obj = JSONObject(result.contents)
                    scannedData = result.contents
                    scannedDevice = Devices(
                        0,
                        obj.getString("type"),
                        obj.getString("marque"),
                        obj.getString("produit"),
                        obj.getString("web"),
                        false,
            "")
                    showConfirmationDialog(scannedDevice!!)
                }
                catch (e: JSONException) {
                    Log.e("QRCodeScanner", "Error: " + e.message)
                    Toast.makeText(this, result.contents, Toast.LENGTH_LONG).show()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun consultUsers() {
        val intent = Intent(this, ListAllUserActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun consultProducts() {
        val userRole = intent.getSerializableExtra("userRole") as String?
        val intent = Intent(this, ListAllDevicesActivity::class.java)
        intent.putExtra("userRole", userRole)
        startActivity(intent)
        finish()
    }

    private fun reveal_btnConsultUsers_for_admin(userRole: String?) {
        if (userRole == "SuperAdmin") {
            binding.btnConsultUsers.visibility = View.VISIBLE
        }
    }

    private fun reveal_btnScanAdd_for_admin(userRole: String?) {
        if (userRole == "SuperAdmin" || userRole == "Admin") {
            binding.btnScanAdd.visibility = View.VISIBLE
            binding.etEmprunteur.visibility = View.VISIBLE
        }
    }

}
