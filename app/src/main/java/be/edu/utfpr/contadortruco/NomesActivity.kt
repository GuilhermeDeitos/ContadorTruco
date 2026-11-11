package be.edu.utfpr.contadortruco

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
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
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        carregarNomesAtuais()
        configurarBotoes()
    }

    private fun carregarNomesAtuais() {
        val nome1 = intent.getStringExtra("EXTRA_NOME_JOGADOR_1") ?: ""
        val nome2 = intent.getStringExtra("EXTRA_NOME_JOGADOR_2") ?: ""

        binding.etNomeJogador1.setText(nome1)
        binding.etNomeJogador2.setText(nome2)
    }

    private fun configurarBotoes() {
        binding.btnConfirmarNome.setOnClickListener {
            val novoNome1 = binding.etNomeJogador1.text.toString().trim()
            val novoNome2 = binding.etNomeJogador2.text.toString().trim()

            val intentResult = Intent().apply {
                putExtra("EXTRA_NOME_JOGADOR_1", novoNome1)
                putExtra("EXTRA_NOME_JOGADOR_2", novoNome2)
            }

            setResult(RESULT_OK, intentResult)
            finish()
        }
    }
}
