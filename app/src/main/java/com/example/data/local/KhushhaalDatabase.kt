package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.*
import com.example.data.local.entities.*

@Database(
    entities = [
        UserEntity::class,
        CashFlowEntity::class,
        TransactionEntity::class,
        EnvelopeEntity::class,
        KametiEntity::class,
        GoalEntity::class,
        DebtEntity::class,
        BillEntity::class,
        LockerEntity::class,
        OrderEntity::class,
        NotificationEntity::class,
        CoachMessageEntity::class,
        ProsperityEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class KhushhaalDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun cashFlowDao(): CashFlowDao
    abstract fun transactionDao(): TransactionDao
    abstract fun envelopeDao(): EnvelopeDao
    abstract fun kametiDao(): KametiDao
    abstract fun goalDao(): GoalDao
    abstract fun debtDao(): DebtDao
    abstract fun billDao(): BillDao
    abstract fun lockerDao(): LockerDao
    abstract fun orderDao(): OrderDao
    abstract fun notificationDao(): NotificationDao
    abstract fun coachDao(): CoachDao
    abstract fun prosperityDao(): ProsperityDao

    companion object {
        private const val DB_NAME = "khushhaal_local_cache.db"

        @Volatile
        private var INSTANCE: KhushhaalDatabase? = null

        fun getInstance(context: Context): KhushhaalDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    KhushhaalDatabase::class.java,
                    DB_NAME
                )
                .fallbackToDestructiveMigration()
                .build()
                .also { INSTANCE = it }
            }
        }
    }
}
