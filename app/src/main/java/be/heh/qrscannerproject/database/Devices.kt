package be.heh.qrscannerproject.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Devices(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "marque") val marque: String?,
    @ColumnInfo(name = "web") val web: String?
)