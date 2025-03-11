package com.example.stopwatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.log

class StopwatchViewModel: ViewModel() {

    private var isRun = true
    private var job: Job? = null

    private val _button1StateStartStop = MutableLiveData("Старт")
    private val _button2StateResetCircle = MutableLiveData("Сброс")
    private val _timer = MutableLiveData(Triple(0, 0, 0))


    val button1StateStartStop: LiveData<String> = _button1StateStartStop
    val button2StateResetCircle: LiveData<String> = _button2StateResetCircle
    val timer: LiveData<Triple<Int,Int,Int>> = _timer


    fun toggleButton1() {
        if (_button1StateStartStop.value == "Старт") {
            start()
            _button1StateStartStop.value = "Стоп"
            _button2StateResetCircle.value = "Круг"
        } else {
            _button1StateStartStop.value = "Старт"
            pause()
            _button2StateResetCircle.value = "Сброс"
        }
    }
        fun toggleButton2() {
            if (_button2StateResetCircle.value == "Сброс") {
                reset()
                _button2StateResetCircle.value = "Круг"
            }else
                circle()
        }


    fun start(){
        isRun = true
        job = viewModelScope.launch(Dispatchers.IO) {
            while(isRun){
                delay(1000)
                _timer.postValue(_timer.value?.let { (hours, min, sec) ->
                    val newSec = (sec+1) % 60
                    val newMin = if(newSec == 0) (min + 1) % 60 else min
                    val newHour = if(newMin == 60) (hours + 1) % 60 else hours

                    Triple(newHour, newMin, newSec)
                })
            }
        }
    }

    fun pause(){
      isRun = false
      job?.cancel()
    }


    fun circle(){
        val circle = timer
    }

    fun reset(){
        pause()
        _timer.value =Triple(0,0,0)
    }

    }

