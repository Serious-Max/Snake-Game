package com.example.snakegame

enum class Direction(val dx: Int, val dy: Int) {
    Left(-1, 0),
    Right(1, 0),
    Up(0, -1),
    Down(0, 1);

    companion object {
        fun invert(direction: Direction): Direction{
            return when(direction){
                Left -> Right
                Right -> Left
                Up -> Down
                Down -> Up
            }
        }
    }
}