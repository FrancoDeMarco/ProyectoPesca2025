package com.example.proyectopesca2025.Reglamentos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyectopesca2025.database.Reglamento

@Composable
fun ReglamentoItem(reglamento: Reglamento, onItemSelected: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemSelected() }
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(
            text = reglamento.reglamentoNombre,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        )
        Text(
            text = reglamento.reglamentoLugar,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        )
    }
}