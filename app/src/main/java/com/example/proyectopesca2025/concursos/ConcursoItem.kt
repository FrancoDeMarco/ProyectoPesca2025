package com.example.proyectopesca2025.concursos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.proyectopesca2025.database.Concurso

@Composable
fun ConcursoItem(concurso: Concurso, onItemClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick)
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(
            text = concurso.concursoNombre,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleLarge,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Text(
            text = concurso.concursoLugar,
            modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.titleLarge

        )
    }
}