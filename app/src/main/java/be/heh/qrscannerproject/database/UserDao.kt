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
    @Query("SELECT * FROM users WHERE role != 'SuperAdmin'")
    fun getwithoutSuperAdmin(): List<User>
    @Query("SELECT COUNT(*) FROM users")
    fun getCount(): Int

    //insert, update, delete
    @Insert
    fun insertAll(vararg users: User)
    @Delete
    fun delete(user: User)
    @Update
    fun update(user: User)
    @Query("UPDATE users SET role = CASE WHEN role = 'Admin' THEN 'User' ELSE 'Admin' END WHERE uid = :userId")
    fun updateUserRole(userId: Int)
    @Query("UPDATE users SET role = CASE " +
            "WHEN role = 'User' THEN 'Suspended' " +
            "WHEN role = 'Admin' THEN 'Suspended' " +
            "WHEN role = 'Suspended' THEN 'User' END " +
            "WHERE uid = :userId")
    fun toggleUserRole(userId: Int)

}