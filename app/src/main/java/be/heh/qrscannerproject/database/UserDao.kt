package be.heh.qrscannerproject.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface UserDao {
    //select
    @Query("SELECT * FROM users")
    fun get(): List<User>
    @Query("SELECT * FROM users WHERE uid LIKE (:userIds)")
    fun getByIds(userIds: Int): User
    @Query("SELECT * FROM users WHERE mail LIKE (:mail)")
    fun getByMail(mail: String): User

    //insert, update, delete
    @Insert
    fun insertAll(vararg users: User)
    @Delete
    fun delete(user: User)
    @Update
    fun update(user: User)
}