package com.example.snakegame

import android.graphics.Canvas
import com.example.snakegame.themes.DefaultPixelTheme
import com.example.snakegame.themes.DefaultTheme
import com.example.snakegame.themes.EmojiTheme

enum class ThemeType(val themeId: String){
    Default("theme_default"),
    DefaultPixel("theme_default_pixel"),
    Cat("theme_cat"),
}

object ThemeFactory {
    fun getTheme(themeType: ThemeType): Theme {
        return when(themeType){
            ThemeType.Default -> DefaultTheme()
            ThemeType.DefaultPixel -> DefaultPixelTheme()
            ThemeType.Cat -> EmojiTheme("\uD83D\uDC31", "\uD83E\uDD5B")
            else -> DefaultTheme()
        }
    }
}

interface Theme {
    fun drawBackground(canvas: Canvas, drawInfo: DrawInfo, gameWidth: Int, gameHeight: Int)
    fun drawApple(canvas: Canvas, drawInfo: DrawInfo, appleX: Int, appleY: Int)
    fun drawSnake(canvas: Canvas, drawInfo: DrawInfo, snake: SnakeBody)
}



