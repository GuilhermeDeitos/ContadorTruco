package be.edu.utfpr.contadortruco

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import be.edu.utfpr.contadortruco.databinding.ActivityHistoricoBinding

class HistoricoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoricoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoricoBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        carregarDadosHistorico()
    }

    private fun carregarDadosHistorico() {
        val nome1 = intent.getStringExtra("EXTRA_NOME_JOGADOR_1") ?: getString(R.string.player1_label)
        val nome2 = intent.getStringExtra("EXTRA_NOME_JOGADOR_2") ?: getString(R.string.player2_label)
        val vitorias1 = intent.getIntExtra("EXTRA_VITORIAS_JOGADOR_1", 0)
        val vitorias2 = intent.getIntExtra("EXTRA_VITORIAS_JOGADOR_2", 0)

        binding.tvNomeJogador.text = nome1
        binding.tvNomeJogador2.text = nome2
        binding.tvPontuacaoPlayer1.text = vitorias1.toString()
        binding.tvPontuacaoPlayer2.text = vitorias2.toString()
    }
}
