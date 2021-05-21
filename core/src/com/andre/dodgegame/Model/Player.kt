package com.andre.dodgegame.Model

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad

class Player(px : Float, py :  Float, pWidth :  Float, pHeight :  Float,private val pHp:Int,private val speed:Int) : Rectangle() {
    private var hp:Int = pHp
    private var maxHp:Int = pHp
    init {
        x = px
        y = py
        width = pWidth
        height = pHeight
    }
    fun isDead():Boolean{
        hp -=1
        return hp <=0
    }
    fun getHp(): Int{
        return hp
    }
    fun maxHp(): Int{
        return maxHp
    }
    fun getSpeed(): Int{
        return speed
    }

}