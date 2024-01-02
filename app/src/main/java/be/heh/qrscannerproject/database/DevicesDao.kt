package be.heh.qrscannerproject.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DevicesDao {
    //select
    @Query("SELECT * FROM devices")
    fun getAll(): List<Devices>
    @Query("SELECT * FROM devices WHERE uid LIKE (:devicesIds)")
    fun loadAllByIds(devicesIds: Int): Devices
    @Query("SELECT * FROM devices WHERE produit LIKE (:produit)")
    fun findByProduit(produit: String): Devices
    @Query("SELECT * FROM devices WHERE emprunt LIKE (:emprunt)")
    fun findByEmprunt(emprunt: Boolean): Devices
    @Query("SELECT * FROM devices WHERE dernier_emprunt LIKE (:dernier_emprunt) AND emprunt LIKE type")
    fun findByDernierEmprunt(dernier_emprunt: String): Devices
    @Query("SELECT * FROM devices ORDER BY :sort ASC")
    fun getAllSorted(sort: String): List<Devices>
    @Query("SELECT * FROM devices WHERE type LIKE (:type)")
    fun findByType(type: String): Devices
    @Query("SELECT * FROM devices WHERE marque LIKE (:marque)")
    fun findByMarque(marque: String): Devices
    @Query("SELECT * FROM devices WHERE web LIKE (:web)")
    fun findByWeb(web: String): Devices

    //insert, update, delete
    @Insert
    fun insertAll(vararg devices: Devices)
    @Delete
    fun delete(devices: Devices)
    @Update
    fun update(devices: Devices)

}