package com.example.estacionsandroid

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.Random

class GameActivity : AppCompatActivity() {

    private var colorList = mutableListOf(Item("blanco",  1),
        Item("amarillo",  2),
        Item("naranja",  3),
        Item("verde",  4))

    private var figureList = mutableListOf(Item("coponieve",  1),
        Item("flor",  2),
        Item("sol",  3),
        Item("hoja",  4))

    private var ClothesList = mutableListOf(Item("banadorhombre",  1),
        Item("gorra",  2),
        Item("kimonoprim",  3),
        Item("raincoat", 4))


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gamelayout)

        setImage(figureList)

    }

    //Sdadsadd

    private fun setImage(data: List<Item>) {
        val randomIndex = Random().nextInt(data.size)

        val selectedItem = data[randomIndex]

        val imageView = findViewById<ImageView>(R.id.itemView)
        val imageResId = resources.getIdentifier(selectedItem.img, "drawable", packageName)

        imageView.setImageResource(imageResId)
    }


}