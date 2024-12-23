package com.example.ppp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppButton(text: String, onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .width(110.dp)
            .height(30.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
        onClick = onClick
    ) {
        Text(
            fontSize = 12.sp,
            modifier = Modifier.background(color = Color.Black),
            text = text,
            color = Color.White
        )
    }
}