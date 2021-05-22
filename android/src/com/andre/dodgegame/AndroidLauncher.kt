package com.andre.dodgegame

import android.os.Bundle
import android.util.Log
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration

class AndroidLauncher : AndroidApplication(), AndroidInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()
        config.useAccelerometer = false
        config.useCompass = false

        initialize(DodgeGame(this), config)

//        val lauchIntent = packageManager.getLaunchIntentForPackage("com.example.todogame")
//        lauchIntent?.let { startActivity(it) }

    }

    override fun onGameFinish(win:Boolean) {
        val lauchIntent = packageManager.getLaunchIntentForPackage("com.example.todogame")?.apply {
            putExtra("minigameState", win)
        }
        lauchIntent?.let { startActivity(it) }
    }
}