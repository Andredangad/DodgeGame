package com.andre.dodgegame

import com.andre.dodgegame.Model.Fireball
import com.andre.dodgegame.Model.Player
import com.andre.dodgegame.Model.SuperFireBall
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
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


class DodgeGame() : ApplicationAdapter() {
    lateinit var batch: SpriteBatch
    private lateinit var fireballImage: Texture
    private lateinit var superfireballImage: Texture
    private lateinit var playerImage: Texture

    private lateinit var beware: Sound
    private lateinit var dead: Sound
    private lateinit var fireballExplosion: Sound
    private lateinit var gameMusic: Music
    private lateinit var camera: OrthographicCamera
    private lateinit var touchpad: Touchpad
    private lateinit var stage: Stage
    private var touchpadSkin = Skin()
    private var touchpadStyle = Touchpad.TouchpadStyle()
    private lateinit var touchBackground: Drawable
    private lateinit var touchKnob: Drawable
    private var fireballs:ArrayList<Fireball> = ArrayList()
    private var superfireballs:ArrayList<SuperFireBall> = ArrayList()
    private var lastFbTime:Long = 0
    private var lastSfbTime:Long = TimeUtils.nanoTime()
    private var touchPos: Vector3 = Vector3()
    private var worldWidth = 1200f
    private var worldHeight = 688f
    private lateinit var viewport: Viewport
    private var characterW = 32f
    private var characterH = 32f
    private lateinit var imageMain:Texture
    private lateinit var mainBackground:TextureRegion
    private lateinit var fb:TextureRegion
    private lateinit var sfb:TextureRegion
    private lateinit var player: Player
    private lateinit var red:Texture
    private lateinit var blank:Texture
    private lateinit var font:BitmapFont
    private val timer = System.currentTimeMillis()
    private val gameLifeTime:Int = 60
//    private lateinit var screen:Play
    private lateinit var map: TiledMap
    private  lateinit var renderer: OrthogonalTiledMapRenderer

    //    private var joyStick:Joystick = Joystick()

    override fun create() {
//context.
//        attr.font = BitmapFont(Gdx.files.internal("Calibri.fnt"), Gdx.files.internal("Calibri.png"), false)

        font = BitmapFont()
        font.color = Color.GREEN
        batch = SpriteBatch()
        red = Texture(Gdx.files.internal("image/red.png"))
        val loader = TmxMapLoader()
        map = loader.load("maps/map.tmx")
        renderer = OrthogonalTiledMapRenderer(map,batch)
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
        camera = OrthographicCamera()
//        Permet de voir combien de worldunit on va voir à travers la camera
        camera.setToOrtho(false, 800f, 500f)
        player = Player(worldWidth / 2f - characterW / 2f, worldHeight / 2f - characterH / 2f, characterW, characterH, 3, 5)
        touchpadSkin.add("touchBackground", Texture(Gdx.files.internal("image/touchBackground.png")))
        touchpadSkin.add("touchKnob", Texture(Gdx.files.internal("image/touchKnob.png")))
        touchBackground = touchpadSkin.getDrawable("image/touchBackground")
        touchKnob = touchpadSkin.getDrawable("image/touchKnob")
        viewport = FitViewport(worldWidth, worldHeight)
        touchpadStyle.background = touchBackground
        touchpadStyle.knob = touchKnob
        touchpad = Touchpad(10f,touchpadStyle)
        touchpad.setBounds(15f,15f,200f,200f)
        stage = Stage(viewport, batch)
        stage.addActor(touchpad)
        Gdx.input.inputProcessor = stage

        spawnFireball()
    }

    private fun playerMovement(){
        val speed = player.getSpeed()
        if (player.x > worldWidth - player.width)
        {
            player.x = (worldWidth - player.width)
        }
        if(player.x < 0) player.x = 0f
        if (player.y > worldHeight - player.height)
        {
            player.y = (worldHeight - player.height)
        }
        if(player.y < 0) player.y = 0f
        player.x = (player.x + touchpad.knobPercentX* speed)
        player.y = (player.y + touchpad.knobPercentY* speed)
    }

    override fun render() {

        val time: Int = gameLifeTime - (System.currentTimeMillis() - timer).toInt()/1000
        if(time == 0){
        }
//        ScreenUtils.clear(0f, 0f, 0f, 0f)
        camera.position.set((player.x+ (player.width/2)), (player.y+ (player.height/2)), 0f);
        camera.update()
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.setView(camera)
        renderer.render()

        batch.projectionMatrix = camera.combined

        batch.begin()
        batch.draw(red,camera.position.x - (camera.viewportWidth/2),camera.position.y + (camera.viewportHeight/2 - 15f), (camera.viewportWidth/player.maxHp() * player.getHp() ), 15f)


//        batch.draw(blank, 0f, 0f, worldWidth, worldHeight)
        // TIMER
        font.draw(batch, (time).toString(),camera.position.x,camera.position.y + (camera.viewportHeight/2 - 25f))
        batch.draw(playerImage, player.x, player.y, player.width, player.height)
        batch.draw(red,camera.position.x - (camera.viewportWidth/2),camera.position.y + (camera.viewportHeight/2 - 15f), (camera.viewportWidth/player.maxHp() * player.getHp() ), 15f)
        fireballs.forEach {
//            batch.draw(fireballImage, it.x, it.y, it.width, it.height)
            batch.draw(fb, it.x, it.y, it.width/2, it.height/2, it.width, it.height, 1f, 1f, it.angle.toFloat())
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
            batch.draw(sfb, it.x, it.y,  it.width/2, it.height/2, it.width, it.height, 1f, 1f, angledeg)
        }


        batch.end()
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
            if(fireball.y +32 < 0 || fireball.y > worldHeight || fireball.x +32 < 0 || fireball.x > worldWidth) fireballIt.remove()
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
    private fun spawnSuperFireBall(){
        superfireballs.add(SuperFireBall(MathUtils.random(0f, worldWidth - 32f), worldHeight, 50f, 32f, player.x, player.y, 3.5f))
        lastSfbTime = TimeUtils.nanoTime()
        beware.play()
    }
    private fun spawnFireball(){
        var rd: Fireball? = null

        when(Random().nextInt(4)){
            0 -> {
                rd = Fireball(MathUtils.random(0f, worldWidth - 32f), worldHeight, 50f, 32f, player.x, player.y, 3.5f)
            }
            1 -> {
                rd = Fireball(MathUtils.random(0f, worldWidth - 32f), 0f, 50f, 32f, player.x, player.y, 3.5f)
            }
            2 -> {
                rd = Fireball(worldWidth, MathUtils.random(0f, worldHeight - 32f), 50f, 32f, player.x, player.y, 3.5f)
            }
            3 -> {
                rd = Fireball(0f, MathUtils.random(0f, worldHeight - 32f), 50f, 32f, player.x, player.y, 3.5f)
            }
        }
//        val rd = Fireball(MathUtils.random(0f, worldWidth-64f) , worldHeight, 50f, 32f, player.x, player.y, 3.5f)
        if (rd != null) {
            fireballs.add(rd)
        }

        lastFbTime = TimeUtils.nanoTime()
    }

    override fun dispose() {
        batch.dispose()
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