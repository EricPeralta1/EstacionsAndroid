package com.example.estacionsandroid

import android.content.Intent
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class EndGameActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var soundPool: SoundPool
    private var soundId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.endgamelayout)
        mediaPlayer= MediaPlayer.create(this,R.raw.winscreentheme_dkultimatewin)
        mediaPlayer.setVolume(40F, 40F)
        mediaPlayer.start()

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
        val avatarImageView = findViewById<ImageView>(R.id.avatarPlaceholder)
        val avatarName = intent.getStringExtra("Avatar_Name")

        val avatarResourceId = resources.getIdentifier(avatarName, "drawable", packageName)
        avatarImageView.setImageResource(avatarResourceId)

        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .build()

        soundId = soundPool.load(this, R.raw.tapeffect, 1)

        val restartButton = findViewById<ImageView>(R.id.restartButton)

        restartButton.setOnClickListener {
            soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
            val intent = Intent(this, AvatarActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.start()
    }
}