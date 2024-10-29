package com.example.estacionsandroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
    private lateinit var avatarCopy: ImageView

    private var avatarlist = mutableListOf(
        Avatar("batman",1),
        Avatar("mate",  2),
        Avatar("merida",  4),
        Avatar("pikachu",5),
        Avatar("bluey",6),
        Avatar("cenicienta",7),
        Avatar("cruzramirez",8),
        Avatar("francesco",9),
        Avatar("ironman",10),
        Avatar("jasmine",11),
        Avatar("rayomcqueen",12),
        Avatar("sally",13),
        Avatar("simba",14),
        Avatar("spiderman",15),
        Avatar("stitch",16),
        Avatar("superman",18))


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.avatarlayout)
        avatarCopy = findViewById(R.id.avatarCopy)
        confirmAvatarLayout = findViewById(R.id.confirmAvatarLayout)
        updateAdapter()
        setupAvatarClickListener()
    }



    private fun updateAdapter(){
        lstAvatar = findViewById(R.id.AvatarView)
        adapter = AvatarAdapter(this,R.layout.avatarimage,avatarlist)
        lstAvatar.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun setupAvatarClickListener() {
        lstAvatar.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val avatarId = resources.getIdentifier(avatarlist[position].name, "drawable", packageName)
            avatarCopy.setImageResource(avatarId)

            val selectedAvatar= avatarlist[position]

            val pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_animation)
            confirmAvatarLayout.startAnimation(pulseAnimation)
            setupStartGameClickListener(selectedAvatar.id, selectedAvatar.name)
        }
    }

    private fun setupStartGameClickListener(avatarId: Int, avatarName: String) {
        confirmAvatarLayout.setOnClickListener{
            val intent= Intent(this, IntroActivity::class.java)
            intent.putExtra("Avatar_ID", avatarId)
            intent.putExtra("Avatar_Name", avatarName)
            startActivity(intent)
            Log.d("AvatarActivity", "Starting IntroActivity with Avatar_ID: $avatarId and Avatar_Name: $avatarName")

            finish()
        }
    }

}