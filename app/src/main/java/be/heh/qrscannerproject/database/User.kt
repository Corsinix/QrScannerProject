package be.heh.qrscannerproject.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users",
        indices = [Index(value = ["mail"], unique = true), Index("uid"), ]
)
data class User(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "login") val login: String?,
    @ColumnInfo(name = "mail") val mail: String?,
    @ColumnInfo(name = "password") val password: String?,
    @ColumnInfo(name = "isadmin") val admin: Boolean?
){
    enum class Role {
        ADMIN, USER
    }
}

