package com.example.scheduleappdb.LogicsZone

class Constants {
    companion object {
        val serverIP = "78.107.147.202"
        val serverPort = 9876
        val daysOfWeek = setOf(
            "понедельник",
            "вторник",
            "среда",
            "четверг",
            "пятница",
            "суббота",
            "воскресенье"
        )
        val typesOfWeek = setOf(
            "четная",
            "нечетная"
        )
    }

    enum class Action(val toInt : Int) {
        Adding(1),
        Editing(2)
    }
    enum class ConnectionStage(val toInt : Int) {
        OutOfConnection(-1),
        WaitingForConnection(0),
        SuccessfulConnection(1)
    }
}