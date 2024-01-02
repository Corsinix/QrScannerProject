package be.heh.qrscannerproject.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users",
        indices = [Index(value = ["mail"], unique = true), Index("uid"), ]
)
data class User(
    @ColumnInfo(name = "uid") @PrimaryKey(autoGenerate = true) val uid: Int=0,
    @ColumnInfo(name = "login") val login: String?,
    @ColumnInfo(name = "mail") val mail: String?,
    @ColumnInfo(name = "password") val password: String?,
    @ColumnInfo(name = "role") val role: String?
)

