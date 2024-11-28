package com.example.estacionsandroid

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
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

    private var clickable = true
    private lateinit var mediaPlayerBackgroundMusic: MediaPlayer
    private lateinit var mediaPlayerPopUpMusic: MediaPlayer
    private lateinit var mediaPlayerNarratorHint: MediaPlayer
    private lateinit var mediaPlayerNarratorLvlComplete: MediaPlayer
    private lateinit var soundPool: SoundPool
    private lateinit var soundPoolcolors: SoundPool
    private var soundIdTap: Int = 0
    private var soundIdCorrect: Int = 0




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
    private var soundMap = hashMapOf<Int, Int>()
    var level = 1
    var firstTime = true
    lateinit var item: Item
    var errors = 0
    var totalErrors = 0
    var hints = 0
    lateinit var draggedImageView: ImageView
    private var startTime: Long = 0


    private var canCheckCollision = true
    private val collisionCooldown = 500L  // 500 milliseconds cooldown
    private val handler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gamelayout)
        mediaPlayerBackgroundMusic = MediaPlayer.create(this, R.raw.maingame_parasail)
        mediaPlayerBackgroundMusic.setVolume(0.2F, 0.2F)
        mediaPlayerBackgroundMusic.start()
        mediaPlayerBackgroundMusic.isLooping = true

        mediaPlayerPopUpMusic = MediaPlayer.create(this, R.raw.nextlevelsoundeffect)
        mediaPlayerPopUpMusic.setVolume(0.2f, 0.2f)

        soundPool = SoundPool.Builder()
            .setMaxStreams(2)
            .build()

        soundIdTap = soundPool.load(this, R.raw.tapeffect, 1)
        soundIdCorrect = soundPool.load(this, R.raw.correctefect, 1)

        soundPoolcolors = SoundPool.Builder().setMaxStreams(5).build()

        soundMap[1] = soundPoolcolors.load(this, R.raw.blanc, 1)
        soundMap[2] = soundPoolcolors.load(this, R.raw.groc, 1)
        soundMap[3] = soundPoolcolors.load(this, R.raw.taronja, 1)
        soundMap[4] = soundPoolcolors.load(this, R.raw.rosa, 1)

        mediaPlayerNarratorHint= MediaPlayer.create(this,R.raw.narratorhint)
        mediaPlayerNarratorHint.setVolume(1f, 1f)
        mediaPlayerNarratorLvlComplete= MediaPlayer.create(this,R.raw.narratorwin)
        mediaPlayerNarratorLvlComplete.setVolume(1f, 1f)

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
        if (intent.getStringExtra("Avatar_Name") != null) {
            playerName = intent.getStringExtra("Avatar_Name").toString()
        } else {
            playerName = "batman"
        }

        player = Player(playerName)
        // Eg. 27/11/2024 14:30:45
       player.date = SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
        player.time= SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        startTime = System.currentTimeMillis()
    }

    private fun getElapsedSeconds(): Int {
        val currentTime = System.currentTimeMillis()
        return ((currentTime - startTime) / 1000).toInt()
    }

    var firstTimeDrag = true
    var originalX = 0f
    var originalY = 0f


    @SuppressLint("ClickableViewAccessibility")
    private fun makeDraggable(imageView: ImageView) {
        imageView.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Store the original position
                    if (firstTimeDrag) {
                        originalX = view.x
                        originalY = view.y
                    firstTimeDrag= false;
                    }
                    draggedImageView= imageView
                }

                MotionEvent.ACTION_MOVE -> {
                    view.x = motionEvent.rawX - view.width / 2
                    view.y = motionEvent.rawY - view.height / 2

                    if (canCheckCollision) {
                        // Perform collision check
                        if (checkCollisions(imageView)) {

                            imageView.setOnTouchListener(null)
                            view.x = originalX
                            view.y = originalY
                        }

                        // Set cooldown
                        canCheckCollision = false
                        handler.postDelayed({ canCheckCollision = true }, collisionCooldown)
                    }
                }

                MotionEvent.ACTION_UP -> {
                    // Reset to original position if the drag is released
                    view.x = originalX
                    view.y = originalY
                }
            }
            true
        }
    }


    private fun checkCollisions(draggableView: ImageView): Boolean {
        // Check for collisions with each season ImageView
        val winterImage: ImageView = findViewById(R.id.imageWinter)
        val autumnImage: ImageView = findViewById(R.id.imageAutumn)
        val summerImage: ImageView = findViewById(R.id.imageSummer)
        val springImage: ImageView = findViewById(R.id.imageSpring)

        if (isCollision(draggableView, winterImage)) {
            checkCorrect("Winter", winterImage)
            return true
        } else if (isCollision(draggableView, autumnImage)) {
            checkCorrect("Autumn", autumnImage)
            return true
        } else if (isCollision(draggableView, summerImage)) {
            checkCorrect("Summer", summerImage)
            return true
        } else if (isCollision(draggableView, springImage)) {
            checkCorrect("Spring", springImage)
            return true
        }
        return false
    }

    private fun isCollision(view1: ImageView, view2: ImageView): Boolean {
        val rect1 = android.graphics.Rect()
        val rect2 = android.graphics.Rect()

        view1.getGlobalVisibleRect(rect1)
        view2.getGlobalVisibleRect(rect2)

        val hitboxScaleFactor =
            0.20f // Adjust this value for the desired hitbox size (e.g., 50% smaller)
        val widthReduction = (rect1.width() * (1 - hitboxScaleFactor)).toInt() / 2
        val heightReduction = (rect1.height() * (1 - hitboxScaleFactor)).toInt() / 2

        rect1.inset(widthReduction, heightReduction)

        return rect1.intersect(rect2)
    }

    override fun onPause() {
        super.onPause()
        mediaPlayerBackgroundMusic.pause()

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
            if (clickable) {
                checkCorrect("Winter", winterImage)
                soundPool.play(soundIdTap, 1f, 1f, 0, 0, 1f)

            }
        }
        autumnImage.setOnClickListener {
            if (clickable) {
                checkCorrect("Autumn", autumnImage)
                soundPool.play(soundIdTap, 1f, 1f, 0, 0, 1f)

            }
        }
        summerImage.setOnClickListener {
            if (clickable) {
                checkCorrect("Summer", summerImage)
                soundPool.play(soundIdTap, 1f, 1f, 0, 0, 1f)

            }
        }
        springImage.setOnClickListener {
            if (clickable) {
                checkCorrect("Spring", springImage)
                soundPool.play(soundIdTap, 1f, 1f, 0, 0, 1f)

            }
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

                soundPoolcolors.stop(item.id)
                soundPool.play(soundIdCorrect, 1f, 1f, 0, 0, 1f)

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
                    mediaPlayerBackgroundMusic.setVolume(0.2F,0.2F)
                    mediaPlayerNarratorHint.start()
                    mediaPlayerNarratorHint.setOnCompletionListener {
                        mediaPlayerBackgroundMusic.setVolume(0.2F,0.2F)
                    }
                }
                handler.postDelayed({
                    makeDraggable(draggedImageView) // Reapply the draggable behavior
                }, 500)
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


    private fun changeImage(seasonList: MutableList<ImageView>) {
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
                GlobalScope.launch(Dispatchers.Main){
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
                        mediaPlayerNarratorLvlComplete.start()
                        mediaPlayerNarratorLvlComplete.setVolume(0.7f,0.7f)
                        delay(4250)

                        mediaPlayerPopUpMusic.pause()
                        mediaPlayerNarratorLvlComplete.pause()
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
                GlobalScope.launch(Dispatchers.Main){
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

                        clickable=false

                        itemView.visibility = View.INVISIBLE
                        showcongratsAnimation()
                        mediaPlayerBackgroundMusic.pause()
                        mediaPlayerPopUpMusic.start()
                        mediaPlayerNarratorLvlComplete.start()
                        delay(4250)

                        mediaPlayerPopUpMusic.stop()
                        mediaPlayerNarratorLvlComplete.stop()
                        mediaPlayerBackgroundMusic.start()
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

                player.errors3 = totalErrors
                player.usedHints3 = hints
                player.time3 = getElapsedSeconds()
                startTime = System.currentTimeMillis()
                val avatarName = intent.getStringExtra("Avatar_Name")

                val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Estacions.json")
                // Read existing players from file (if any)
                val players = if (file.exists()) {
                    val reader = FileReader(file)
                    val gson = Gson()
                    val type = object : TypeToken<MutableList<Player>>() {}.type
                    gson.fromJson<MutableList<Player>>(reader, type) ?: mutableListOf()
                } else {
                    mutableListOf<Player>()
                }

                players.add(player)
                val gson = Gson()
                val json = gson.toJson(players)


                FileOutputStream(file).use { outputStream ->
                    outputStream.write(json.toByteArray())
                }

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
        draggedImageView= imageView
        handler.postDelayed({
            makeDraggable(imageView)

        },500
        )

        mediaPlayerBackgroundMusic.setVolume(0.2f, 0.2f)

        if (level==1){
        when (selectedItem.id) {

            1 -> playSound(1)
            2 -> playSound(2)
            3 -> playSound(3)
            4 -> playSound(4)
        }
    }

        if (tempList.isEmpty() && level < 4) {
            level += 1
            firstTime = true
        }
    }

    private fun playSound(itemId: Int) {
        Handler(Looper.getMainLooper()).postDelayed({

            soundMap[itemId]?.let { soundId ->
                soundPoolcolors.play(soundId, 1f, 1f, 1, 0, 1f)
            }
        }, 1000)

    }
}