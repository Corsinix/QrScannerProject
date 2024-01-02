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
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }
    fun xmlClickEvent(v: View) {
        when (v.id) {
        // R.id.bt_Ecrire_main -> {
            binding.btEcrireMain.id -> ecrire()
            binding.btLireMain.id -> lire()
            binding.btLireLoginMain.id -> lireCeLogin(binding.etChildrenLogin.text.toString())
        }
    }

    private fun lireCeLogin(l: String) {
        AsyncTask.execute {
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "MyDataBase"
            ).build()
            val dao = db.userDao()
            val dbL = dao?.getByLogin(l)
            var uL = User(dbL?.uid?:0, dbL?.login?:"INDEFINI", dbL?.mail?:"INDEFINI",
                dbL?.password?:"INDEFINI", dbL?.role?: "User")
            binding.tvLoginMain.setTextColor(Color.RED)
            binding.tvMainPwd.setTextColor(Color.RED)
            binding.tvEmailMain.setTextColor(Color.RED)
            binding.tvLoginMain.text = "LOGIN : " + uL.login
            binding.tvMainPwd.text = "PASSWORD : " + uL.password
            binding.tvEmailMain.text = "EMAIL : " + uL.mail
        }
    }

    private fun lire() {
        AsyncTask.execute {
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "MyDataBase"
            ).build()
            val dao = db.userDao()
            val liste = dao.get()
            liste.forEach { item -> Log.i("READ", item.toString()) }
        }
    }

    private fun ecrire() {
            var err : String = ""
            val u = User(0, binding.etChildrenLogin.text.toString(), binding.etChildrenEmail.text.toString(),
                binding.etChildrenPwd.text.toString(), "SuperAdmin")

            AsyncTask.execute() {
                try {
                    val db = Room.databaseBuilder(
                        applicationContext,
                        AppDatabase::class.java, "MyDataBase"
                    ).build()
                    val dao = db.userDao()
                    val u1 = User(0, u.login, u.mail, u.password, "User")
                    Log.i("Lire", "Ratioooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo")
                    dao.insertAll(u1)
            }
            catch (e: Exception){
                err = e.message.toString()
            }
        }

        try {
            if (err == ""){
                Toast.makeText(this,"OK",Toast.LENGTH_LONG).show()}
            else {
                Toast.makeText(this,err,Toast.LENGTH_LONG).show()}
        }
        catch (e: Exception){
            Toast.makeText(this,e.message.toString(),Toast.LENGTH_LONG).show()
        }
    }

    private fun lireBDD(){
        AsyncTask.execute{
            val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "user-database").build()
            val userDao = db.userDao()
            val devicesDao = db.devicesDao()
            val u1 = User(1, "admin", "admin@gmail.com", "1234", "Admin")
            val u2 = User(2, "user", "user@gmail.com", "1234", "User")

            val d1 = Devices(1, "PC", "HP",  "pc hp", "https://www.hp.com", false, "")
            val d2 = Devices(2, "PC", "DELL", "pc dell", "https://www.dell.com", false, "")
            val d3 = Devices(3, "PC", "LENOVO", "pc lenovo", "https://www.lenovo.com", false, "")
            devicesDao.insertAll(d1, d2, d3)
            userDao.insertAll(u1, u2)
        }
    }
}