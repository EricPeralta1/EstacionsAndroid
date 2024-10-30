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

        val avatarId = intent.getIntExtra("Avatar_ID",0)
        val avatarName = intent.getStringExtra("Avatar_Name")

        videoView.setOnCompletionListener {
            val intent = Intent(this, EndGameActivity::class.java)
            val bundle = Bundle()
            bundle.putInt("Avatar_ID", avatarId)
            bundle.putString("Avatar_Name", avatarName)
            intent.putExtras(bundle)
            startActivity(intent)
            finish()
        }

    }
}