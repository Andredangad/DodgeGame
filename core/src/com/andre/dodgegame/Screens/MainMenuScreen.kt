package com.andre.dodgegame.Screens

import com.andre.dodgegame.DodgeGame
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture

class MainMenuScreen(private val game: DodgeGame) : Screen {

    private var playButtonInactive:Texture = Texture(Gdx.files.internal("image/play_button_inactive.png"))
    private val PLAY_BUTTON_WIDTH = 300f
    private val PLAY_BUTTON_HEIGHT = 120f

    init {
        Gdx.input.inputProcessor = object : InputAdapter() {
            override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {

                val x = game.camera.viewportWidth/2 - PLAY_BUTTON_WIDTH/2
                val y = game.camera.viewportHeight - screenY
                if(screenX > x && screenX < x + PLAY_BUTTON_WIDTH && y < game.camera.viewportHeight/2 + PLAY_BUTTON_HEIGHT/2 && y > game.camera.viewportHeight/2 - PLAY_BUTTON_HEIGHT/2  ){
                    Gdx.app.log("dsq", screenX.toString())
                    dispose()
                    game.screen = MainGameScreen(game)
                }
                return super.touchUp(screenX, screenY, pointer, button)
            }
        }
    }
    override fun hide() {

    }

    override fun show() {

    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1f,0f,0f,1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        game.batch.begin()
        game.batch.draw(playButtonInactive, game.camera.viewportWidth/2 - PLAY_BUTTON_WIDTH/2, game.camera.viewportHeight/2 - PLAY_BUTTON_HEIGHT/2, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT)
        game.batch.end()
    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun resize(width: Int, height: Int) {

    }

    override fun dispose() {
        Gdx.input.inputProcessor = null
    }
}