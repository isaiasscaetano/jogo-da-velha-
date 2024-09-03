package com.example.jogodavelha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jogodavelha.ui.theme.DARK_GREEN
import com.example.jogodavelha.ui.theme.GREEN
import com.example.jogodavelha.ui.theme.WHITE

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JogoDaVelha()
        }
    }

    @Composable
    fun JogoDaVelha() {
        var bloco by remember { mutableStateOf(List(9) { "" }) }
        var jogadorAtual by remember { mutableStateOf("x") }
        var vencedor by remember { mutableStateOf<String?>(null) }

        fun reiniciarJogo() {
            bloco = List(9) { "" }
            vencedor = null
            jogadorAtual = "x"
        }

        fun cliqueDoBloco(index: Int) {
            if (bloco[index].isEmpty() && vencedor == null) {
                bloco = bloco.toMutableList().apply {
                    this[index] = jogadorAtual
                }
                vencedor = verificarVencedor(bloco)
                if (vencedor == null) {
                    jogadorAtual = if (jogadorAtual == "x") "o" else "x"
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = vencedor?.let { "Vencedor: $it" } ?: "Jogador $jogadorAtual",
                    style = MaterialTheme.typography.headlineMedium
                )
                Icon(
                    imageVector = Icons.Rounded.Refresh,
                    contentDescription = null,
                    tint = GREEN,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(30.dp)
                        .clickable { reiniciarJogo() }
                )
            }

            Grade(bloco = bloco, cliqueDoBloco = ::cliqueDoBloco)
        }
    }
}

@Composable
fun Grade(bloco: List<String>, cliqueDoBloco: (Int) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (row in 0 until 3) {
            Row {
                for (col in 0 until 3) {
                    val index = row * 3 + col
                    Quadrado(
                        value = bloco[index],
                        onClick = { cliqueDoBloco(index) }
                    )
                }
            }
        }
    }
}

@Composable
fun Quadrado(value: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(WHITE)
            .border(2.dp, DARK_GREEN)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineLarge,
            color = GREEN,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

fun verificarVencedor(bloco: List<String>): String? {
    val combinacoesVencedoras = listOf(
        listOf(0, 1, 2),
        listOf(3, 4, 5),
        listOf(6, 7, 8),
        listOf(0, 3, 6),
        listOf(1, 4, 7),
        listOf(2, 5, 8),
        listOf(0, 4, 8),
        listOf(2, 4, 6)
    )

    for (combinacao in combinacoesVencedoras) {
        val (a, b, c) = combinacao
        if (bloco[a].isNotEmpty() && bloco[a] == bloco[b] && bloco[a] == bloco[c]) {
            return bloco[a]
        }
    }

    return if (bloco.all { it.isNotEmpty() }) "empate" else null
}
