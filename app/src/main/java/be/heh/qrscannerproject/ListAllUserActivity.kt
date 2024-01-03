package be.heh.qrscannerproject

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import be.heh.qrscannerproject.database.AppDatabase
import be.heh.qrscannerproject.database.User
import be.heh.qrscannerproject.databinding.ActivityListAllUserBinding

class ListAllUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListAllUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListAllUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        listAllUser()
    }

    fun xmlClickEvent(v: View) {
        when (v.id) {
            binding.btnRetour.id -> retour()
        }
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

            if (userList != null) {
                runOnUiThread {
                    val listView: ListView = findViewById(R.id.userListView)
                    val adapter = object : ArrayAdapter<User>(
                        this,
                        android.R.layout.simple_list_item_2,
                        android.R.id.text1,
                        userList
                    ) {
                        override fun getView(
                            position: Int,
                            convertView: View?,
                            parent: ViewGroup
                        ): View {
                            val view = super.getView(position, convertView, parent)
                            val text1: TextView = view.findViewById(android.R.id.text1)
                            val text2: TextView = view.findViewById(android.R.id.text2)

                            val user = getItem(position)
                            text1.text = user?.mail
                            text2.text = when (user?.role) {
                                "Suspended" -> "Suspendu"
                                else -> "Role: ${user?.role}"
                            }

                            return view
                        }
                    }

                    listView.adapter = adapter

                    listView.setOnItemClickListener { _, _, position, _ ->
                        // Lorsqu'un élément est cliqué, récupérez l'utilisateur sélectionné
                        val selectedUser = adapter.getItem(position)

                        // Vous pouvez lancer une autre activité pour la modification ici
                        // Ou afficher une boîte de dialogue pour la modification/suppression
                        showOptionsDialog(selectedUser)
                    }
                }
            }
        }
    }

    private fun showOptionsDialog(selectedUser: User?) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Options utilisateur")
        dialogBuilder.setMessage("Que voulez-vous faire avec cet utilisateur ?")

        dialogBuilder.setPositiveButton("Admin/User") { _, _ ->
            // Basculer le rôle de l'utilisateur (Admin <-> User)
            AsyncTask.execute {
                val db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java, "MyDataBase"
                ).build()
                val dao = db.userDao()

                try {
                    if (selectedUser != null) {
                        dao.updateUserRole(selectedUser.uid)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                // Rafraîchir la liste après la mise à jour
                listAllUser()
            }
        }
        dialogBuilder.setNeutralButton("Suspendre") { _, _ ->
            // Bascule entre "User", "Admin" et "Suspended"
            AsyncTask.execute {
                val db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java, "MyDataBase"
                ).build()
                val dao = db.userDao()
                if (selectedUser != null) {
                    dao.toggleUserRole(selectedUser.uid)
                }

                // Rafraîchir la liste après la mise à jour
                listAllUser()
            }
        }


        dialogBuilder.setNegativeButton("Supprimer") { _, _ ->
            // Supprimer l'utilisateur de la base de données
            AsyncTask.execute {
                val db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java, "MyDataBase"
                ).build()
                val dao = db.userDao()
                if (selectedUser != null) {
                    dao?.delete(selectedUser)
                }

                // Rafraîchir la liste après la suppression
                listAllUser()
            }
        }


        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
}
