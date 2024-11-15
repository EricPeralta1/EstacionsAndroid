package com.example.estacionsandroid

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Random
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import com.google.gson.Gson
import java.io.File
import java.io.FileOutputStream
import android.os.Environment
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import java.io.FileReader


class GameActivity : AppCompatActivity() {

    private var clickable= true
    private lateinit var mediaPlayerBackgroundMusic: MediaPlayer
    private lateinit var mediaPlayerPopUpMusic: MediaPlayer



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
    val fileName = "Estacions.json"


    private var tempList = mutableListOf<Item>()
    private val seasonList = mutableListOf<ImageView>()
    lateinit var player: Player
    var level = 1
    var firstTime = true
    lateinit var item: Item
    var errors = 0
    var totalErrors = 0
    var hints = 0

    private var startTime: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gamelayout)
        mediaPlayerBackgroundMusic= MediaPlayer.create(this,R.raw.maingame_parasail)
        mediaPlayerBackgroundMusic.setVolume(40F, 40F)
        mediaPlayerBackgroundMusic.start()
        mediaPlayerBackgroundMusic.isLooping = true
        mediaPlayerBackgroundMusic.setVolume(2f, 2f)

        mediaPlayerPopUpMusic = MediaPlayer.create(this, R.raw.specialist) // Replace with actual popup music file
        mediaPlayerPopUpMusic.setVolume(3f, 3f)

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
        val playerName: String
        if (intent.getStringExtra("Avatar_Name")!= null){
         playerName= intent.getStringExtra("Avatar_Name").toString()
        } else  {
            playerName= "batman"
        }

        player = Player(playerName)
        player.date= SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().time)
        startTime = System.currentTimeMillis()
    }

    private fun getElapsedSeconds(): Int {
        val currentTime = System.currentTimeMillis()
        return ((currentTime - startTime) / 1000).toInt()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayerBackgroundMusic.pause()
        mediaPlayerPopUpMusic.pause()
    }

    override fun onResume() {
        super.onResume()
        mediaPlayerBackgroundMusic.start()
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
                image.visibility = View.INVISIBLE
                changeImage(seasonList)
                errors = 0

                clueWinterImage.clearAnimation()
                clueSummerImage.clearAnimation()
                clueAutumnImage.clearAnimation()
                clueSpringImage.clearAnimation()
            }

            else -> {
                errors += 1
                totalErrors += 1
                if (errors == 5) {
                    errors = 0
                    hints += 1
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
        if (seasonList.size >= 4) {
            for (item in seasonList) {
                item.visibility = View.VISIBLE
            }
            seasonList.clear()
        }

        when (level) {
            1 -> {
                setImage(tempList)
            }

            2 -> {

                mediaPlayerBackgroundMusic.pause()
                GlobalScope.launch(Dispatchers.Main) {
                    val itemView = findViewById<ImageView>(R.id.itemView)
                    if (firstTime) {
                        player.errors1 = totalErrors
                        player.usedHints1 = hints
                        player.time1 = getElapsedSeconds()
                        startTime = System.currentTimeMillis()
                        hints=0
                        totalErrors=0
                        clickable = false

                        itemView.visibility = View.INVISIBLE
                        showcongratsAnimation()
                        mediaPlayerPopUpMusic.start()
                        delay(4250)
                        mediaPlayerPopUpMusic.pause()
                        clickable = true
                        fadeoutcongratsAnimation()
                        tempList.addAll(figureList)
                    }
                    mediaPlayerBackgroundMusic.start()
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
                mediaPlayerBackgroundMusic.pause()
                GlobalScope.launch(Dispatchers.Main) {
                    val itemView = findViewById<ImageView>(R.id.itemView)
                    if (firstTime) {

                        player.errors2 = totalErrors
                        player.usedHints2 = hints
                        player.time2 = getElapsedSeconds()
                        startTime = System.currentTimeMillis()
                        hints=0
                        totalErrors=0
                        val icon1 = findViewById<ImageView>(R.id.iconauxtop)
                        val icon2 = findViewById<ImageView>(R.id.iconauxbottom)

                        icon1.setBackgroundResource(R.drawable.snowflakeicon)
                        icon2.setBackgroundResource(R.drawable.flowericon)

                        clickable = false

                        itemView.visibility = View.INVISIBLE
                        showcongratsAnimation()
                        mediaPlayerPopUpMusic.start()
                        delay(4250)
                        mediaPlayerPopUpMusic.pause()

                        clickable = true

                        fadeoutcongratsAnimation()
                        tempList.addAll(clothesList)
                    }
                    mediaPlayerBackgroundMusic.start()

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

                player.errors3 = totalErrors
                player.usedHints3 = hints
                player.time3 = getElapsedSeconds()
                startTime = System.currentTimeMillis()

                val avatarName = intent.getStringExtra("Avatar_Name")

                val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Estacions.json")
                println("Saving player data: ")
                println("Name: ${player.name}")
                println("Errors1: ${player.errors1}")
                println("Time1: ${player.time1}")
                println("UsedHints1: ${player.usedHints1}")
                println("Date: ${player.date}")

                // Read existing players from file (if any)
                val players = if (file.exists()) {
                    val reader = FileReader(file)
                    val gson = Gson()
                    val type = object : TypeToken<MutableList<Player>>() {}.type
                    gson.fromJson<MutableList<Player>>(reader, type) ?: mutableListOf()
                } else {
                    mutableListOf<Player>()  // If file doesn't exist, create a new list
                }

                // Step 2: Add the new player to the list
                players.add(player)

                // Step 3: Convert the list of players to JSON
                val gson = Gson()
                val json = gson.toJson(players)

                // Step 4: Write the updated list of players back to the file
                FileOutputStream(file).use { outputStream ->
                    outputStream.write(json.toByteArray())  // Write the JSON data to the file
                }

                println("Player added to JSON file: ${player.name}")
                val intent = Intent(this, EndGameActivity::class.java)
                intent.putExtra("Avatar_Name", avatarName)
                startActivity(intent)
                mediaPlayerBackgroundMusic.pause()
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
        animationSequenceText.addAnimation(fadeText)
        animationSequenceText.addAnimation(zoomText)
        animationSequenceText.duration = 500

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
        animationFadeOut.duration= 1000

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