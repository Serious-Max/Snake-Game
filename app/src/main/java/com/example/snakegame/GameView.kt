package com.example.snakegame

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import kotlin.math.min

data class DrawInfo(var cellSize: Float, var offsetX: Float, var offsetY: Float) {
}

class GameView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val drawTimer = object : CountDownTimer(Long.MAX_VALUE, 100L) {
        override fun onTick(p0: Long) {
            invalidate()
        }

        override fun onFinish() {
        }

    }

    private var drawInfo: DrawInfo = DrawInfo(0f, 0f, 0f)

    lateinit var game: SnakeLogic
    lateinit var score: TextView
    lateinit var theme: Theme

    init {
        this.drawTimer.start()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateSizeAndOffsets()
    }

    private fun updateSizeAndOffsets() {
        this.drawInfo.cellSize =
            min(this.width / this.game.FIELD_WIDTH, this.height / this.game.FIELD_HEIGHT).toFloat()
        this.drawInfo.offsetX = (this.width - this.drawInfo.cellSize * this.game.FIELD_WIDTH) / 2
        this.drawInfo.offsetY = (this.height - this.drawInfo.cellSize * this.game.FIELD_HEIGHT) / 2
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.score.text = this.game.score.toString()
        this.theme.drawBackground(
            canvas,
            this.drawInfo,
            this.game.FIELD_WIDTH,
            this.game.FIELD_HEIGHT
        )
        this.theme.drawApple(canvas, this.drawInfo, this.game.appleX, this.game.appleY)
        this.theme.drawSnake(canvas, this.drawInfo, this.game.snakeBody)
    }
}