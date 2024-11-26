package com.example.estacionsandroid

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class AvatarActivity : AppCompatActivity() {
    private lateinit var adapter : AvatarAdapter
    private lateinit var lstAvatar : GridView
    private lateinit var confirmAvatarLayout: LinearLayout
    private lateinit var startButton: ImageView
    private lateinit var avatarCopy: ImageView
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var mediaPlayerIntro: MediaPlayer
    private lateinit var soundPool: SoundPool
    private var soundId: Int = 0



    private var avatarlist = mutableListOf(
        Avatar("batman"),
        Avatar("mate"),
        Avatar("merida"),
        Avatar("pikachu"),
        Avatar("bluey"),
        Avatar("cenicienta"),
        Avatar("cruzramirez"),
        Avatar("francesco"),
        Avatar("ironman"),
        Avatar("jasmine"),
        Avatar("rayomcqueen"),
        Avatar("sally"),
        Avatar("simba"),
        Avatar("spiderman"),
        Avatar("stitch"),
        Avatar("superman",))


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.avatarlayout)
        mediaPlayer= MediaPlayer.create(this,R.raw.menutheme)
        mediaPlayer.start()
        mediaPlayer.isLooping = true
        mediaPlayer.setVolume(0.5f, 0.5f)
        mediaPlayerIntro = MediaPlayer.create(this,R.raw.introvoice)
        mediaPlayerIntro.start()
        mediaPlayerIntro.setOnCompletionListener {
            mediaPlayer.setVolume(0.7f, 0.7f)
        }


        avatarCopy = findViewById(R.id.avatarCopy)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        avatarCopy.visibility = ImageView.INVISIBLE
        startButton = findViewById(R.id.startButton)
        startButton.visibility = ImageView.INVISIBLE



        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .build()

        soundId = soundPool.load(this, R.raw.tapeffect, 1)

        confirmAvatarLayout = findViewById(R.id.confirmAvatarLayout)
        updateAdapter()
        setupAvatarClickListener()

    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.start()
    }
    private fun updateAdapter(){
        lstAvatar = findViewById(R.id.AvatarView)
        adapter = AvatarAdapter(this,R.layout.avatarimage,avatarlist)
        lstAvatar.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun setupAvatarClickListener() {
        lstAvatar.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

            soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
            val avatarId = resources.getIdentifier(avatarlist[position].name, "drawable", packageName)
            avatarCopy.setImageResource(avatarId)
            avatarCopy.visibility = ImageView.VISIBLE
            startButton.visibility = ImageView.VISIBLE
            createAvatarSelectedAnimation()


            val selectedAvatar= avatarlist[position]

            setupStartGameClickListener(selectedAvatar.name)
        }
    }
    private fun setupStartGameClickListener(avatarName: String) {
        confirmAvatarLayout.setOnClickListener{
            val intent= Intent(this, IntroActivity::class.java)
            intent.putExtra("Avatar_Name", avatarName)
            startActivity(intent)
            mediaPlayer.stop()
            mediaPlayerIntro.stop()

            finish()
        }
    }

    private fun createAvatarSelectedAnimation(){
        val backgroundAnimation = findViewById<ImageView>(R.id.avatarCopy)

        val fadeIn = ObjectAnimator.ofFloat(backgroundAnimation, "alpha", 0f, 1f)
        fadeIn.duration=250

        val leftAvatarSelected = ObjectAnimator.ofFloat(backgroundAnimation, "rotation", -20f)
        leftAvatarSelected.duration = 250

        val centerAvatarSelected = ObjectAnimator.ofFloat(backgroundAnimation, "rotation", 0f)
        leftAvatarSelected.duration = 250

        val rightAvatarSelected = ObjectAnimator.ofFloat(backgroundAnimation, "rotation", 20f)
        leftAvatarSelected.duration = 250

        val endAvatarSelected = ObjectAnimator.ofFloat(backgroundAnimation, "rotation", 0f)
        leftAvatarSelected.duration = 250

        val animationSequence = AnimatorSet()
        animationSequence.playSequentially(fadeIn, leftAvatarSelected, centerAvatarSelected, rightAvatarSelected, endAvatarSelected)
        animationSequence.start()
    }
}
