package be.edu.utfpr.contadortruco

import android.content.Context
import androidx.core.content.edit

/** Guarda a [Partida] em SharedPreferences para sobreviver ao fechamento do app. */
class PartidaStorage(context: Context) {

    private val prefs = context.getSharedPreferences("partida", Context.MODE_PRIVATE)

    fun carregar(): Partida {
        val jogadas = prefs.getString(KEY_JOGADAS, "").orEmpty()
            .split(",")
            .mapNotNull { item ->
                val partes = item.split(":")
                val jogador = partes.getOrNull(0)?.toIntOrNull()
                val pontos = partes.getOrNull(1)?.toIntOrNull()
                if (partes.size == 2 && jogador != null && jogador in 0..1 && pontos != null) {
                    Jogada(jogador, pontos)
                } else {
                    null
                }
            }

        return Partida(
            pontos = intArrayOf(prefs.getInt(KEY_PONTOS_1, 0), prefs.getInt(KEY_PONTOS_2, 0)),
            vitorias = intArrayOf(prefs.getInt(KEY_VITORIAS_1, 0), prefs.getInt(KEY_VITORIAS_2, 0)),
            nomes = arrayOf(
                prefs.getString(KEY_NOME_1, "").orEmpty(),
                prefs.getString(KEY_NOME_2, "").orEmpty()
            ),
            vencedor = prefs.getInt(KEY_VENCEDOR, Partida.SEM_VENCEDOR).coerceIn(-1, 1),
            jogadasIniciais = jogadas
        )
    }

    fun salvar(partida: Partida) {
        prefs.edit {
            putInt(KEY_PONTOS_1, partida.pontos[0])
            putInt(KEY_PONTOS_2, partida.pontos[1])
            putInt(KEY_VITORIAS_1, partida.vitorias[0])
            putInt(KEY_VITORIAS_2, partida.vitorias[1])
            putString(KEY_NOME_1, partida.nomes[0])
            putString(KEY_NOME_2, partida.nomes[1])
            putInt(KEY_VENCEDOR, partida.vencedor)
            putString(KEY_JOGADAS, partida.historico.joinToString(",") { "${it.jogador}:${it.pontos}" })
        }
    }

    private companion object {
        const val KEY_PONTOS_1 = "pontos1"
        const val KEY_PONTOS_2 = "pontos2"
        const val KEY_VITORIAS_1 = "vitorias1"
        const val KEY_VITORIAS_2 = "vitorias2"
        const val KEY_NOME_1 = "nome1"
        const val KEY_NOME_2 = "nome2"
        const val KEY_VENCEDOR = "vencedor"
        const val KEY_JOGADAS = "jogadas"
    }
}
