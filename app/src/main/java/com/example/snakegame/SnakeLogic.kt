package com.example.snakegame

import android.content.SharedPreferences
import android.os.CountDownTimer

interface GameCallback {
    val sharedPreferences: SharedPreferences
    fun onScoreIncrease()
    fun onGameOver()
}

class SnakeLogic(private val callback: GameCallback) {
    val FIELD_WIDTH: Int = 10
    val FIELD_HEIGHT: Int = 15

    private val START_TIMEOUT: Long = 300L
    private val START_SEGMENTS: Int = 3

    private lateinit var timer: CountDownTimer
    private lateinit var direction: Direction
    private var isPaused: Boolean = true

    var appleX: Int = 0
    var appleY: Int = 0
    var score: Int = 0

    lateinit var snakeBody: SnakeBody

    fun newGame() {
        this.score = 0
        this.initTimer()
        this.initBody()
        this.generateApple()
        this.timer.start()
    }

    fun resumeGame() {
        this.isPaused = false
    }

    fun pauseGame() {
        this.isPaused = true
    }

    fun setDirection(direction: Direction) {
        // защита от изменения положения на противоположное
        if (this.direction == Direction.invert(direction)) return
        this.direction = direction
    }

    fun makeMove() {
        if (this.isPaused) return

        val newHeadX = this.snakeBody.headX() + this.direction.dx
        val newHeadY = this.snakeBody.headY() + this.direction.dy
        if (newHeadX < 0 || newHeadX >= this.FIELD_WIDTH) {
            this.gameOver()
            return
        }
        if (newHeadY < 0 || newHeadY >= this.FIELD_HEIGHT) {
            this.gameOver()
            return
        }
        if (this.snakeBody.inBody(newHeadX, newHeadY)) {
            this.gameOver()
            return
        }
        this.snakeBody.addHead(this.direction)
        if (newHeadX == this.appleX && newHeadY == this.appleY) {
            this.increaseScore()
            this.generateApple()
        } else {
            this.snakeBody.dropTail()
        }
    }

    private fun increaseScore() {
        score += 1
        val previousBest = callback.sharedPreferences.getInt("best_score", 0)
        if (score > previousBest) {
            with(callback.sharedPreferences.edit()) {
                putInt("best_score", score)
                commit()
            }
        }
        callback.onScoreIncrease()
    }

    private fun initTimer() {
        this.timer = object : CountDownTimer(Long.MAX_VALUE, this.START_TIMEOUT) {
            override fun onTick(p0: Long) {
                makeMove()
            }

            override fun onFinish() {
            }
        }
    }

    private fun initBody() {
        // змея расположена вертикально, голова в центре, начало движения вниз
        this.direction = Direction.Down
        val startX = this.FIELD_WIDTH / 2
        val startY = this.FIELD_HEIGHT / 2
        this.snakeBody = SnakeBody(startX, startY)
        for (i in 0..<(this.START_SEGMENTS - 1)) {
            this.snakeBody.addTail(Direction.Up)
        }
    }

    private fun gameOver() {
        this.pauseGame()
        callback.onGameOver()
    }

    private fun generateApple() {
        var newAppleX: Int
        var newAppleY: Int
        do {
            newAppleX = (0..<this.FIELD_WIDTH).random()
            newAppleY = (0..<this.FIELD_HEIGHT).random()
        } while (this.snakeBody.inBody(newAppleX, newAppleY))
        this.appleX = newAppleX
        this.appleY = newAppleY
    }

}