package com.example.estacionsandroid

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class introActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.introlayout)

        startVideoTutorial()
    }

    private fun startVideoTutorial(){
        videoView = findViewById(R.id.tutorialLevel)

        val videoUri: Uri = Uri.parse("android.resource://res/" + R.raw.tutorial)
        videoView.setVideoURI(videoUri)

        videoView.setOnCompletionListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
            finish()
        }

        videoView.start()

    }


}