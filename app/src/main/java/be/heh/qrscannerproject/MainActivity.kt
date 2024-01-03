package be.heh.qrscannerproject

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.room.Room
import be.heh.qrscannerproject.database.AppDatabase
import be.heh.qrscannerproject.database.Devices
import be.heh.qrscannerproject.database.User
import be.heh.qrscannerproject.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    // private lateinit var binding: ActivityMainBinding
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        createSuperAdmin()
    }



    fun xmlClickEvent(v: View) {
        when (v.id) {
        // R.id.bt_Ecrire_main -> {
            binding.btEcrireMain.id -> sing_in()
            binding.btLireMain.id -> login()
        }
    }


    private fun login() {
        AsyncTask.execute {
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "MyDataBase"
            ).build()
            val dao = db.userDao()
            val dbL = dao?.getByMail(binding.etChildrenEmail.text.toString())
            var uL = User(dbL?.uid?:0, dbL?.mail?:"INDEFINI",
                dbL?.password?:"INDEFINI", dbL?.role?:"INDEFINI")
            if (uL.password == binding.etChildrenPwd.text.toString()){
                val intent = Intent(this, MenuPrincipal::class.java)
                intent.putExtra("userRole", uL.role)
                startActivity(intent)
                finish()
            }
            else{
                binding.tvEmailMain.setTextColor(Color.RED)
                binding.tvMainPwd.setTextColor(Color.RED)
                binding.tvMainPwd.text = "PASSWORD : " + uL.password
                binding.tvEmailMain.text = "EMAIL : " + uL.mail
            }
        }
    }

    private fun sing_in() {
            var err : String = ""
            val u = User(0, binding.etChildrenEmail.text.toString(),
                binding.etChildrenPwd.text.toString(), "SuperAdmin")
            AsyncTask.execute() {
                try {
                    val db = Room.databaseBuilder(
                        applicationContext,
                        AppDatabase::class.java, "MyDataBase"
                    ).build()
                    val dao = db.userDao()
                    val u1 = User(0, u.mail, u.password, "User")
                    dao.insertAll(u1)
            }
            catch (e: Exception){
                err = e.message.toString()
            }
        }
        try {
        }
        catch (e: Exception){
            Toast.makeText(this,e.message.toString(),Toast.LENGTH_LONG).show()
        }
    }
    private fun createSuperAdmin() {
        AsyncTask.execute() {
            try {
                val db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java, "MyDataBase"
                ).build()
                val dao = db.userDao()
                val u = User(0, "SuperAdmin@gmail.com", "AdminSuper", "SuperAdmin")
                val u2 = User(0, "Admin@gmail.com", "Admin", "Admin")
                val u3 = User(0, "gmail@gmail.com", "User", "User")
                dao.insertAll(u, u2, u3)
                val d1 = Devices(0, "Ordinateur", "Dell", "Latitude 5400", "https://www.dell.com/fr-be", true,"Admin@gmail.com")
                val d2 = Devices(0, "Ordinateur", "Dell", "Latitude 5400", "https://www.dell.com/fr-be", true,"gmail@gmail.com")
                val dao2 = db.devicesDao()
                dao2.insertAll(d1, d2)
            } catch (e: Exception) {
        Log.d("MainActivity", e.message.toString())
            }
        }
    }
}