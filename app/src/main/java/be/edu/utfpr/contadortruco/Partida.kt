package be.edu.utfpr.contadortruco

data class Jogada(val jogador: Int, val pontos: Int)

/**
 * Estado e regras de uma sessão de truco entre dois jogadores (índices 0 e 1).
 * Não depende de Android, para poder ser testado como JUnit puro.
 *
 * [nomes] guarda "" quando o jogador usa o nome padrão (que depende do idioma).
 */
class Partida(
    val pontos: IntArray = IntArray(2),
    val vitorias: IntArray = IntArray(2),
    val nomes: Array<String> = arrayOf("", ""),
    var vencedor: Int = SEM_VENCEDOR,
    jogadasIniciais: List<Jogada> = emptyList()
) {
    private val jogadas = ArrayDeque(jogadasIniciais)

    val historico: List<Jogada> get() = jogadas
    val podeDesfazer: Boolean get() = jogadas.isNotEmpty()
    val encerrada: Boolean get() = vencedor != SEM_VENCEDOR

    /** Soma pontos; ao chegar em 12 ou mais a partida termina e a vitória é contada. */
    fun adicionar(jogador: Int, quantidade: Int) {
        if (encerrada) return
        pontos[jogador] += quantidade
        jogadas.addLast(Jogada(jogador, quantidade))
        if (pontos[jogador] >= Constants.PONTOS_PARA_VENCER) {
            vencedor = jogador
            vitorias[jogador]++
        }
    }

    /** Desfaz a última pontuação; se ela encerrou a partida, a vitória também é desfeita. */
    fun desfazer() {
        val jogada = jogadas.removeLastOrNull() ?: return
        if (vencedor == jogada.jogador) {
            vitorias[jogada.jogador]--
            vencedor = SEM_VENCEDOR
        }
        pontos[jogada.jogador] -= jogada.pontos
    }

    /** Começa outra partida mantendo vitórias e nomes. */
    fun novaPartida() {
        pontos.fill(0)
        jogadas.clear()
        vencedor = SEM_VENCEDOR
    }

    /** Volta tudo ao início: placar, vitórias e nomes. */
    fun zerar() {
        novaPartida()
        vitorias.fill(0)
        nomes.fill("")
    }

    companion object {
        const val SEM_VENCEDOR = -1
    }
}
