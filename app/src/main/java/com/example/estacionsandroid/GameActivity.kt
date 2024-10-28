package com.example.estacionsandroid

import android.media.Image
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text
import java.util.Random

class GameActivity : AppCompatActivity() {

    private var colorList = mutableListOf(
        Item("blanco", 1),
        Item("amarillo", 2),
        Item("naranja", 3),
        Item("verde", 4)
    )

    private var figureList = mutableListOf(
        Item("snow", 1),
        Item("flower", 4),
        Item("sun", 2),
        Item("leaf", 3)
    )

    private var clothesList = mutableListOf(
        Item("swimsuit", 2),
        Item("scarf", 1),
        Item("flowertshirt", 4),
        Item("raincoat", 3)
    )

    private var tempList = mutableListOf<Item>()
    var level = 1
    var firstTime = true
    lateinit var item: Item


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gamelayout)

        tempList.addAll(colorList)
        setImage(tempList)
        val winterImage: ImageView = findViewById(R.id.imageWinter)
        val autumnImage: ImageView = findViewById(R.id.imageAutumn)
        val summerImage: ImageView = findViewById(R.id.imageSummer)
        val springImage: ImageView = findViewById(R.id.imageSpring)

        onClickListeners(winterImage, autumnImage, summerImage, springImage)
    }

    private fun onClickListeners(winterImage: ImageView, autumnImage: ImageView, summerImage: ImageView, springImage: ImageView) {
        winterImage.setOnClickListener{
            checkCorrect("Winter")
        }
        autumnImage.setOnClickListener{
            checkCorrect("Autumn")
        }
        summerImage.setOnClickListener{
            checkCorrect("Summer")
        }
        springImage.setOnClickListener{
            checkCorrect("Spring")
        }
    }

    private fun checkCorrect(season: String) {
        val condition= Pair(season, item.id)

        when(condition){
            Pair("Winter", 1) ->{
                //show correct feedback
                changeImage()
            }
            Pair("Summer", 2) ->{
                //show correct feedback
                changeImage()
            }
            Pair("Autumn", 3) ->{
                //show correct feedback
                changeImage()
            }
            Pair("Spring", 4) ->{
                //show correct feedback
                changeImage()
            }
            else -> {

            }
        }
    }

    private fun changeImage() {
        when (level) {
            1 -> {
                setImage(tempList)
            }

            2 -> {
                if (firstTime) {
                    tempList.addAll(figureList)
                }
                setImage(tempList)
            }
            3 -> {
                tempList.addAll(clothesList)
                setImage(tempList)
            }
            else -> println("wtf just , how did we get here")
        }
    }


    private fun setImage(data: List<Item>) {
        firstTime= false
        val randomIndex = Random().nextInt(data.size)

        val selectedItem = data[randomIndex]
        val imageView = findViewById<ImageView>(R.id.itemView)
        val imageResId = resources.getIdentifier(selectedItem.img, "drawable", packageName)
        item= data[randomIndex]

        imageView.setImageResource(imageResId)
        tempList.remove(data[randomIndex])
        if (tempList.isEmpty()) {
            level += 1
            firstTime= true
        }
    }


}