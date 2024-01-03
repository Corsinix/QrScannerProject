package be.heh.qrscannerproject

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.room.Room
import be.heh.qrscannerproject.database.AppDatabase
import be.heh.qrscannerproject.databinding.ActivityListAllDevicesBinding
import be.heh.qrscannerproject.databinding.ActivityListAllUserBinding

class ListAllDevicesActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_all_devices)
        listView = findViewById(R.id.listViewDevices)
        val userRole = intent.getSerializableExtra("userRole") as String?
        listAllDevices()
    }


    private fun listAllDevices() {
        AsyncTask.execute {
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "MyDataBase"
            ).build()

            val dao = db.devicesDao()
            val devicesList = dao.getAll()

            runOnUiThread {
                // Adapter to display devices in the ListView
                val adapter = ArrayAdapter(
                    this@ListAllDevicesActivity,
                    android.R.layout.simple_list_item_1,
                    devicesList.map {
                        val empruntText = if (it.emprunt == true) "Emprunté par : " else "Non emprunté: "
                        "${it.type} - ${it.marque} - ${it.produit} - ${it.web} \n$empruntText ${it.dernier_emprunt}"
                    }
                )

                listView.adapter = adapter

                // Définir le clic sur un élément de la liste
                listView.setOnItemClickListener { _, _, position, _ ->
                    val websiteUrl = devicesList[position].web
                    showConfirmationDialog(websiteUrl)
                }
            }
        }
    }


    private fun showConfirmationDialog(websiteUrl: String?) {
        AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("Voulez-vous ouvrir le site web dans le navigateur?")
            .setPositiveButton("Oui") { _, _ ->
                openBrowser(websiteUrl)
            }
            .setNegativeButton("Non") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun openBrowser(websiteUrl: String?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(websiteUrl)
        // Spécifier le package de l'application Chrome
        intent.`package` = "com.android.chrome"
        startActivity(intent)
    }


    fun xmlClickEvent(v: View) {
        when (v.id) {
            R.id.btnRetour -> backToMain()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list_devices, menu)
        return true
    }

    private fun backToMain() {
        val userRole = intent.getSerializableExtra("userRole") as String?
        // Retour à l'activité principale avec conservation de l'email du Super Admin
        val intent = Intent(this@ListAllDevicesActivity, MenuPrincipal::class.java)
        if ( userRole == "SuperAdmin"){
            intent.putExtra("userRole", "SuperAdmin") }
        else if (userRole == "Admin"){
            intent.putExtra("userRole", "Admin") }
        else if (userRole == "User"){
            intent.putExtra("userRole", "User")}
        startActivity(intent)
        finish()
    }
}