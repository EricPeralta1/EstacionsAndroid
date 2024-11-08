package com.example.estacionsandroid

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.media.Image
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    private val seasonList = mutableListOf<ImageView>()
    var level = 1
    var firstTime = true
    lateinit var item: Item
    var errors = 0
    var totalErrors = 0
    var hints = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gamelayout)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
        tempList.addAll(colorList)
        setImage(tempList)
        val winterImage: ImageView = findViewById(R.id.imageWinter)
        val autumnImage: ImageView = findViewById(R.id.imageAutumn)
        val summerImage: ImageView = findViewById(R.id.imageSummer)
        val springImage: ImageView = findViewById(R.id.imageSpring)

        onClickListeners(winterImage, autumnImage, summerImage, springImage)
    }

    private fun onClickListeners(
        winterImage: ImageView,
        autumnImage: ImageView,
        summerImage: ImageView,
        springImage: ImageView
    ) {
        winterImage.setOnClickListener {
            checkCorrect("Winter", winterImage)
        }
        autumnImage.setOnClickListener {
            checkCorrect("Autumn", autumnImage)
        }
        summerImage.setOnClickListener {
            checkCorrect("Summer", summerImage)
        }
        springImage.setOnClickListener {
            checkCorrect("Spring", springImage)
        }
    }

    private fun checkCorrect(season: String, image: ImageView) {
        val condition = Pair(season, item.id)

        when (condition) {
            Pair("Winter", 1), Pair("Summer", 2), Pair("Autumn", 3), Pair("Spring", 4) -> {
                // show correct feedback
                seasonList.add(image)
                image.visibility = View.INVISIBLE
                changeImage(seasonList)
            }

            else -> {
                errors += 1
                totalErrors += 1
                if (errors == 5) {
                    errors = 0
                    hints + 1
                    showHint(condition.second)
                }

            }
        }
    }


    //Todo(Show visual effects for hints)
    private fun showHint(second: Int) {
        when (second) {
            //Check "checkCorrect()" for season order
            1 -> {}
            2 -> {}
            3 -> {}
            4 -> {}
        }
    }


    private fun changeImage(seasonList : MutableList<ImageView>) {
        if (seasonList.size >= 4){
            for (item in seasonList){
                item.visibility= View.VISIBLE
            }
            seasonList.clear()
        }

        when (level) {
            1 -> {
                setImage(tempList)
            }

            2 -> {
                GlobalScope.launch(Dispatchers.Main){
                if (firstTime) {

                        setCongratsImage()
                        delay(50)

                    tempList.addAll(figureList)
                }
                setImage(tempList)
                }
            }

            3 -> {
                GlobalScope.launch(Dispatchers.Main){
                    if (firstTime) {
                        setCongratsImage()
                        delay(50)

                        tempList.addAll(clothesList)
                    }
                    setImage(tempList)
                }
            }

            4 -> {
                val avatarName = intent.getStringExtra("Avatar_Name")
                val intent= Intent(this, EndGameActivity::class.java)
                intent.putExtra("Avatar_Name", avatarName)
                startActivity(intent)
                finish()

            }

            else -> println("wtf, just how did we get here")
        }


    }

    private fun setCongratsImage() {
        val imageView = findViewById<ImageView>(R.id.itemView)
        imageView.setImageResource(R.drawable.congratulations)
    }


    private fun setImage(data: List<Item>) {
        firstTime = false
        val randomIndex = Random().nextInt(data.size)

        val selectedItem = data[randomIndex]
        val imageView = findViewById<ImageView>(R.id.itemView)
        val imageResId = resources.getIdentifier(selectedItem.img, "drawable", packageName)
        item = data[randomIndex]

        imageView.setImageResource(imageResId)
        tempList.remove(data[randomIndex])
        if (tempList.isEmpty() && level < 4) {
            level += 1
            firstTime = true
        }
    }


}