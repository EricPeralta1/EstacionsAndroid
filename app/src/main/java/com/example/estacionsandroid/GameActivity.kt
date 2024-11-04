package com.example.estacionsandroid

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.Random

class GameActivity : AppCompatActivity() {

    private var colorList = mutableListOf(Item("blanco",  1),
        Item("amarillo",  2),
        Item("naranja",  3),
        Item("verde",  4))

    private var figureList = mutableListOf(Item("snow",  1),
        Item("flower",  2),
        Item("sun",  3),
        Item("leaf",  4))

    private var ClothesList = mutableListOf(Item("swimsuit",  1),
        Item("scarf",  2),
        Item("flowertshirt",  3),
        Item("raincoat", 4))


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gamelayout)

        setImage(figureList)

        val gameLayoutBackground = findViewById<ConstraintLayout>(R.id.gameLayoutBackground)
        val animationDrawable = gameLayoutBackground.background as AnimationDrawable
        animationDrawable.start()
    }


    private fun setImage(data: List<Item>) {
        val randomIndex = Random().nextInt(data.size)

        val selectedItem = data[randomIndex]

        val imageView = findViewById<ImageView>(R.id.itemView)
        val imageResId = resources.getIdentifier(selectedItem.img, "drawable", packageName)

        imageView.setImageResource(imageResId)
    }


}