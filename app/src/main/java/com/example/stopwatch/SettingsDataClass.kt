package com.example.stopwatch

data class SettingsDataClass(
    val isRun: Boolean = true,
    val button1StateStartStop: String = "Start",
    val button2StateResetCircle: String = "Lap",
    val timerHours: Int = 0,
    val timerMin: Int = 0,
    val timerSec: Int = 0,
    val itemData: String = "",
    val lapCount: Int = 0,
    var isVisible: Boolean = false,
    var isGreenOr: Boolean = true,
)

