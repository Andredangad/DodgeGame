package com.andre.dodgegame.model

import com.badlogic.gdx.math.Rectangle

class Player(px : Float, py :  Float, pWidth :  Float, pHeight :  Float, pHp:Int, private val speed:Int) : Rectangle() {
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