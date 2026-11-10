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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.btnSum1player1.setOnClickListener { addPointsPlayer1(1) }
        binding.btnSum3player1.setOnClickListener { addPointsPlayer1(3) }
        binding.btnSum6player1.setOnClickListener { addPointsPlayer1(6) }
        binding.btnSum9player1.setOnClickListener { addPointsPlayer1(9) }
        binding.btnSum12player1.setOnClickListener { addPointsPlayer1(12) }

        binding.btnSum1player2.setOnClickListener { addPointsPlayer2(1) }
        binding.btnSum3player2.setOnClickListener { addPointsPlayer2(3) }
        binding.btnSum6player2.setOnClickListener { addPointsPlayer2(6) }
        binding.btnSum9player2.setOnClickListener { addPointsPlayer2(9) }
        binding.btnSum12player2.setOnClickListener { addPointsPlayer2(12) }

        binding.btnHistJogadas.setOnClickListener {
            binding.tvResult.text = "0"
            binding.tvResult2.text = "0"
        }

        binding.btnZerarHistorico.setOnClickListener {
            binding.tvResult.text = "0"
            binding.tvResult2.text = "0"
        }

        binding.btnInfNomes.setOnClickListener {
            binding.tvNome1.text = "Jogador 1:"
            binding.tvNome2.text = "Jogador 2:"
        }
    }

    private fun addPointsPlayer1(points: Int) {
        val current = binding.tvResult.text.toString().toIntOrNull() ?: 0
        val newScore = current + points
        binding.tvResult.text = newScore.toString()
        checkWinner(newScore, binding.tvResult2.text.toString().toIntOrNull() ?: 0)
    }

    private fun addPointsPlayer2(points: Int) {
        val current = binding.tvResult2.text.toString().toIntOrNull() ?: 0
        val newScore = current + points
        binding.tvResult2.text = newScore.toString()
        checkWinner(binding.tvResult.text.toString().toIntOrNull() ?: 0, newScore)
    }

    private fun checkWinner(score1: Int, score2: Int) {
        if (score1 >= 12) {
            Toast.makeText(this, "Jogador 1 venceu!", Toast.LENGTH_SHORT).show()
            resetScores()

        } else if (score2 >= 12) {
            Toast.makeText(this, "Jogador 2 venceu!", Toast.LENGTH_SHORT).show()
            resetScores()
        }
    }

    private fun resetScores() {
        binding.tvResult.text = "0"
        binding.tvResult2.text = "0"
    }

    override fun onResume() {
        super.onResume()
        binding.tvResult.text = "0"
        binding.tvResult2.text = "0"
    }
}
