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
    }
    fun xmlClickEvent(v: View) {
        when (v.id) {
        // R.id.bt_Ecrire_main -> {
            binding.btEcrireMain.id -> sing_in()
            binding.btLireMain.id -> login()
            binding.btLireLoginMain.id -> ad_admin(binding.etChildrenEmail.text.toString())
        }
    }

    private fun ad_admin(mail: String) {
        AsyncTask.execute {
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "MyDataBase"
            ).build()
            val dao = db.userDao()
            val dbL = dao?.getByMail(mail)
            var uL = User(dbL?.uid?:0, dbL?.mail?:"INDEFINI",
                dbL?.password?:"INDEFINI", dbL?.role?: "User")
            binding.tvEmailMain.setTextColor(Color.RED)
            binding.tvMainPwd.setTextColor(Color.RED)
            binding.tvMainPwd.text = "PASSWORD : " + uL.password
            binding.tvEmailMain.text = "EMAIL : " + uL.mail
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
                dbL?.password?:"INDEFINI", dbL?.role?: "User")
            if (uL.password == binding.etChildrenPwd.text.toString()){
                val intent = Intent(this, ScannerActivity::class.java)
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
            if (err == ""){
                Toast.makeText(this,"User already exist",Toast.LENGTH_LONG).show()}
            else {
                Toast.makeText(this,err,Toast.LENGTH_LONG).show()}
        }
        catch (e: Exception){
            Toast.makeText(this,e.message.toString(),Toast.LENGTH_LONG).show()
        }
    }
}