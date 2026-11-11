package be.edu.utfpr.contadortruco

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NomesActivityTest {

    @Test
    fun testCarregaNomesAtuaisEEdita() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(context, NomesActivity::class.java).apply {
            putExtra(Constants.EXTRA_NOME_JOGADOR_1, "Dupla A")
            putExtra(Constants.EXTRA_NOME_JOGADOR_2, "Dupla B")
        }

        ActivityScenario.launch<NomesActivity>(intent).use {
            // Verifica se os nomes recebidos via extra foram preenchidos
            onView(withId(R.id.et_nomeJogador1))
                .check(matches(withText("Dupla A")))

            onView(withId(R.id.et_nomeJogador2))
                .check(matches(withText("Dupla B")))

            // Edita os nomes
            onView(withId(R.id.et_nomeJogador1))
                .perform(clearText(), typeText("Equipe Azul"))

            onView(withId(R.id.et_nomeJogador2))
                .perform(clearText(), typeText("Equipe Vermelha"))

            // Clica em confirmar
            onView(withId(R.id.btn_confirmarNome))
                .perform(click())
        }
    }
}
