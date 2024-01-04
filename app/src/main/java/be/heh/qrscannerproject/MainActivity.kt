package be.heh.qrscannerproject

import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import be.heh.qrscannerproject.database.AppDatabase
import be.heh.qrscannerproject.database.Devices
import be.heh.qrscannerproject.database.User
import be.heh.qrscannerproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    fun xmlClickEvent(v: View) {
        when (v.id) {
            binding.btEcrireMain.id -> signIn()
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
            val email = binding.etChildrenEmail.text.toString()
            val password = binding.etChildrenPwd.text.toString()
            val user = dao?.getByMail(email)

            runOnUiThread {
                if (user != null) {
                    if (user.role == "Suspended") {
                        // Utilisateur suspendu
                        Toast.makeText(
                            this,
                            "Votre compte est suspendu. Veuillez contacter l'administrateur.",
                            Toast.LENGTH_LONG
                        ).show()
                    } else if (user.password == password) {
                        // Connexion réussie
                        val intent = Intent(this, MenuPrincipal::class.java)
                        intent.putExtra("userRole", user.role)
                        startActivity(intent)
                        finish()
                    } else {
                        // Mot de passe incorrect
                        binding.tvEmailMain.setTextColor(Color.RED)
                        binding.tvMainPwd.setTextColor(Color.RED)
                        binding.tvMainPwd.text = "Mot de passe incorrect"
                        binding.tvEmailMain.text = ""
                    }
                } else {
                    // Utilisateur non trouvé
                    binding.tvEmailMain.setTextColor(Color.RED)
                    binding.tvMainPwd.setTextColor(Color.RED)
                    binding.tvMainPwd.text = "Utilisateur non trouvé"
                    binding.tvEmailMain.text = ""
                }
            }
        }
    }

    private fun signIn() {
        val email = binding.etChildrenEmail.text.toString()
        val password = binding.etChildrenPwd.text.toString()

        if (isValidEmail(email) && isValidPassword(password)) {
            AsyncTask.execute {
                try {
                    val db = Room.databaseBuilder(
                        applicationContext,
                        AppDatabase::class.java, "MyDataBase"
                    ).build()
                    val userDao = db.userDao()

                    // Vérifier s'il s'agit du premier utilisateur
                    val isFirstUser = userDao.getCount() == 0

                    val userRole = if (isFirstUser) {
                        "SuperAdmin" // Si c'est le premier utilisateur, définir comme superadmin
                    } else {
                        "User" // Sinon, définir comme utilisateur normal
                    }

                    val user = User(0, email, password, userRole)
                    userDao.insertAll(user)

                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Compte créé avec succès!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this, "Ce compte existe", Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else {
            Toast.makeText(
                this,
                "Email ou mot de passe invalide (minimum 4 caractères)",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun isValidEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 4
    }

    private fun createSuperAdmin() {
        AsyncTask.execute {
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
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun createInitialDevices() {
        AsyncTask.execute {
            try {
                val db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java, "MyDataBase"
                ).build()

                val dao = db.devicesDao()

                val d1 = Devices(0, "Ordinateur", "Dell", "Latitude 5400", "https://www.dell.com/fr-be", false, "")
                val d2 = Devices(0, "Ordinateur", "HP", "EliteBook 840 G7", "https://www.hp.com", false, "")
                val d3 = Devices(0, "Smartphone", "Samsung", "Galaxy S21", "https://www.samsung.com", false, "")

                dao.insertAll(d1, d2, d3)
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}