package be.heh.qrscannerproject

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.room.Room
import be.heh.qrscannerproject.database.AppDatabase
import be.heh.qrscannerproject.database.User
import be.heh.qrscannerproject.databinding.ActivityListAllUserBinding

class ListAllUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListAllUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_all_user)
        binding = ActivityListAllUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        listAllUser()
    }
    fun xmlClickEvent(v: View) {
        when (v.id) {
            binding.btnRetour.id -> retour()
            binding.btnSupprimer.id -> supprimer()
            binding.btnToggleOP.id -> toggleOP()
        }
    }

    private fun toggleOP() {
        TODO("Not yet implemented")
    }

    private fun supprimer() {
        TODO("Not yet implemented")
    }

    private fun retour() {
        val intent = Intent(this, MenuPrincipal::class.java)
        intent.putExtra("userRole", "SuperAdmin")
        startActivity(intent)
        finish()
    }

    private fun listAllUser() {
        AsyncTask.execute {
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "MyDataBase"
            ).build()
            val dao = db.userDao()
            val userList = dao?.getwithoutSuperAdmin()

            runOnUiThread {
                displayUserList(userList)
            }
        }
    }

    private fun displayUserList(userList: List<User>?) {
        if (userList != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, userList)
            val listView: ListView = findViewById(R.id.userListView)
            listView.adapter = adapter
        }
    }
}