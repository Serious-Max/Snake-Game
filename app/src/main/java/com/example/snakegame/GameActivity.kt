package com.example.snakegame

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.snakegame.databinding.ActivityGameBinding


class GameActivity() : AppCompatActivity(), OnClickListener, GameCallback {
    private lateinit var binding: ActivityGameBinding
    private lateinit var game: SnakeLogic
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var soundPool: SoundPool

    private var appleEatId: Int = 0
    private var gameOverId: Int = 0
    private var gameOver: Boolean = false

    override lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        this.binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(this.binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(this.binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        this.sharedPreferences = this.getSharedPreferences("best_score", Context.MODE_PRIVATE)

        this.binding.gamefield.score = this.binding.scoreboard
        this.game = SnakeLogic(this)
        this.binding.gamefield.game = game

        val selectedTheme: ThemeType? = intent.getSerializableExtra("theme") as ThemeType?
        this.binding.gamefield.theme = ThemeFactory.getTheme(selectedTheme ?: ThemeType.Default)

        this.mediaPlayer = MediaPlayer.create(this, R.raw.game_theme).apply {
            isLooping = true
        }

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        this.soundPool = SoundPool.Builder()
            .setMaxStreams(2)
            .setAudioAttributes(audioAttributes)
            .build()

        this.appleEatId = this.soundPool.load(this, R.raw.apple_eat, 0)
        this.gameOverId = this.soundPool.load(this, R.raw.game_over, 0)

        this.gameOver = false
        this.game.newGame()
        this.binding.gameOverMessage.visibility = View.INVISIBLE
        this.binding.gamefield.visibility = View.VISIBLE
    }

    override fun onClick(p0: View?) {
        if (this.gameOver) finish()
        when (p0!!.id) {
            R.id.button_up -> this.binding.gamefield.game.setDirection(Direction.Up)
            R.id.button_down -> this.binding.gamefield.game.setDirection(Direction.Down)
            R.id.button_left -> this.binding.gamefield.game.setDirection(Direction.Left)
            R.id.button_right -> this.binding.gamefield.game.setDirection(Direction.Right)
        }
    }

    override fun onPause() {
        super.onPause()
        this.game.pauseGame()
        this.mediaPlayer.pause()
        this.soundPool.autoPause()
    }

    override fun onResume() {
        super.onResume()
        if (!this.gameOver) {
            this.game.resumeGame()
        }
        this.mediaPlayer.start()
        this.soundPool.autoResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.mediaPlayer.release()
        this.soundPool.release()
    }

    override fun onScoreIncrease() {
        this.soundPool.play(this.appleEatId, 1f, 1f, 0, 0, 1f)
    }

    override fun onGameOver() {
        this.gameOver = true
        this.binding.gameOverMessage.visibility = View.VISIBLE
        this.binding.gamefield.visibility = View.INVISIBLE
        this.soundPool.play(this.gameOverId, 1f, 1f, 1, 0, 1f)

    }
}