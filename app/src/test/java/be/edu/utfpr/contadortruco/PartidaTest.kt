package be.edu.utfpr.contadortruco

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PartidaTest {

    @Test
    fun chegarEmDozeEncerraEContaVitoria() {
        val p = Partida()
        p.adicionar(0, 9)
        p.adicionar(0, 3)

        assertTrue(p.encerrada)
        assertEquals(0, p.vencedor)
        assertEquals(1, p.vitorias[0])
        assertEquals(12, p.pontos[0])
    }

    @Test
    fun passarDeDozeTambemVenceEMantemOPlacarFinal() {
        val p = Partida()
        p.adicionar(1, 9)
        p.adicionar(1, 6)

        assertEquals(1, p.vencedor)
        assertEquals(15, p.pontos[1])
    }

    @Test
    fun naoPontuaDepoisDeEncerrada() {
        val p = Partida()
        p.adicionar(0, 12)
        p.adicionar(1, 3)

        assertEquals(0, p.pontos[1])
        assertEquals(1, p.historico.size)
    }

    @Test
    fun desfazerRetiraOsPontosEADesfazAVitoria() {
        val p = Partida()
        p.adicionar(1, 3)
        p.adicionar(0, 12)

        p.desfazer()

        assertFalse(p.encerrada)
        assertEquals(0, p.vitorias[0])
        assertEquals(0, p.pontos[0])
        assertEquals(3, p.pontos[1])
    }

    @Test
    fun desfazerSemJogadasNaoFazNada() {
        val p = Partida()
        p.desfazer()

        assertEquals(0, p.pontos[0])
        assertFalse(p.podeDesfazer)
    }

    @Test
    fun novaPartidaMantemVitoriasENomes() {
        val p = Partida()
        p.nomes[0] = "Ana"
        p.adicionar(0, 12)

        p.novaPartida()

        assertEquals(0, p.pontos[0])
        assertEquals(1, p.vitorias[0])
        assertEquals("Ana", p.nomes[0])
        assertFalse(p.encerrada)
        assertFalse(p.podeDesfazer)
    }

    @Test
    fun zerarLimpaTudo() {
        val p = Partida()
        p.nomes[1] = "Bruno"
        p.adicionar(1, 12)

        p.zerar()

        assertEquals(0, p.vitorias[1])
        assertEquals("", p.nomes[1])
        assertFalse(p.encerrada)
    }
}
