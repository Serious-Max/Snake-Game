package com.example.snakegame

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.snakegame.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity(), OnClickListener {
    private lateinit var binding: ActivityMenuBinding
    private lateinit var bestScoreView: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var mediaPlayer: MediaPlayer

    private val themes = ThemeType.entries
    private var selectedTheme = themes[0]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        this.binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(this.binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(this.binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bestScoreView = this.binding.menuBestScore
        sharedPreferences = this.getSharedPreferences("best_score", Context.MODE_PRIVATE)
        this.mediaPlayer = MediaPlayer.create(this, R.raw.menu_theme).apply {
            isLooping = true
        }

        val adapter =
            object : ArrayAdapter<ThemeType>(this, android.R.layout.simple_spinner_item, themes) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = convertView ?: LayoutInflater.from(context)
                        .inflate(android.R.layout.simple_spinner_item, parent, false)
                    val textView = view.findViewById<TextView>(android.R.id.text1)
                    val themeId =
                        resources.getIdentifier(themes[position].themeId, "string", packageName)
                    textView.text = getString(themeId)
                    return view
                }

                override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
                ): View {
                    val view = convertView ?: LayoutInflater.from(context)
                        .inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)
                    val textView = view.findViewById<TextView>(android.R.id.text1)
                    val themeId =
                        resources.getIdentifier(themes[position].themeId, "string", packageName)
                    textView.text = getString(themeId)
                    return view
                }
            }.also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                val themeSelector: Spinner = this.binding.themeSelector
                themeSelector.adapter = adapter
                themeSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        selectedTheme = themes[position]
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                    }
                }

            }
    }

    override fun onStart() {
        super.onStart()
        val previousBest = sharedPreferences.getInt("best_score", 0)
        bestScoreView.text = getString(R.string.best_score, previousBest)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.button_start -> this.startNewGame()
            R.id.button_share -> this.share()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.mediaPlayer.release()
    }

    override fun onResume() {
        super.onResume()
        this.mediaPlayer.start()
    }

    override fun onPause() {
        super.onPause()
        this.mediaPlayer.pause()
    }

    private fun startNewGame() {
        val intent = Intent(this, GameActivity::class.java).apply {
            putExtra("theme", selectedTheme)
        }
        startActivity(intent)
    }

    private fun share() {
        val previousBest = sharedPreferences.getInt("best_score", 0)
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text, previousBest))
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_title)))
    }
}