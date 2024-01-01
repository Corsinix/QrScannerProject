package be.heh.qrscannerproject.database

import androidx.room.*

@Database(entities = [User::class, Devices::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun devicesDao(): DevicesDao
}