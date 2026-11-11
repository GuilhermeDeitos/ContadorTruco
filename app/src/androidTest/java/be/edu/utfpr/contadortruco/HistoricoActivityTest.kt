package be.edu.utfpr.contadortruco

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matchers.allOf
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HistoricoActivityTest {

    @Test
    fun testExibeVitoriasEContagemDePartidas() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(context, HistoricoActivity::class.java).apply {
            putExtra(Constants.EXTRA_NOME_JOGADOR_1, "Jogador A")
            putExtra(Constants.EXTRA_NOME_JOGADOR_2, "Jogador B")
            putExtra(Constants.EXTRA_VITORIAS_JOGADOR_1, 3)
            putExtra(Constants.EXTRA_VITORIAS_JOGADOR_2, 1)
        }

        ActivityScenario.launch<HistoricoActivity>(intent).use {
            // Verifica se a contagem total de partidas mostra "4 partidas"
            onView(withId(R.id.tv_partidas))
                .check(matches(withText("4 partidas")))

            // Verifica se o nome do Jogador A aparece no card wins1
            onView(allOf(withId(R.id.tv_nome), isDescendantOfA(withId(R.id.wins1))))
                .check(matches(withText("Jogador A")))

            // Verifica se o número de vitórias "3" é exibido no card wins1
            onView(allOf(withId(R.id.tv_vitorias), isDescendantOfA(withId(R.id.wins1))))
                .check(matches(withText("3")))
        }
    }

    @Test
    fun testExibeEstadoVazioQuandoNaoHaVitorias() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(context, HistoricoActivity::class.java).apply {
            putExtra(Constants.EXTRA_NOME_JOGADOR_1, "Jogador A")
            putExtra(Constants.EXTRA_NOME_JOGADOR_2, "Jogador B")
            putExtra(Constants.EXTRA_VITORIAS_JOGADOR_1, 0)
            putExtra(Constants.EXTRA_VITORIAS_JOGADOR_2, 0)
        }

        ActivityScenario.launch<HistoricoActivity>(intent).use {
            // Verifica se a mensagem de histórico vazio é exibida
            onView(withId(R.id.tv_vazio))
                .check(matches(isDisplayed()))
        }
    }
}
