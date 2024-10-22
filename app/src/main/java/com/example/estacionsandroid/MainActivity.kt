package com.example.estacionsandroid

import android.os.Bundle
import android.widget.GridView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var adapter : AvatarAdapter
    private lateinit var lstAvatar : GridView


    private var avatarlist = mutableListOf(Avatar("batman",1),
        Avatar("mate",  2),
        Avatar("sully",  3),
        Avatar("merida",  4),
        Avatar("pikachu",5),
        Avatar("batman",6),
        Avatar("bluey",7),
        Avatar("cenicienta",8),
        Avatar("cruzramirez",9),
        Avatar("francesco",10),
        Avatar("ironman",11),
        Avatar("jasmine",12),
        Avatar("rayomcqueen",13),
        Avatar("sally",14),
        Avatar("simba",15),
        Avatar("spiderman",16),
        Avatar("stitch",17),
        Avatar("storm",18),
        Avatar("superman",19))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.avatarlayout)

        updateAdapter(avatarlist)

    }
    private fun updateAdapter( data: List<Avatar>){
        lstAvatar = findViewById(R.id.AvatarView)
        adapter = AvatarAdapter(this,R.layout.avatarimage,avatarlist)
        lstAvatar.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}