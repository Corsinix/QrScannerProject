package be.heh.qrscannerproject.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "devices",
    indices = [Index("uid"),Index(value = ["produit"], unique = true)]
)
data class Devices(
    @ColumnInfo(name = "uid" ) @PrimaryKey(autoGenerate = true) var uid: Int = 0,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "marque") val marque: String?,
    @ColumnInfo(name = "produit") var produit: String?,
    @ColumnInfo(name = "web") val web: String?,
    @ColumnInfo(name = "emprunt") var emprunt: Boolean?,
    @ColumnInfo(name = "dernier_emprunt") var dernier_emprunt: String?
)