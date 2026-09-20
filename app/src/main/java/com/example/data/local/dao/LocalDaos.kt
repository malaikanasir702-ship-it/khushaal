package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profile WHERE id = 'me'")
    fun getUserProfile(): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(user: UserEntity)

    @Query("DELETE FROM user_profile")
    suspend fun clear()
}

@Dao
interface CashFlowDao {
    @Query("SELECT * FROM cash_flow WHERE id = 'current'")
    fun getCashFlow(): Flow<CashFlowEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCashFlow(cashFlow: CashFlowEntity)

    @Query("DELETE FROM cash_flow")
    suspend fun clear()
}

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getTransactions(): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<TransactionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM transactions")
    suspend fun clear()
}

@Dao
interface EnvelopeDao {
    @Query("SELECT * FROM envelopes")
    fun getEnvelopes(): Flow<List<EnvelopeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnvelopes(envelopes: List<EnvelopeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnvelope(envelope: EnvelopeEntity)

    @Query("DELETE FROM envelopes")
    suspend fun clear()
}

@Dao
interface KametiDao {
    @Query("SELECT * FROM kametis")
    fun getKametis(): Flow<List<KametiEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKametis(kametis: List<KametiEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKameti(kameti: KametiEntity)

    @Query("DELETE FROM kametis")
    suspend fun clear()
}

@Dao
interface GoalDao {
    @Query("SELECT * FROM goals")
    fun getGoals(): Flow<List<GoalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoals(goals: List<GoalEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: GoalEntity)

    @Query("DELETE FROM goals")
    suspend fun clear()
}

@Dao
interface DebtDao {
    @Query("SELECT * FROM debts")
    fun getDebts(): Flow<List<DebtEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDebts(debts: List<DebtEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDebt(debt: DebtEntity)

    @Query("DELETE FROM debts")
    suspend fun clear()
}

@Dao
interface BillDao {
    @Query("SELECT * FROM bills")
    fun getBills(): Flow<List<BillEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBills(bills: List<BillEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBill(bill: BillEntity)

    @Query("DELETE FROM bills")
    suspend fun clear()
}

@Dao
interface LockerDao {
    @Query("SELECT * FROM locker WHERE id = 'locker'")
    fun getLocker(): Flow<LockerEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocker(locker: LockerEntity)

    @Query("DELETE FROM locker")
    suspend fun clear()
}

@Dao
interface OrderDao {
    @Query("SELECT * FROM customer_orders")
    fun getOrders(): Flow<List<OrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrders(orders: List<OrderEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Query("DELETE FROM customer_orders")
    suspend fun clear()
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getNotifications(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :notifId")
    suspend fun markAsRead(notifId: String)

    @Query("DELETE FROM notifications")
    suspend fun clear()
}

@Dao
interface CoachDao {
    @Query("SELECT * FROM coach_messages ORDER BY timestamp ASC")
    fun getMessages(): Flow<List<CoachMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<CoachMessageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: CoachMessageEntity)

    @Query("DELETE FROM coach_messages")
    suspend fun clear()
}

@Dao
interface ProsperityDao {
    @Query("SELECT * FROM prosperity WHERE id = 'prosperity'")
    fun getProsperity(): Flow<ProsperityEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProsperity(prosperity: ProsperityEntity)

    @Query("DELETE FROM prosperity")
    suspend fun clear()
}
