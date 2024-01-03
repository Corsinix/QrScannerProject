package be.heh.qrscannerproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import be.heh.qrscannerproject.databinding.ActivityMenuPrincipalBinding

class MenuPrincipal : AppCompatActivity() {
    private lateinit var binding: ActivityMenuPrincipalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)
        binding = ActivityMenuPrincipalBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val userRole = intent.getSerializableExtra("userRole") as String?
        reveal_btnConsultUsers_for_admin(userRole)
    }
    fun xmlClickEvent(v: View) {
        when (v.id) {
            binding.btnConsultProducts.id -> consultProducts()
            binding.btnConsultUsers.id -> consultUsers()
            binding.btnScanAdd.id -> scanAdd()
            binding.btnScanEmprunt.id -> scanEmprunt()
        }
    }

    private fun scanEmprunt() {
        TODO("Not yet implemented")
    }

    private fun scanAdd() {
        TODO("Not yet implemented")
    }

    private fun consultUsers() {
        val intent = Intent(this, ListAllUserActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun consultProducts() {
        TODO("Not yet implemented")
    }

    private fun reveal_btnConsultUsers_for_admin(userRole: String?) {
        if (userRole == "SuperAdmin"){
            binding.btnConsultUsers.visibility = View.VISIBLE
        }
    }
}