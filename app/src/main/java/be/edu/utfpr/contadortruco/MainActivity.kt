package be.edu.utfpr.contadortruco

import android.content.Intent
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import be.edu.utfpr.contadortruco.databinding.ActivityMainBinding
import be.edu.utfpr.contadortruco.databinding.ItemPlayerBinding
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var storage: PartidaStorage
    private lateinit var partida: Partida

    private var dialogoVitoria: AlertDialog? = null

    private val nomesLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data ?: return@registerForActivityResult
            atualizarNome(0, data.getStringExtra(Constants.EXTRA_NOME_JOGADOR_1))
            atualizarNome(1, data.getStringExtra(Constants.EXTRA_NOME_JOGADOR_2))
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

        // A partida vem do armazenamento: sobrevive a rotação e a fechar o app.
        storage = PartidaStorage(this)
        partida = storage.carregar()

        binding.tvMeta.text = getString(R.string.goal_chip, Constants.PONTOS_PARA_VENCER)
        configurarBotoes()
        atualizarPlacares()

        // Se o app foi fechado com a partida já decidida, o diálogo volta.
        if (partida.encerrada) {
            mostrarDialogoVitoria()
        }
    }

    override fun onPause() {
        super.onPause()
        storage.salvar(partida)
    }

    override fun onDestroy() {
        dialogoVitoria?.dismiss()
        super.onDestroy()
    }

    private fun botoesDePontos(card: ItemPlayerBinding) = listOf(
        card.btnSum1 to 1,
        card.btnSum3 to 3,
        card.btnSum6 to 6,
        card.btnSum9 to 9,
        card.btnSum12 to 12
    )

    private fun configurarBotoes() {
        configurarJogador(binding.player1, 0)
        configurarJogador(binding.player2, 1)

        binding.btnDesfazer.setOnClickListener {
            partida.desfazer()
            atualizarPlacares()
        }

        binding.btnHistJogadas.setOnClickListener {
            abrirHistorico()
        }

        binding.btnInfNomes.setOnClickListener {
            abrirInformarNomes()
        }

        binding.btnZerarHistorico.setOnClickListener {
            confirmarZerarHistorico()
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

    private fun nome(jogador: Int): String =
        partida.nomes[jogador].ifBlank {
            getString(if (jogador == 0) R.string.player1_default_name else R.string.player2_default_name)
        }

    // Nome em branco mantém o atual; igual ao padrão volta a ser "padrão" (segue o idioma).
    private fun atualizarNome(jogador: Int, novo: String?) {
        if (novo.isNullOrBlank()) return
        val padrao = getString(if (jogador == 0) R.string.player1_default_name else R.string.player2_default_name)
        partida.nomes[jogador] = if (novo == padrao) "" else novo
    }

    private fun abrirHistorico() {
        val intent = Intent(this, HistoricoActivity::class.java).apply {
            putExtra(Constants.EXTRA_NOME_JOGADOR_1, nome(0))
            putExtra(Constants.EXTRA_NOME_JOGADOR_2, nome(1))
            putExtra(Constants.EXTRA_VITORIAS_JOGADOR_1, partida.vitorias[0])
            putExtra(Constants.EXTRA_VITORIAS_JOGADOR_2, partida.vitorias[1])
        }
        startActivity(intent)
    }

    private fun abrirInformarNomes() {
        val intent = Intent(this, NomesActivity::class.java).apply {
            putExtra(Constants.EXTRA_NOME_JOGADOR_1, nome(0))
            putExtra(Constants.EXTRA_NOME_JOGADOR_2, nome(1))
        }
        nomesLauncher.launch(intent)
    }

    private fun adicionarPontos(jogador: Int, pontos: Int) {
        partida.adicionar(jogador, pontos)
        atualizarPlacares()
        pulsar(if (jogador == 0) binding.player1.tvPontos else binding.player2.tvPontos)

        if (partida.encerrada) {
            mostrarDialogoVitoria()
        }
    }

    private fun mostrarDialogoVitoria() {
        if (dialogoVitoria?.isShowing == true) return

        // Não dá para fechar tocando fora: a decisão é sempre "nova partida" ou "desfazer".
        dialogoVitoria = MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.player_won, nome(partida.vencedor)))
            .setMessage(
                getString(
                    R.string.final_score,
                    nome(0), partida.pontos[0], partida.pontos[1], nome(1)
                )
            )
            .setCancelable(false)
            .setPositiveButton(R.string.new_match) { _, _ ->
                partida.novaPartida()
                atualizarPlacares()
            }
            .setNegativeButton(R.string.undo) { _, _ ->
                partida.desfazer()
                atualizarPlacares()
            }
            .show()
    }

    private fun confirmarZerarHistorico() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.reset_title)
            .setMessage(R.string.reset_message)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.btn_zerar_historico) { _, _ -> zerarHistorico() }
            .show()
    }

    private fun zerarHistorico() {
        partida.zerar()
        atualizarPlacares()
        Snackbar.make(binding.root, R.string.history_reset, Snackbar.LENGTH_SHORT).show()
    }

    private fun atualizarPlacares() {
        val p = partida.pontos
        atualizarCard(binding.player1, nome(0), p[0], partida.vitorias[0], p[0] > p[1])
        atualizarCard(binding.player2, nome(1), p[1], partida.vitorias[1], p[1] > p[0])
        binding.btnDesfazer.isEnabled = partida.podeDesfazer
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
}
