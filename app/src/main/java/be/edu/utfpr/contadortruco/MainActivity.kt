package be.edu.utfpr.contadortruco

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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

        configurarBotoes()
        atualizarPlacares()
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

        binding.btnZerarHistorico.setOnClickListener {
            zerarHistorico()
        }

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
            binding.tvNome1.text.toString().trim().removeSuffix(":")
        } else {
            vitoriasJogador2 += 1
            binding.tvNome2.text.toString().trim().removeSuffix(":")
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
        binding.tvResult.text = pontosJogador1.toString()
        binding.tvResult2.text = pontosJogador2.toString()
    }

    private fun zerarHistorico() {
        zerarPontos()

        vitoriasJogador1 = 0
        vitoriasJogador2 = 0

        binding.tvNome1.text = getString(R.string.player1_label)
        binding.tvNome2.text = getString(R.string.player2_label)

        atualizarPlacares()

        Toast.makeText(
            this,
            getString(R.string.history_reset),
            Toast.LENGTH_SHORT
        ).show()
    }
}