package com.example.estacionsandroid

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class EndGameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.endgamelayout)
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
        val avatarImageView = findViewById<ImageView>(R.id.avatarPlaceholder)

        val bundle: Bundle? = intent.extras
        val avatarId = bundle?.getInt("Avatar_ID", 0)
        val avatarName = bundle?.getString("Avatar_Name")



            val avatarResourceId = resources.getIdentifier(avatarName, "drawable", packageName)
            avatarImageView.setImageResource(avatarResourceId)



        val restartButton = findViewById<Button>(R.id.restartButton)
        restartButton.setOnClickListener {
            val intent = Intent(this, AvatarActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}