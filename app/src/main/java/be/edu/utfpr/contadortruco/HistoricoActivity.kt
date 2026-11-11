package be.edu.utfpr.contadortruco

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import be.edu.utfpr.contadortruco.databinding.ActivityHistoricoBinding
import be.edu.utfpr.contadortruco.databinding.ItemWinsBinding
import com.google.android.material.color.MaterialColors

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

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        carregarDadosHistorico()
    }

    private fun carregarDadosHistorico() {
        val nome1 = intent.getStringExtra(Constants.EXTRA_NOME_JOGADOR_1) ?: getString(R.string.player1_default_name)
        val nome2 = intent.getStringExtra(Constants.EXTRA_NOME_JOGADOR_2) ?: getString(R.string.player2_default_name)
        val vitorias1 = intent.getIntExtra(Constants.EXTRA_VITORIAS_JOGADOR_1, 0)
        val vitorias2 = intent.getIntExtra(Constants.EXTRA_VITORIAS_JOGADOR_2, 0)

        val partidas = vitorias1 + vitorias2
        val maximo = maxOf(vitorias1, vitorias2, 1)

        preencher(binding.wins1, nome1, vitorias1, vitorias1 > vitorias2, maximo)
        preencher(binding.wins2, nome2, vitorias2, vitorias2 > vitorias1, maximo)

        val semPartidas = partidas == 0
        binding.tvPartidas.isVisible = !semPartidas
        binding.tvVazio.isVisible = semPartidas
        if (semPartidas) {
            binding.tvVazio.text = getString(R.string.history_empty, Constants.PONTOS_PARA_VENCER)
        } else {
            binding.tvPartidas.text = getString(
                if (partidas == 1) R.string.matches_one else R.string.matches_many,
                partidas
            )
        }
    }

    private fun preencher(card: ItemWinsBinding, nome: String, vitorias: Int, lider: Boolean, maximo: Int) {
        val vitoriasTexto = getString(
            if (vitorias == 1) R.string.wins_one else R.string.wins_many,
            vitorias
        )

        card.tvNome.text = nome
        card.tvVitorias.text = vitorias.toString()
        card.tvVitorias.contentDescription = getString(R.string.cd_player_score, nome, vitoriasTexto)
        card.chipLider.isVisible = lider

        // Quem lidera usa a cor de destaque; o outro, um tom neutro.
        val cor = MaterialColors.getColor(
            card.root,
            if (lider) androidx.appcompat.R.attr.colorPrimary else com.google.android.material.R.attr.colorOutline
        )
        card.progresso.setIndicatorColor(cor)
        card.progresso.max = maximo
        card.progresso.setProgressCompat(vitorias, true)

        card.root.strokeColor = MaterialColors.getColor(
            card.root,
            if (lider) androidx.appcompat.R.attr.colorPrimary else com.google.android.material.R.attr.colorOutlineVariant
        )
    }
}
