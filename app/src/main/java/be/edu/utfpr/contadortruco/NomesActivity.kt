package be.edu.utfpr.contadortruco

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import be.edu.utfpr.contadortruco.databinding.ActivityNomesBinding

class NomesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNomesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNomesBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val teclado = insets.getInsets(WindowInsetsCompat.Type.ime())
            // Com edge-to-edge o teclado não redimensiona a janela; o espaço dele entra aqui.
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, maxOf(systemBars.bottom, teclado.bottom))
            insets
        }

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        carregarNomesAtuais()
        configurarBotoes()

        if (savedInstanceState == null) {
            focarPrimeiroCampo()
        }
    }

    private fun carregarNomesAtuais() {
        val nome1 = intent.getStringExtra(Constants.EXTRA_NOME_JOGADOR_1) ?: ""
        val nome2 = intent.getStringExtra(Constants.EXTRA_NOME_JOGADOR_2) ?: ""

        binding.etNomeJogador1.setText(nome1)
        binding.etNomeJogador2.setText(nome2)
    }

    // Abre já editando o primeiro nome, com o texto selecionado para trocar direto.
    private fun focarPrimeiroCampo() {
        binding.etNomeJogador1.requestFocus()
        binding.etNomeJogador1.selectAll()
        WindowCompat.getInsetsController(window, binding.etNomeJogador1)
            .show(WindowInsetsCompat.Type.ime())
    }

    private fun configurarBotoes() {
        binding.btnConfirmarNome.setOnClickListener { confirmar() }

        // "Concluir" no teclado do segundo campo equivale a tocar em Confirmar.
        binding.etNomeJogador2.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                confirmar()
                true
            } else {
                false
            }
        }
    }

    private fun confirmar() {
        val novoNome1 = binding.etNomeJogador1.text.toString().trim()
        val novoNome2 = binding.etNomeJogador2.text.toString().trim()

        val intentResult = Intent().apply {
            putExtra(Constants.EXTRA_NOME_JOGADOR_1, novoNome1)
            putExtra(Constants.EXTRA_NOME_JOGADOR_2, novoNome2)
        }

        setResult(RESULT_OK, intentResult)
        finish()
    }
}
