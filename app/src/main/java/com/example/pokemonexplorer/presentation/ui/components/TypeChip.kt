package com.example.pokemonexplorer.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun TypeChip(
    type: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // 1. Ορίζουμε τα χρώματα ρητά για να έχουμε τέλεια αντίθεση (Contrast)
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary // Χρώμα θέματος (π.χ. Μωβ/Μπλε)
    } else {
        Color(0xFFE0E0E0) // Ανοιχτό Γκρι για τα μη επιλεγμένα
    }

    val contentColor = if (isSelected) {
        Color.White // Λευκά γράμματα στο επιλεγμένο
    } else {
        Color.Black // Μαύρα γράμματα στο μη επιλεγμένο (για να διαβάζεται εύκολα)
    }

    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(20.dp), // Λίγο πιο στρογγυλεμένες γωνίες
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = type.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
            // 2. Κάνουμε πιο έντονη τη γραμματοσειρά αν είναι επιλεγμένο
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}