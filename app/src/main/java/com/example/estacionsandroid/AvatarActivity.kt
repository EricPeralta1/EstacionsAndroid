package com.example.estacionsandroid

import android.os.Bundle
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity

class AvatarActivity : AppCompatActivity() {
    private lateinit var adapter : AvatarAdapter
    private lateinit var lstAvatar : GridView

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
        updateAdapter()

    }
    private fun updateAdapter(){
        lstAvatar = findViewById(R.id.AvatarView)
        adapter = AvatarAdapter(this,R.layout.avatarimage,avatarlist)
        lstAvatar.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}