package be.heh.qrscannerproject.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Devices(
    @ColumnInfo(name = "reference" ) @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "marque") val marque: String?,
    @ColumnInfo(name = "web") val web: String?
)