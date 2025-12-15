package com.example.pokemonexplorer.presentation.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pokemonexplorer.presentation.ui.theme.PokemonExplorerTheme

@Composable
fun StatBar(label: String, value: Int, max: Int, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.width(30.dp),
            fontWeight = FontWeight.Bold
        )
        // Progress bar
        LinearProgressIndicator(
            progress = { (value / max.toFloat()).coerceIn(0f, 1f) },
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = Color.LightGray.copy(alpha = 0.3f),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$value",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.width(25.dp)
        )
    }
}

@Preview
@Composable
private fun StatBarPreview() {
    PokemonExplorerTheme {
        StatBar(
            label = "HP",
            value = 100,
            max = 200,
            color = Color.Red
        )
    }
}