package com.example.ppp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ppp.model.Transaction
import com.example.ppp.ui.theme.income
import com.example.ppp.viewmodel.MainViewModel
import java.util.Date
import kotlin.math.absoluteValue

@Composable
fun AddOrEditScreen(viewModel: MainViewModel, transaction: Transaction?, onFinished: () -> Unit) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            val income = rememberSaveable {
                mutableStateOf((transaction?.amount ?: 0.0) > 0.0)
            }
            val name = rememberSaveable {
                mutableStateOf(transaction?.name ?: "")
            }
            val amount = rememberSaveable {
                mutableStateOf((transaction?.amount ?: 0.0).absoluteValue.toString())
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    income.value = !income.value
                }
            ) {
                Text(if (income.value) "Income" else "Expense")
            }
            Spacer(Modifier.height(8.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = name.value,
                onValueChange = { name.value = it }
            )
            Spacer(Modifier.height(8.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = amount.value,
                onValueChange = { amount.value = it }
            )

            Spacer(Modifier.weight(1f))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val sign = if (income.value) 1 else -1
                    viewModel.upsert(
                        Transaction(
                            name.value,
                            sign * amount.value.toDouble(),
                            transaction?.date ?: Date(),
                            transaction?.id ?: 0
                        )
                    )
                    onFinished()
                }
            ) {
                if (transaction == null) {
                    Text("Add")
                } else {
                    Text("Update")
                }
            }

            if (transaction != null) {
                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.delete(transaction)
                        onFinished()
                    }
                ) {
                    Text("Delete")
                }
            }
        }
    }
}