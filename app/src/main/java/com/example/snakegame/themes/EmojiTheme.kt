package com.example.snakegame.themes

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.example.snakegame.DrawInfo
import com.example.snakegame.SnakeBody
import com.example.snakegame.Theme
import kotlin.math.max

class EmojiTheme(private val bodyEmoji: String, private val appleEmoji: String) : Theme {
    private val snakePaint = Paint().apply { textAlign = Paint.Align.CENTER }
    private val applePaint = Paint().apply { textAlign = Paint.Align.CENTER }
    private val dotPaint = Paint().apply { color = Color.GRAY }

    override fun drawBackground(
        canvas: Canvas,
        drawInfo: DrawInfo,
        gameWidth: Int,
        gameHeight: Int
    ) {
        for (x in 0..<gameWidth) {
            for (y in 0..<gameHeight) {
                val centerX = x * drawInfo.cellSize + drawInfo.offsetX + drawInfo.cellSize / 2
                val centerY = y * drawInfo.cellSize + drawInfo.offsetY + drawInfo.cellSize / 2
                canvas.drawCircle(centerX, centerY, drawInfo.cellSize / 8, this.dotPaint)
            }
        }
    }

    override fun drawApple(canvas: Canvas, drawInfo: DrawInfo, appleX: Int, appleY: Int) {
        val appleRectX = appleX * drawInfo.cellSize + drawInfo.offsetX
        val appleRectY = appleY * drawInfo.cellSize + drawInfo.offsetY
        val textBounds = Rect()
        this.applePaint.getTextBounds(this.appleEmoji, 0, this.appleEmoji.length, textBounds)
        this.applePaint.textSize = this.applePaint.textSize * drawInfo.cellSize / max(
            textBounds.width(),
            textBounds.height()
        ).toFloat()

        canvas.drawText(
            this.appleEmoji,
            appleRectX + (drawInfo.cellSize / 2),
            appleRectY + (drawInfo.cellSize / 2) - ((this.applePaint.descent() + this.applePaint.ascent()) / 2),
            this.applePaint
        )
    }

    override fun drawSnake(canvas: Canvas, drawInfo: DrawInfo, snake: SnakeBody) {
        val textBounds = Rect()
        this.snakePaint.getTextBounds(this.bodyEmoji, 0, this.bodyEmoji.length, textBounds)
        this.snakePaint.textSize = this.snakePaint.textSize * drawInfo.cellSize / max(
            textBounds.width(),
            textBounds.height()
        ).toFloat()

        for (segment in snake.snakeBody) {
            val segmentX = segment.x * drawInfo.cellSize + drawInfo.offsetX
            val segmentY = segment.y * drawInfo.cellSize + drawInfo.offsetY
            canvas.drawText(
                this.bodyEmoji,
                segmentX + (drawInfo.cellSize / 2),
                segmentY + (drawInfo.cellSize / 2) - ((this.snakePaint.descent() + this.snakePaint.ascent()) / 2),
                this.snakePaint
            )
        }
    }
}