package be.heh.qrscannerproject.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "devices",
    indices = [Index(value = ["uid"], unique = true), Index("type"), Index("marque"), Index("produit"), Index("web"), Index("emprunt"), Index("dernier_emprunt") ],
    foreignKeys  = [ForeignKey(entity = User::class, parentColumns = ["mail"], childColumns = ["dernier_emprunt"], onDelete = ForeignKey.RESTRICT)]
)
data class Devices(
    @ColumnInfo(name = "uid" ) @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "marque") val marque: String?,
    @ColumnInfo(name = "produit") val produit: String?,
    @ColumnInfo(name = "web") val web: String?,
    @ColumnInfo(name = "emprunt") val emprunt: Boolean?,
    @ColumnInfo(name = "dernier_emprunt") val dernier_emprunt: String?
) {
    enum class Type {
        Smatphone, Tablet
    }
}