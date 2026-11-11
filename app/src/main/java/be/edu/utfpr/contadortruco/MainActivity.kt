package be.edu.utfpr.contadortruco

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import be.edu.utfpr.contadortruco.databinding.ActivityMainBinding

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
            atualizarExibicaoNomes()
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

        configurarBotoes()
        atualizarExibicaoNomes()
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

    private fun configurarBotoes() {
        binding.btnSum1player1.setOnClickListener { adicionarPontos(1, 1) }
        binding.btnSum3player1.setOnClickListener { adicionarPontos(1, 3) }
        binding.btnSum6player1.setOnClickListener { adicionarPontos(1, 6) }
        binding.btnSum9player1.setOnClickListener { adicionarPontos(1, 9) }
        binding.btnSum12player1.setOnClickListener { adicionarPontos(1, 12) }

        binding.btnSum1player2.setOnClickListener { adicionarPontos(2, 1) }
        binding.btnSum3player2.setOnClickListener { adicionarPontos(2, 3) }
        binding.btnSum6player2.setOnClickListener { adicionarPontos(2, 6) }
        binding.btnSum9player2.setOnClickListener { adicionarPontos(2, 9) }
        binding.btnSum12player2.setOnClickListener { adicionarPontos(2, 12) }

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

    private fun abrirHistorico() {
        val intent = Intent(this, HistoricoActivity::class.java).apply {
            putExtra(Constants.EXTRA_NOME_JOGADOR_1, getString(R.string.player_name_format, nomeJogador1))
            putExtra(Constants.EXTRA_NOME_JOGADOR_2, getString(R.string.player_name_format, nomeJogador2))
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
    }

    private fun verificarVencedor() {
        when {
            pontosJogador1 >= 12 -> encerrarPartida(1)
            pontosJogador2 >= 12 -> encerrarPartida(2)
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

    private fun atualizarExibicaoNomes() {
        binding.tvNome1.text = getString(R.string.player_name_format, nomeJogador1)
        binding.tvNome2.text = getString(R.string.player_name_format, nomeJogador2)
    }

    private fun atualizarPlacares() {
        binding.tvResult.text = pontosJogador1.toString()
        binding.tvResult2.text = pontosJogador2.toString()
    }

    private fun zerarHistorico() {
        zerarPontos()

        vitoriasJogador1 = 0
        vitoriasJogador2 = 0

        nomeJogador1 = getString(R.string.player1_default_name)
        nomeJogador2 = getString(R.string.player2_default_name)

        atualizarExibicaoNomes()
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
