package com.andre.dodgegame.Screens

import com.andre.dodgegame.DodgeGame
import com.andre.dodgegame.Model.Fireball
import com.andre.dodgegame.Model.Player
import com.andre.dodgegame.Model.SuperFireBall
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.utils.TimeUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class MainGameScreen(private val game: DodgeGame) : Screen {
//    var batch: SpriteBatch


    private var fireballImage: Texture
    private var superfireballImage: Texture
    private var playerImage: Texture

    private var beware: Sound
    private var dead: Sound
    private var fireballExplosion: Sound
    private var gameMusic: Music
//    private var camera: OrthographicCamera

    private var touchpad: Touchpad
    private var stage: Stage
    private var touchpadSkin = Skin()
    private var touchpadStyle = Touchpad.TouchpadStyle()
    private var touchBackground: Drawable
    private var touchKnob: Drawable
    private var fireballs:ArrayList<Fireball> = ArrayList()
    private var superfireballs:ArrayList<SuperFireBall> = ArrayList()
    private var lastFbTime:Long = 0
    private var lastSfbTime:Long = TimeUtils.nanoTime()
//    private var worldWidth = 1200f
//    private var worldHeight = 688f
    private var viewport: Viewport
    private var characterW = 32f
    private var characterH = 32f
//    private lateinit var imageMain: Texture
//    private lateinit var mainBackground: TextureRegion
    private var fb: TextureRegion
    private var sfb: TextureRegion
    private var player: Player
    private var red: Texture
//    private lateinit var blank: Texture
    private val timer = System.currentTimeMillis()
    private val gameLifeTime:Int = 60
    //    private lateinit var screen:Play
    private var map: TiledMap
    private var renderer: OrthogonalTiledMapRenderer
    private var time = 0
    private var fbSpeed = 3.5f
    //    private var joyStick:Joystick = Joystick()

    init {
//        batch = SpriteBatch()
        red = Texture(Gdx.files.internal("image/red.png"))
        val loader = TmxMapLoader()
        map = loader.load("maps/map.tmx")
        renderer = OrthogonalTiledMapRenderer(map,game.batch)
        fireballImage = Texture(Gdx.files.internal("image/Fireball.png"))
        fb  = TextureRegion(fireballImage)
        superfireballImage = Texture(Gdx.files.internal("image/Fireball2.png"))
        sfb  = TextureRegion(superfireballImage)
        playerImage = Texture(Gdx.files.internal("image/player.png"))
        beware = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/Beware.ogg"))
        dead = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/dead.ogg"))
        fireballExplosion = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/fireball-explosion.wav"))
        fireballExplosion.setVolume(0, 1.5f)

        //YouFulca work
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("Musics/Battle-deadly_loop.mp3"))
        gameMusic.isLooping = true
        gameMusic.play()
//        camera = OrthographicCamera()
//        Permet de voir combien de worldunit on va voir à travers la camera
//        camera.setToOrtho(false, 800f, 500f)
        player = Player(game.worldWidth / 2f - characterW / 2f, game.worldHeight / 2f - characterH / 2f, characterW, characterH, 3, 5)
        touchpadSkin.add("touchBackground", Texture(Gdx.files.internal("image/touchBackground.png")))
        touchpadSkin.add("touchKnob", Texture(Gdx.files.internal("image/touchKnob.png")))
        touchBackground = touchpadSkin.getDrawable("touchBackground")
        touchKnob = touchpadSkin.getDrawable("touchKnob")
        viewport = FitViewport(game.worldWidth, game.worldHeight)
        touchpadStyle.background = touchBackground
        touchpadStyle.knob = touchKnob
        touchpad = Touchpad(10f,touchpadStyle)
        touchpad.setBounds(15f,15f,200f,200f)
        stage = Stage(viewport, game.batch)
        stage.addActor(touchpad)
        Gdx.input.inputProcessor = stage

        spawnFireball()
    }

    private fun playerMovement(){
        val speed = player.getSpeed()
        if (player.x > game.worldWidth - player.width)
        {
            player.x = (game.worldWidth - player.width)
        }
        if(player.x < 0) player.x = 0f
        if (player.y > game.worldHeight - player.height)
        {
            player.y = (game.worldHeight - player.height)
        }
        if(player.y < 0) player.y = 0f
        player.x = (player.x + touchpad.knobPercentX* speed)
        player.y = (player.y + touchpad.knobPercentY* speed)
    }

    private fun spawnSuperFireBall(){
        superfireballs.add(SuperFireBall(MathUtils.random(0f, game.worldWidth - 32f), game.worldHeight, 50f, 32f, player.x, player.y, 3.5f))
        lastSfbTime = TimeUtils.nanoTime()
        beware.play()
    }
    private fun spawnFireball(){
        var rd: Fireball? = null
        
        when(Random().nextInt(4)){
            0 -> {
                rd = Fireball(MathUtils.random(0f, game.worldWidth - 32f), game.worldHeight, 50f, 32f, player.x, player.y, fbSpeed)
            }
            1 -> {
                rd = Fireball(MathUtils.random(0f, game.worldWidth - 32f), 0f, 50f, 32f, player.x, player.y, fbSpeed)
            }
            2 -> {
                rd = Fireball(game.worldWidth, MathUtils.random(0f, game.worldHeight - 32f), 50f, 32f, player.x, player.y, fbSpeed)
            }
            3 -> {
                rd = Fireball(0f, MathUtils.random(0f, game.worldHeight - 32f), 50f, 32f, player.x, player.y, fbSpeed)
            }
        }
//        val rd = Fireball(MathUtils.random(0f, worldWidth-64f) , worldHeight, 50f, 32f, player.x, player.y, 3.5f)
        if (rd != null) {
            fireballs.add(rd)
        }

        lastFbTime = TimeUtils.nanoTime()
    }

    override fun hide() {
    }

    override fun show() {
    }

    override fun render(delta: Float) {

//        time = gameLifeTime - (System.currentTimeMillis() - timer).toInt()/1000
        time = (System.currentTimeMillis() - timer).toInt()/1000
        val m = (time)/60
        val s= (time - m*60)
        val t = "${if (m <= 10) "0${m}" else "$m"} : ${if (s <= 10) "0${s}" else "$s"}"
        if(time == 0){
        }
//        ScreenUtils.clear(0f, 0f, 0f, 0f)
        game.camera.position.set((player.x+ (player.width/2)), (player.y+ (player.height/2)), 0f);
        game.camera.update()
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.setView(game.camera)
        renderer.render()

        game.batch.projectionMatrix = game.camera.combined

        game.batch.begin()
        game.batch.draw(red,game.camera.position.x - (game.camera.viewportWidth/2),game.camera.position.y + (game.camera.viewportHeight/2 - 15f), (game.camera.viewportWidth/player.maxHp() * player.getHp() ), 15f)


//        batch.draw(blank, 0f, 0f, worldWidth, worldHeight)
        // TIMER
        game.font.draw(game.batch, (t), game.camera.position.x - (game.camera.viewportWidth/8),game.camera.position.y + (game.camera.viewportHeight/2 - 25f))
        game.batch.draw(playerImage, player.x, player.y, player.width, player.height)
        game.batch.draw(red,game.camera.position.x - (game.camera.viewportWidth/2),game.camera.position.y + (game.camera.viewportHeight/2 - 15f), (game.camera.viewportWidth/player.maxHp() * player.getHp() ), 15f)
        fireballs.forEach {
//            batch.draw(fireballImage, it.x, it.y, it.width, it.height)
            game.batch.draw(fb, it.x, it.y, it.width/2, it.height/2, it.width, it.height, 1f, 1f, it.angle.toFloat())
//            val image2 = Image(fireballImage)
//            batch.draw(fireballImage, it.x, it.y, it.x, it.y, 64f, 64f, 1f, 2.0f, 45f, it.x.toInt(), it.y.toInt(), 64, 64, false, false)
//            image2.setPosition(it.x, it.y)
//            image2.width = it.width
//            image2.height = it.height
//            image2.rotation = 45F
//            stage.addActor(image2)

        }
        superfireballs.forEach {

            var dx = (player.x - it.x).toDouble()
            var dy = (player.y - it.y).toDouble()
            val angle = Vector2(dx.toFloat(), dy.toFloat())
            val angledeg = angle.angleDeg()
            game.batch.draw(sfb, it.x, it.y,  it.width/2, it.height/2, it.width, it.height, 1f, 1f, angledeg)
        }


        game.batch.end()
        stage.act(Gdx.graphics.deltaTime)
        stage.draw()
        playerMovement()
        if(TimeUtils.nanoTime() - lastFbTime > 800000000) spawnFireball()
        if(TimeUtils.nanoTime() - lastSfbTime > 15000000000) spawnSuperFireBall()
        if(TimeUtils.nanoTime() - lastSfbTime > 10000000000){
            superfireballs.clear()
        }
        val fireballIt:MutableIterator<Fireball> = fireballs.iterator()
        while(fireballIt.hasNext()){
            val fireball: Fireball = fireballIt.next()
            fireball.move()
            val dir = Vector2()
            dir.set(player.x, player.y)
            if(fireball.y +32 < 0 || fireball.y > game.worldHeight || fireball.x +32 < 0 || fireball.x > game.worldWidth) fireballIt.remove()
            if(fireball.overlaps(player)){
                fireballExplosion.play()
                if(player.isDead()){
                    dead.play()
                }
                fireballIt.remove()
            }
        }
        val superFireBallIt:MutableIterator<SuperFireBall> = superfireballs.iterator()
        while(superFireBallIt.hasNext()){
            val superfireball: SuperFireBall = superFireBallIt.next()
            val dx = (player.x - superfireball.x).toDouble()
            val dy = (player.y - superfireball.y).toDouble()
            val angle = atan2(dy.toFloat(), dx.toFloat())
            val velX = cos(angle) * 3
            val velY = sin(angle) * 3
            superfireball.x += velX
            superfireball.y += velY
            if(superfireball.overlaps(player)){
                fireballExplosion.play()
                if(player.isDead()){
//                    dead.play()
                    Gdx.app.exit()

                }

                superFireBallIt.remove()
            }

        }
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun dispose() {
//        batch.dispose()
        fireballImage.dispose()
        playerImage.dispose()
        fireballExplosion.dispose()
        beware.dispose()
        dead.dispose()
        gameMusic.dispose()
        map.dispose()
        renderer.dispose()
    }

}