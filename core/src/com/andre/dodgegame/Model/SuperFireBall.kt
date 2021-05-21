package com.andre.dodgegame.Model

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import kotlin.math.sqrt

class SuperFireBall(rx: Float, ry: Float, rw: Float, rh: Float, private val playerx: Float, private val playery: Float, private val speed: Float): Rectangle() {
    var velX : Double = 0.0
    var velY: Double = 0.0
    private var dx : Double = 0.0
    private var dy: Double = 0.0
    var angle: Float = 0.0f
    init {
        x = rx
        y = ry
        width = rw
        height = rh
//        dx = (playerx - x).toDouble()
//        dy = (playery - y).toDouble()
//        val length = sqrt((dx * dx + dy * dy))
//        dx /= length
//        dy /= length
//        velX = dx * speed
//        velY = dy * speed
//        angle = Vector2(velX.toFloat(), velY.toFloat()).angleDeg().toInt()
    }

    fun move(playerX:Float, playerY:Float){
//        let x = space.ally.x - space.aliens[chooseAlien].x;
//        let y = space.ally.y - space.aliens[chooseAlien].y;
//        let angle = Math.atan2(y, x);
//        space.aliens[chooseAlien].xVel =  Math.cos(angle);
//        space.aliens[chooseAlien].yVel =  Math.sin(angle);
        dx = (playerx - x).toDouble()
        dy = (playery - y).toDouble()
        val length = sqrt((dx * dx + dy * dy))
        dx /= length
        dy /= length
        velX = dx * speed
        velY = dy * speed
        val angle = Vector2(velX.toFloat(), velY.toFloat())
        x += (velX).toFloat()
        y += (velY).toFloat()
    }
}