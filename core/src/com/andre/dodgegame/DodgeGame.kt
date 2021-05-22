package com.andre.dodgegame

import com.andre.dodgegame.screens.MainMenuScreen
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch


class DodgeGame(private val androidInterface: AndroidInterface) : Game() {
    lateinit var batch: SpriteBatch
    lateinit var camera: OrthographicCamera
    var worldWidth = 1200f
    var worldHeight = 688f
    lateinit var font:BitmapFont

    override fun create() {

        batch = SpriteBatch()
        camera = OrthographicCamera(worldWidth, worldHeight)
        font = BitmapFont(Gdx.files.internal("font/score.fnt"))

        this.setScreen(MainMenuScreen(this, androidInterface))

    }


    override fun render() {
        batch.projectionMatrix = (camera.combined)
        super.render()
    }

    override fun resize(width: Int, height: Int) {
        camera.setToOrtho(false, width.toFloat(), height.toFloat())
        super.resize(width, height)
    }

    override fun dispose() {
        batch.dispose()

    }

}