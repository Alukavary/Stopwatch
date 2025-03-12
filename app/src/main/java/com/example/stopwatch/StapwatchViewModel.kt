package com.example.stopwatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StopwatchViewModel : ViewModel() {

    private var isRun = true
    private var job: Job? = null

    private val _button1StateStartStop = MutableLiveData("Start")
    private val _button2StateResetCircle = MutableLiveData("Reset")

    private val _timer = MutableLiveData(Triple(0, 0, 0))
    val timer: LiveData<Triple<Int, Int, Int>> = _timer

    private val _itemData = MutableLiveData<MutableList<ItemClass>>(mutableListOf())
    val itemData: LiveData<List<ItemClass>> = _itemData as LiveData<List<ItemClass>>

    private var lapCount = 0

    private var _isVisible = MutableLiveData(false)
    var isVisible: LiveData<Boolean> = _isVisible

    private var _isGreenOr = MutableLiveData(true)
    var isGreenOr: LiveData<Boolean> = _isGreenOr


    val button1StateStartStop: LiveData<String> = _button1StateStartStop
    val button2StateResetCircle: LiveData<String> = _button2StateResetCircle


    fun toggleButton1() {
        if (_button1StateStartStop.value == "Start") {
            start()
            _isVisible.value = true
            _isGreenOr.value = false
            _button1StateStartStop.value = "Stop"
            _button2StateResetCircle.value = "Lap"
        } else {
            _button1StateStartStop.value = "Start"
            _isGreenOr.value = true
            pause()
            _button2StateResetCircle.value = "Reset"
        }
    }

    fun toggleButton2() {
        if (_button2StateResetCircle.value == "Reset") {
            reset()
            _button2StateResetCircle.value = "Lap"
            _isVisible.value = false
        } else
            _timer.value?.let{circle(it)}
    }


    fun start() {
        isRun = true
        job = viewModelScope.launch(Dispatchers.IO) {
            while (isRun) {
                delay(1000)
                _timer.postValue(_timer.value?.let { (hours, min, sec) ->
                    val newSec = (sec + 1) % 60
                    val newMin = if (newSec == 0) (min + 1) % 60 else min
                    val newHour = if (newMin == 60 && newSec == 0) hours + 1 else hours
                    Triple(newHour, newMin, newSec)
                })
            }
        }
    }

    fun pause() {
        isRun = false
        job?.cancel()
    }


    fun circle(time: Triple<Int, Int, Int>) {
        val timeSt = "%02d:%02d:%02d".format(time.first, time.second, time.third)
        lapCount++

        val newCircle = ItemClass("Lap $lapCount",timeSt)
        _itemData.value = (_itemData.value ?: mutableListOf()).apply { add(newCircle) }

    }

    fun reset() {
        pause()
        _timer.value = Triple(0, 0, 0)
        lapCount = 0
        _itemData.value = mutableListOf()

    }

}

