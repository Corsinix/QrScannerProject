package be.heh.qrscannerproject.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DevicesDao {
    @Query("SELECT * FROM devices")
    fun getAll(): List<Devices>

    @Query("SELECT * FROM devices WHERE reference IN (:devicesIds)")
    fun loadAllByIds(devicesIds: IntArray): List<Devices>

    @Insert
    fun insertAll(vararg devices: Devices)

    @Delete
    fun delete(devices: Devices)

}