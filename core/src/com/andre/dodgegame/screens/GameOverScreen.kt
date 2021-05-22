package com.andre.dodgegame.screens

import com.andre.dodgegame.DodgeGame
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.utils.Align

class GameOverScreen(private val game: DodgeGame, private val time:Int): Screen {
    private var exitButton: Texture = Texture(Gdx.files.internal("image/exit_button_inactive.png"))
    private val EXIT_BUTTON_WIDTH = 300f
    private val EXIT_BUTTON_HEIGHT = 120f
    private val m = (time)/60
    private val s= (time - m*60)
    private val t = "${if (m <= 10) "0${m}" else "$m"} : ${if (s <= 10) "0${s}" else "$s"}"
    private var win: GlyphLayout = GlyphLayout(game.font, "You won !!", Color.WHITE, 0f, Align.left, false)
    private var lost: GlyphLayout = GlyphLayout(game.font, "You lost", Color.WHITE, 0f, Align.left, false)
    private var timer: GlyphLayout = GlyphLayout(game.font, "you survive : $t", Color.WHITE, 0f, Align.left, false)

    init {
        Gdx.input.inputProcessor = object : InputAdapter() {
            override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {

                val x = game.camera.viewportWidth/2 - EXIT_BUTTON_WIDTH/2
                val y = game.camera.viewportHeight - screenY
                if(screenX > x && screenX < x + EXIT_BUTTON_WIDTH && y < game.camera.viewportHeight/2 + EXIT_BUTTON_HEIGHT/2 && y > game.camera.viewportHeight/2 - EXIT_BUTTON_HEIGHT/2  ){
                    dispose()
                    Gdx.app.exit()
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
        Gdx.gl.glClearColor(0f,0f,0f,1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        game.batch.begin()
        game.batch.draw(exitButton, game.camera.viewportWidth/2 - EXIT_BUTTON_WIDTH/2, game.camera.viewportHeight/2 - EXIT_BUTTON_HEIGHT/2, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT)
        if(time < 60){
            game.font.draw(game.batch, lost, game.camera.viewportWidth/2 - lost.width / 2, (game.camera.viewportHeight/2 + EXIT_BUTTON_HEIGHT))
        }
        else{
            game.font.draw(game.batch, win, game.camera.viewportWidth/2 - win.width / 2, (game.camera.viewportHeight/2 + EXIT_BUTTON_HEIGHT))
        }
        game.font.draw(game.batch, timer, game.camera.viewportWidth/2 - timer.width / 2, (game.camera.viewportHeight/2 - EXIT_BUTTON_HEIGHT/2))
        game.batch.end()
    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun resize(width: Int, height: Int) {
        game.camera.setToOrtho(false, width.toFloat(), height.toFloat())
    }

    override fun dispose() {
        Gdx.input.inputProcessor = null
        exitButton.dispose()
    }

}