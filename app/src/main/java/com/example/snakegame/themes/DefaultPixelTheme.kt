package com.example.snakegame.themes

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.snakegame.DrawInfo
import com.example.snakegame.SnakeBody
import com.example.snakegame.Theme

class DefaultPixelTheme : Theme {
    private val snakePaint = Paint().apply { color = Color.GREEN }
    private val applePaint = Paint().apply { color = Color.RED }
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
                canvas.drawRect(
                    centerX - drawInfo.cellSize / 8,
                    centerY - drawInfo.cellSize / 8,
                    centerX + drawInfo.cellSize / 8,
                    centerY + drawInfo.cellSize / 8,
                    this.dotPaint
                )
            }
        }
    }

    override fun drawApple(canvas: Canvas, drawInfo: DrawInfo, appleX: Int, appleY: Int) {
        val appleRectX = appleX * drawInfo.cellSize + drawInfo.offsetX
        val appleRectY = appleY * drawInfo.cellSize + drawInfo.offsetY
        canvas.drawRect(
            appleRectX + drawInfo.cellSize / 8,
            appleRectY + drawInfo.cellSize / 8,
            appleRectX + drawInfo.cellSize - drawInfo.cellSize / 8,
            appleRectY + drawInfo.cellSize - drawInfo.cellSize / 8,
            this.applePaint
        )
    }

    override fun drawSnake(canvas: Canvas, drawInfo: DrawInfo, snake: SnakeBody) {
        for (segment in snake.snakeBody) {
            val segmentX = segment.x * drawInfo.cellSize + drawInfo.offsetX
            val segmentY = segment.y * drawInfo.cellSize + drawInfo.offsetY
            canvas.drawRect(
                segmentX + drawInfo.cellSize / 8,
                segmentY + drawInfo.cellSize / 8,
                segmentX + drawInfo.cellSize - drawInfo.cellSize / 8,
                segmentY + drawInfo.cellSize - drawInfo.cellSize / 8,
                this.snakePaint
            )
        }
    }
}