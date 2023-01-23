package com.example.scheduleappdb.LogicsZone

class Constants {
    companion object {
        val serverIP = "192.168.1.71"
        val serverPort = 9876
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