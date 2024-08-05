package com.example.snakegame

import java.util.LinkedList

data class SnakeSegment(
    val x: Int,
    val y: Int,
    var previous: Direction?,
    var next: Direction?

) {}

class SnakeBody(x: Int, y: Int) {
    val snakeBody: LinkedList<SnakeSegment> = LinkedList()

    init {
        snakeBody.addFirst(
            SnakeSegment(
                x, y, null, null
            )
        )
    }

    fun addHead(direction: Direction) {
        this.snakeBody.first.next = direction
        this.snakeBody.addFirst(
            SnakeSegment(
                this.snakeBody.first.x + direction.dx,
                this.snakeBody.first.y + direction.dy,
                Direction.invert(direction),
                null
            )
        )
    }

    fun addTail(direction: Direction) {
        this.snakeBody.last.previous = direction
        this.snakeBody.addLast(
            SnakeSegment(
                this.snakeBody.first.x + direction.dx,
                this.snakeBody.first.y + direction.dy,
                null,
                Direction.invert(direction)
            )
        )
    }

    fun dropTail() {
        this.snakeBody.removeLast()
        this.snakeBody.last.previous = null
    }

    fun inBody(x: Int, y: Int): Boolean {
        for (segment in this.snakeBody) {
            if (x == segment.x && y == segment.y) return true
        }
        return false
    }

    fun headX(): Int {
        return this.snakeBody.first.x
    }

    fun headY(): Int {
        return this.snakeBody.first.y
    }


}