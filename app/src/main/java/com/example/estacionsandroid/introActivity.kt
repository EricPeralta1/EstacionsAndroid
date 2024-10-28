package com.example.estacionsandroid

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.introlayout)

        val videoView: VideoView = findViewById(R.id.tutorialLevel)
        val videoPath : String = ("android.resource://" + packageName + "/" + R.raw.example)
        val uri : Uri = Uri.parse(videoPath)

        videoView.setVideoURI(uri)

        videoView.start()


        videoView.setOnCompletionListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}