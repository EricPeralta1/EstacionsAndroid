package com.example.estacionsandroid

import android.content.Intent
import android.os.Bundle
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
        Avatar("merida",  3),
        Avatar("pikachu",4),
        Avatar("bluey",5),
        Avatar("cenicienta",6),
        Avatar("cruzramirez",7),
        Avatar("francesco",8),
        Avatar("ironman",9),
        Avatar("jasmine",10),
        Avatar("rayomcqueen",11),
        Avatar("sally",12),
        Avatar("simba",13),
        Avatar("spiderman",14),
        Avatar("stitch",15),
        Avatar("superman",16))


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

            finish()
        }
    }
}