package be.heh.qrscannerproject.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "mail") val mail: String?,
    @ColumnInfo(name = "password") val password: String?,
    @ColumnInfo(name = "isadmin") val admin: Boolean?
)

