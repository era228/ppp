package com.example.ppp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ppp.Utils
import com.example.ppp.model.Amount
import com.example.ppp.model.Transaction
import com.example.ppp.ui.components.AppButton
import com.example.ppp.ui.theme.income
import com.example.ppp.ui.theme.my
import com.example.ppp.viewmodel.MainViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import kotlin.math.absoluteValue

@Composable
fun MainScreen(viewModel: MainViewModel, addOrEdit: (Transaction?) -> Unit) {

    val displayState = viewModel.displayState.collectAsState().value
    val transactions = displayState.transactions.collectAsState(listOf())
    val totalIncome = displayState.totalIncome.collectAsState(Amount())
    val totalExpense = displayState.totalExpense.collectAsState(Amount())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { addOrEdit(null) }) {
                Icon(Icons.Default.Add, "Add")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Spacer(Modifier.width(16.dp))
                for (period in MainViewModel.Period.entries) {
                    AppButton(
                        text = period.getDisplayName(),
                        onClick = { viewModel.setPeriod(period) }
                    )
                    Spacer(Modifier.width(16.dp))
                }
            }

            Row(
                modifier = Modifier.padding(top = 16.dp)
            ) {
                TotalItem(true, totalIncome.value.value)
                Spacer(Modifier.width(64.dp))
                TotalItem(false, totalExpense.value.value)
            }

            Row {
                val valute by viewModel.valute.collectAsState()
                val valuteCodes = listOf("USD", "EUR", "GBP")
                for (code in valuteCodes) {
                    valute[code]?.let {
                        Text(
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(
                                    start = 10.dp,
                                    top = 10.dp,
                                )
                                .background(
                                    color = my,
                                    shape = RoundedCornerShape(2.dp)
                                )
                                .clip(RoundedCornerShape(2.dp)),
                            text = "$code: ${it.value}",
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(transactions.value) { note ->
                    TransactionItem(note, viewModel, addOrEdit)
                }
            }
        }
    }
}

@Composable
fun TotalItem(
    isIncome: Boolean,
    value: Double
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val color = if (isIncome) income else Color.Red
        Text(
            fontSize = 24.sp,
            modifier = Modifier,
            text = if (isIncome) "Income" else "Expense",
            color = color
        )
        Text(
            fontSize = 32.sp,
            modifier = Modifier,
            text = Utils.formatAmount(value.absoluteValue),
            color = color
        )
    }
}

@Composable
fun TransactionItem(
    transaction: Transaction,
    viewModel: MainViewModel,
    edit: (Transaction?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable {
                edit(transaction)
            }
            .padding(16.dp)
    ) {
        Text(if (transaction.amount > 0) "+" else "-")
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.name, style = MaterialTheme.typography.titleMedium,
                color = Color.Red,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = Utils.formatAmount(transaction.amount.absoluteValue), style = MaterialTheme.typography.bodySmall)

            val format = SimpleDateFormat.getDateTimeInstance()
            Text(format.format(transaction.date))
        }

        IconButton(onClick = { viewModel.delete(transaction) }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                modifier = Modifier.size(36.dp)
            )
        }
    }
}