package be.edu.utfpr.contadortruco

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Before
    fun setup() {
        // Limpa SharedPreferences antes de cada teste para ter um estado limpo
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.getSharedPreferences("partida", android.content.Context.MODE_PRIVATE)
            .edit()
            .clear()
            .commit()
    }

    @Test
    fun testAdicionarPontosEAtualizarPlacar() {
        ActivityScenario.launch(MainActivity::class.java).use {
            // Clica no botão +1 do Jogador 1 (dentro de player1)
            onView(allOf(withId(R.id.btn_sum1), isDescendantOfA(withId(R.id.player1))))
                .perform(click())

            // Clica no botão +3 do Jogador 1
            onView(allOf(withId(R.id.btn_sum3), isDescendantOfA(withId(R.id.player1))))
                .perform(click())

            // Verifica se a pontuação do Jogador 1 mostra "4"
            onView(allOf(withId(R.id.tv_pontos), isDescendantOfA(withId(R.id.player1))))
                .check(matches(withText("4")))
        }
    }

    @Test
    fun testDesfazerJogada() {
        ActivityScenario.launch(MainActivity::class.java).use {
            // Adiciona +3 pontos para o Jogador 1
            onView(allOf(withId(R.id.btn_sum3), isDescendantOfA(withId(R.id.player1))))
                .perform(click())

            // Clica em desfazer
            onView(withId(R.id.btn_desfazer))
                .perform(click())

            // Verifica se a pontuação do Jogador 1 voltou para "0"
            onView(allOf(withId(R.id.tv_pontos), isDescendantOfA(withId(R.id.player1))))
                .check(matches(withText("0")))
        }
    }

    @Test
    fun testVitoriaExibeDialogoENovaPartida() {
        ActivityScenario.launch(MainActivity::class.java).use {
            // Adiciona 12 pontos para o Jogador 1 (vitória direta)
            onView(allOf(withId(R.id.btn_sum12), isDescendantOfA(withId(R.id.player1))))
                .perform(click())

            // Verifica se o diálogo de vitória abriu com o botão "Nova partida"
            onView(withText(R.string.new_match))
                .check(matches(isDisplayed()))

            // Inicia nova partida
            onView(withText(R.string.new_match))
                .perform(click())

            // Verifica se a pontuação do Jogador 1 resetou para "0"
            onView(allOf(withId(R.id.tv_pontos), isDescendantOfA(withId(R.id.player1))))
                .check(matches(withText("0")))
        }
    }

    @Test
    fun testConfirmacaoAoZerarHistorico() {
        ActivityScenario.launch(MainActivity::class.java).use {
            // Clica em Zerar Histórico
            onView(withId(R.id.btn_zerarHistorico))
                .perform(click())

            // Verifica se o diálogo de confirmação é exibido
            onView(withText(R.string.reset_title))
                .check(matches(isDisplayed()))

            // Confirma zerar no botão do diálogo
            onView(allOf(withId(android.R.id.button1), withText(R.string.btn_zerar_historico)))
                .perform(click())
        }
    }

    @Test
    fun testNavegacaoParaHistorico() {
        ActivityScenario.launch(MainActivity::class.java).use {
            // Clica no botão de Histórico
            onView(withId(R.id.btn_histJogadas))
                .perform(click())

            // Verifica se a HistoricoActivity foi exibida
            onView(withText(R.string.title_historico))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun testNavegacaoParaNomes() {
        ActivityScenario.launch(MainActivity::class.java).use {
            // Clica no botão de Informar Nomes
            onView(withId(R.id.btn_infNomes))
                .perform(click())

            // Verifica se a NomesActivity foi exibida
            onView(withId(R.id.et_nomeJogador1))
                .check(matches(isDisplayed()))
        }
    }
}
