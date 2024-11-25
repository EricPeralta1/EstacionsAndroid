package com.example.estacionsandroid

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class IntroActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private var videoTime: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.introlayout)
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        videoView = findViewById(R.id.tutorialLevel)
        val videoPath : String = ("android.resource://" + packageName + "/" + R.raw.videotutorial)
        val uri : Uri = Uri.parse(videoPath)

        videoView.setVideoURI(uri)
        videoView.start()

        val avatarName = intent.getStringExtra("Avatar_Name")

        videoView.setOnCompletionListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("Avatar_Name", avatarName)
            startActivity(intent)
            finish()
        }

    }

    override fun onPause() {
        super.onPause()
        if (videoView.isPlaying) {
            videoTime = videoView.currentPosition
            videoView.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!videoView.isPlaying) {
            videoView.seekTo(videoTime)
            videoView.start()
        }
    }
}