package com.andre.dodgegame.Screens

import com.andre.dodgegame.DodgeGame
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture

class MainMenuScreen(private val game: DodgeGame) : Screen {
    private var playButtonActive:Texture = Texture(Gdx.files.internal("image/play_button_active.jpg"))
    private var playButtonInactive:Texture = Texture(Gdx.files.internal("image/play_button_inactive.jpg"))
    private var exitButtonActive:Texture = Texture(Gdx.files.internal("image/exit_button_active.jpg"))
    private var exitButtonInactive:Texture = Texture(Gdx.files.internal("image/exit_button_inactive.jpg"))

    override fun hide() {

    }

    override fun show() {

    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1f,0f,0f,1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        game.batch.begin()
        game.batch.draw(exitButtonActive)
        game.batch.end()
    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun resize(width: Int, height: Int) {

    }

    override fun dispose() {

    }
}