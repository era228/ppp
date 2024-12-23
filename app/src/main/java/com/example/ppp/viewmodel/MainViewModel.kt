package com.example.ppp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ppp.model.Amount
import com.example.ppp.model.CbrResult
import com.example.ppp.model.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class MainViewModel(private val repository: Repository) : ViewModel() {

    val valute = MutableStateFlow(HashMap<String, CbrResult.Valuta>())
    val displayState = MutableStateFlow(DisplayState())

    init {
        setPeriod(Period.LastWeek)

        viewModelScope.launch(Dispatchers.IO) {
            repository.getDaily().collect {
                valute.value = it?.valute ?: HashMap()
            }
        }
    }

    data class DisplayState(
        val transactions: Flow<List<Transaction>> = MutableStateFlow(listOf()),
        val totalIncome: Flow<Amount> = MutableStateFlow(Amount()),
        val totalExpense: Flow<Amount> = MutableStateFlow(Amount())
    )

    enum class Period {
        LastWeek,
        LastMonth,
        AllTime;

        fun getDisplayName() = when (this) {
            LastWeek -> "Last week"
            LastMonth -> "Last month"
            AllTime -> "All time"
        }
    }

    fun setPeriod(period: Period) {
        val currentDate = Date().time
        val date = when (period) {
            Period.LastWeek -> Date(currentDate - 7 * 24 * 60 * 60 * 1000L)
            Period.LastMonth -> Date(currentDate - 30 * 24 * 60 * 60 * 1000L)
            Period.AllTime -> Date(0)
        }

        displayState.value = DisplayState(
            repository.getTransactionForDate(date),
            repository.getTotalIncomeAmount(date),
            repository.getTotalExpenseAmount(date)
        )
    }

    fun delete(transaction: Transaction) {
        viewModelScope.launch { repository.delete(transaction) }
    }

    fun upsert(transaction: Transaction) {
        viewModelScope.launch {
            repository.upsert(transaction)
        }
    }
}