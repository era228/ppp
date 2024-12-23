package com.example.ppp.viewmodel

import com.example.ppp.api.CbrService
import com.example.ppp.model.Amount
import com.example.ppp.model.CbrResult
import com.example.ppp.model.Transaction
import com.example.ppp.room.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import java.util.Date

class Repository(private val db: AppDatabase) {

    fun getDaily(): Flow<CbrResult?> {
        return flow {
            CbrService.getInstance().getDaily().body()
        }
    }

    fun getTransactionForDate(date: Date): Flow<List<Transaction>> {
        return db.transactionDao.getTransactionForDate(date)
    }

    fun getTotalIncomeAmount(date: Date): Flow<Amount> {
        return db.transactionDao.getTotalIncomeAmount(date)
    }

    fun getTotalExpenseAmount(date: Date): Flow<Amount> {
        return db.transactionDao.getTotalExpenseAmount(date)
    }

    suspend fun upsert(transaction: Transaction) {
        db.transactionDao.upsert(transaction)
    }

    suspend fun delete(transaction: Transaction) {
        db.transactionDao.delete(transaction)
    }
}