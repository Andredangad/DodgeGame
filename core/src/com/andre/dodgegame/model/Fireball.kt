package com.andre.dodgegame.model

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import kotlin.math.sqrt

class Fireball(rx: Float, ry: Float, rw: Float, rh: Float, playerx: Float, playery: Float, speed: Float) : Rectangle(){
    var velX : Double = 0.0
    var velY: Double = 0.0
    var angle: Int = 0
    private var dx : Double = 0.0
    private var dy: Double = 0.0
    init {
        x = rx
        y = ry
        width = rw
        height = rh
        dx = (playerx - x).toDouble()
        dy = (playery - y).toDouble()
        val length = sqrt((dx * dx + dy * dy))
        dx /= length
        dy /= length
        velX = dx * speed
        velY = dy * speed
        angle = Vector2(velX.toFloat(), velY.toFloat()).angleDeg().toInt()
    }

    fun move(){
        x += (velX).toFloat()
        y += (velY).toFloat()
    }

}