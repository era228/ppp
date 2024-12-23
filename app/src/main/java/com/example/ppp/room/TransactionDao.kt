package com.example.ppp.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.ppp.model.Amount
import com.example.ppp.model.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface TransactionDao {
    @Upsert
    suspend fun upsert(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)

    @Query("SELECT * FROM `Transaction` WHERE date > :date")
    fun getTransactionForDate(date: Date): Flow<List<Transaction>>

    @Query("SELECT sum(amount) as value FROM `Transaction` WHERE date > :date AND amount > 0")
    fun getTotalIncomeAmount(date: Date): Flow<Amount>

    @Query("SELECT sum(amount) as value FROM `Transaction` WHERE date > :date AND amount < 0")
    fun getTotalExpenseAmount(date: Date): Flow<Amount>
}