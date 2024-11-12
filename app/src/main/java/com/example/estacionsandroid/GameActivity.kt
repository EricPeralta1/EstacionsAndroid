package com.example.estacionsandroid

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Random

class GameActivity : AppCompatActivity() {

    private var clickable= true

    private var colorList = mutableListOf(
        Item("blanco", 1),
        Item("amarillo", 2),
        Item("naranja", 3),
        Item("rosa", 4)
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
            if (clickable){
            checkCorrect("Winter", winterImage)}
        }
        autumnImage.setOnClickListener {
            if (clickable){
            checkCorrect("Autumn", autumnImage)}
        }
        summerImage.setOnClickListener {
            if (clickable){
            checkCorrect("Summer", summerImage)}
        }
        springImage.setOnClickListener {
            if (clickable){
            checkCorrect("Spring", springImage)}
        }
    }

    private fun checkCorrect(season: String, image: ImageView) {
        val condition = Pair(season, item.id)
        val clueWinterImage: ImageView = findViewById(R.id.clueWinter)
        val clueSummerImage: ImageView = findViewById(R.id.clueSummer)
        val clueAutumnImage: ImageView = findViewById(R.id.clueAutumn)
        val clueSpringImage: ImageView = findViewById(R.id.clueSpring)

        when (condition) {
            Pair("Winter", 1), Pair("Summer", 2), Pair("Autumn", 3), Pair("Spring", 4) -> {
                seasonList.add(image)
                errors = 0
                image.visibility = View.INVISIBLE

                clueWinterImage.clearAnimation()
                clueSummerImage.clearAnimation()
                clueAutumnImage.clearAnimation()
                clueSpringImage.clearAnimation()

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


    private fun showHint(second: Int) {
        val clueWinterImage: ImageView = findViewById(R.id.clueWinter)
        val clueSummerImage: ImageView = findViewById(R.id.clueSummer)
        val clueAutumnImage: ImageView = findViewById(R.id.clueAutumn)
        val clueSpringImage: ImageView = findViewById(R.id.clueSpring)

        val clueAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_animation)

        when (second) {
            1 -> {
                clueWinterImage.startAnimation(clueAnimation)
            }
            2 -> {
                clueSummerImage.startAnimation(clueAnimation)
            }
            3 -> {
                clueAutumnImage.startAnimation(clueAnimation)
            }
            4 -> {
                clueSpringImage.startAnimation(clueAnimation)
            }
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
                    val itemView = findViewById<ImageView>(R.id.itemView)
                    if (firstTime) {

                    itemView.visibility = View.INVISIBLE
                        showcongratsAnimation()
                        clickable=false
                        delay(4250)
                        clickable=true

                    fadeoutcongratsAnimation()
                    tempList.addAll(figureList)
                }

                    val congratsView = findViewById<ImageView>(R.id.congratulationstext)
                    val iconauxView1 = findViewById<ImageView>(R.id.iconauxtop)
                    val iconauxView2 = findViewById<ImageView>(R.id.iconauxbottom)
                    congratsView.visibility = View.INVISIBLE
                    iconauxView1.visibility = View.INVISIBLE
                    iconauxView2.visibility = View.INVISIBLE
                    itemView.visibility = View.VISIBLE

                setImage(tempList)
                }
            }

            3 -> {
                GlobalScope.launch(Dispatchers.Main){
                    val itemView = findViewById<ImageView>(R.id.itemView)
                    if (firstTime) {
                        val icon1 = findViewById<ImageView>(R.id.iconauxtop)
                        val icon2 = findViewById<ImageView>(R.id.iconauxbottom)

                        icon1.setBackgroundResource(R.drawable.snowflakeicon)
                        icon2.setBackgroundResource(R.drawable.flowericon)

                        clickable=false

                        itemView.visibility = View.INVISIBLE
                        showcongratsAnimation()
                        delay(4250)

                        clickable=true


                        fadeoutcongratsAnimation()
                        tempList.addAll(clothesList)
                    }

                    val congratsView = findViewById<ImageView>(R.id.congratulationstext)
                    val iconauxView1 = findViewById<ImageView>(R.id.iconauxtop)
                    val iconauxView2 = findViewById<ImageView>(R.id.iconauxbottom)
                    congratsView.visibility = View.INVISIBLE
                    iconauxView1.visibility = View.INVISIBLE
                    iconauxView2.visibility = View.INVISIBLE
                    itemView.visibility = View.VISIBLE


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

    private fun showcongratsAnimation() {
        val congratsView = findViewById<ImageView>(R.id.congratulationstext)
        val iconauxView1 = findViewById<ImageView>(R.id.iconauxtop)
        val iconauxView2 = findViewById<ImageView>(R.id.iconauxbottom)

        val animationSequenceText = AnimationSet(false)
        val fadeText = AnimationUtils.loadAnimation(this, R.anim.fade_animation)
        val zoomText = AnimationUtils.loadAnimation(this, R.anim.zoom_animation)
        animationSequenceText.duration = 500
        animationSequenceText.addAnimation(fadeText)
        animationSequenceText.addAnimation(zoomText)

        val animationSequenceIcons = AnimationSet(false)
        val fadeIcons = AnimationUtils.loadAnimation(this, R.anim.fade_animation)
        val rotateIcons = AnimationUtils.loadAnimation(this, R.anim.rotate_animation)
        animationSequenceIcons.addAnimation(fadeIcons)
        animationSequenceIcons.addAnimation(rotateIcons)

        congratsView.startAnimation(animationSequenceText)
        congratsView.visibility = View.VISIBLE


        iconauxView1.startAnimation(animationSequenceIcons)
        iconauxView2.startAnimation(animationSequenceIcons)
        iconauxView1.visibility = View.VISIBLE
        iconauxView2.visibility = View.VISIBLE
    }

    private fun fadeoutcongratsAnimation(){
        val congratsView = findViewById<ImageView>(R.id.congratulationstext)
        val iconauxView1 = findViewById<ImageView>(R.id.iconauxtop)
        val iconauxView2 = findViewById<ImageView>(R.id.iconauxbottom)
        val animationFadeOut = AnimationSet(false)
        val fadeOut  = AnimationUtils.loadAnimation(this, R.anim.fadeout_animation)
        animationFadeOut.addAnimation(fadeOut)
        animationFadeOut.duration = 1000

        congratsView.startAnimation(animationFadeOut)
        iconauxView1.startAnimation(animationFadeOut)
        iconauxView2.startAnimation(animationFadeOut)
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