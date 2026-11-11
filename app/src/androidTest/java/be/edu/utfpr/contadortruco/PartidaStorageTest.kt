package be.edu.utfpr.contadortruco

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PartidaStorageTest {

    private lateinit var storage: PartidaStorage

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.getSharedPreferences("partida", android.content.Context.MODE_PRIVATE)
            .edit()
            .clear()
            .commit()
        storage = PartidaStorage(context)
    }

    @Test
    fun testSalvarECarregarPartidaEmArmazenamento() {
        val partidaOriginal = Partida(
            pontos = intArrayOf(6, 3),
            vitorias = intArrayOf(2, 1),
            nomes = arrayOf("Dupla Azul", "Dupla Vermelha")
        )
        partidaOriginal.adicionar(0, 3)

        storage.salvar(partidaOriginal)

        val partidaCarregada = storage.carregar()

        assertEquals(partidaOriginal.pontos[0], partidaCarregada.pontos[0])
        assertEquals(partidaOriginal.pontos[1], partidaCarregada.pontos[1])
        assertEquals(partidaOriginal.vitorias[0], partidaCarregada.vitorias[0])
        assertEquals(partidaOriginal.vitorias[1], partidaCarregada.vitorias[1])
        assertEquals(partidaOriginal.nomes[0], partidaCarregada.nomes[0])
        assertEquals(partidaOriginal.nomes[1], partidaCarregada.nomes[1])
        assertEquals(partidaOriginal.historico.size, partidaCarregada.historico.size)
    }
}
