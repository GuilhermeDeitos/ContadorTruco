package be.edu.utfpr.contadortruco

import android.content.Intent
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import be.edu.utfpr.contadortruco.databinding.ActivityMainBinding
import be.edu.utfpr.contadortruco.databinding.ItemPlayerBinding
import com.google.android.material.color.MaterialColors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var pontosJogador1 = 0
    private var pontosJogador2 = 0

    private var vitoriasJogador1 = 0
    private var vitoriasJogador2 = 0

    private var nomeJogador1 = ""
    private var nomeJogador2 = ""

    private val nomesLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data ?: return@registerForActivityResult
            val novoNome1 = data.getStringExtra(Constants.EXTRA_NOME_JOGADOR_1)
            val novoNome2 = data.getStringExtra(Constants.EXTRA_NOME_JOGADOR_2)

            if (!novoNome1.isNullOrBlank()) {
                nomeJogador1 = novoNome1
            }
            if (!novoNome2.isNullOrBlank()) {
                nomeJogador2 = novoNome2
            }
            atualizarPlacares()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        if (savedInstanceState != null) {
            pontosJogador1 = savedInstanceState.getInt(KEY_PONTOS_1, 0)
            pontosJogador2 = savedInstanceState.getInt(KEY_PONTOS_2, 0)
            vitoriasJogador1 = savedInstanceState.getInt(KEY_VITORIAS_1, 0)
            vitoriasJogador2 = savedInstanceState.getInt(KEY_VITORIAS_2, 0)
            nomeJogador1 = savedInstanceState.getString(KEY_NOME_1) ?: getString(R.string.player1_default_name)
            nomeJogador2 = savedInstanceState.getString(KEY_NOME_2) ?: getString(R.string.player2_default_name)
        } else {
            nomeJogador1 = getString(R.string.player1_default_name)
            nomeJogador2 = getString(R.string.player2_default_name)
        }

        binding.tvMeta.text = getString(R.string.goal_chip, Constants.PONTOS_PARA_VENCER)
        configurarBotoes()
        atualizarPlacares()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_PONTOS_1, pontosJogador1)
        outState.putInt(KEY_PONTOS_2, pontosJogador2)
        outState.putInt(KEY_VITORIAS_1, vitoriasJogador1)
        outState.putInt(KEY_VITORIAS_2, vitoriasJogador2)
        outState.putString(KEY_NOME_1, nomeJogador1)
        outState.putString(KEY_NOME_2, nomeJogador2)
    }

    private fun botoesDePontos(card: ItemPlayerBinding) = listOf(
        card.btnSum1 to 1,
        card.btnSum3 to 3,
        card.btnSum6 to 6,
        card.btnSum9 to 9,
        card.btnSum12 to 12
    )

    private fun configurarBotoes() {
        configurarJogador(binding.player1, 1)
        configurarJogador(binding.player2, 2)

        binding.btnHistJogadas.setOnClickListener {
            abrirHistorico()
        }

        binding.btnInfNomes.setOnClickListener {
            abrirInformarNomes()
        }

        binding.btnZerarHistorico.setOnClickListener {
            zerarHistorico()
        }
    }

    private fun configurarJogador(card: ItemPlayerBinding, jogador: Int) {
        botoesDePontos(card).forEach { (botao, pontos) ->
            botao.setOnClickListener {
                it.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                adicionarPontos(jogador, pontos)
            }
        }
    }

    private fun abrirHistorico() {
        val intent = Intent(this, HistoricoActivity::class.java).apply {
            putExtra(Constants.EXTRA_NOME_JOGADOR_1, nomeJogador1)
            putExtra(Constants.EXTRA_NOME_JOGADOR_2, nomeJogador2)
            putExtra(Constants.EXTRA_VITORIAS_JOGADOR_1, vitoriasJogador1)
            putExtra(Constants.EXTRA_VITORIAS_JOGADOR_2, vitoriasJogador2)
        }
        startActivity(intent)
    }

    private fun abrirInformarNomes() {
        val intent = Intent(this, NomesActivity::class.java).apply {
            putExtra(Constants.EXTRA_NOME_JOGADOR_1, nomeJogador1)
            putExtra(Constants.EXTRA_NOME_JOGADOR_2, nomeJogador2)
        }
        nomesLauncher.launch(intent)
    }

    private fun adicionarPontos(jogador: Int, pontos: Int) {
        when (jogador) {
            1 -> pontosJogador1 += pontos
            2 -> pontosJogador2 += pontos
            else -> return
        }

        verificarVencedor()
        atualizarPlacares()
        pulsar(if (jogador == 1) binding.player1.tvPontos else binding.player2.tvPontos)
    }

    private fun verificarVencedor() {
        when {
            pontosJogador1 >= Constants.PONTOS_PARA_VENCER -> encerrarPartida(1)
            pontosJogador2 >= Constants.PONTOS_PARA_VENCER -> encerrarPartida(2)
        }
    }

    private fun encerrarPartida(vencedor: Int) {
        val nomeVencedor = if (vencedor == 1) {
            vitoriasJogador1 += 1
            nomeJogador1
        } else {
            vitoriasJogador2 += 1
            nomeJogador2
        }

        Toast.makeText(
            this,
            getString(R.string.player_won, nomeVencedor),
            Toast.LENGTH_SHORT
        ).show()

        zerarPontos()
    }

    private fun zerarPontos() {
        pontosJogador1 = 0
        pontosJogador2 = 0
    }

    private fun atualizarPlacares() {
        atualizarCard(binding.player1, nomeJogador1, pontosJogador1, vitoriasJogador1, pontosJogador1 > pontosJogador2)
        atualizarCard(binding.player2, nomeJogador2, pontosJogador2, vitoriasJogador2, pontosJogador2 > pontosJogador1)
    }

    private fun atualizarCard(
        card: ItemPlayerBinding,
        nome: String,
        pontos: Int,
        vitorias: Int,
        lider: Boolean
    ) {
        card.tvNome.text = nome
        card.tvVitorias.text = textoVitorias(vitorias)
        card.tvPontos.text = pontos.toString()
        card.tvPontos.contentDescription = getString(R.string.cd_player_score, nome, textoPontos(pontos))
        card.progresso.setProgressCompat(pontos.coerceAtMost(Constants.PONTOS_PARA_VENCER), true)

        // Quem está na frente ganha a borda de destaque; empate não destaca ninguém.
        val atributoBorda = if (lider) {
            androidx.appcompat.R.attr.colorPrimary
        } else {
            com.google.android.material.R.attr.colorOutlineVariant
        }
        card.root.strokeColor = MaterialColors.getColor(card.root, atributoBorda)

        botoesDePontos(card).forEach { (botao, valor) ->
            botao.contentDescription = getString(R.string.cd_add_points, textoPontos(valor), nome)
        }
    }

    private fun textoPontos(n: Int) =
        getString(if (n == 1) R.string.points_one else R.string.points_many, n)

    private fun textoVitorias(n: Int) =
        getString(if (n == 1) R.string.wins_one else R.string.wins_many, n)

    // Pequeno "pulso" no número para confirmar visualmente que o toque foi registrado.
    private fun pulsar(v: View) {
        v.pivotX = 0f
        v.pivotY = v.height / 2f
        v.animate().scaleX(1.05f).scaleY(1.05f).setDuration(90).withEndAction {
            v.animate().scaleX(1f).scaleY(1f).setDuration(120).start()
        }.start()
    }

    private fun zerarHistorico() {
        zerarPontos()

        vitoriasJogador1 = 0
        vitoriasJogador2 = 0

        nomeJogador1 = getString(R.string.player1_default_name)
        nomeJogador2 = getString(R.string.player2_default_name)

        atualizarPlacares()

        Toast.makeText(
            this,
            getString(R.string.history_reset),
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        private const val KEY_PONTOS_1 = "KEY_PONTOS_1"
        private const val KEY_PONTOS_2 = "KEY_PONTOS_2"
        private const val KEY_VITORIAS_1 = "KEY_VITORIAS_1"
        private const val KEY_VITORIAS_2 = "KEY_VITORIAS_2"
        private const val KEY_NOME_1 = "KEY_NOME_1"
        private const val KEY_NOME_2 = "KEY_NOME_2"
    }
}
