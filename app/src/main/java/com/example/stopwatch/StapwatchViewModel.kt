package com.example.stopwatch

import android.app.Application
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StopwatchViewModel(application: Application) : AndroidViewModel(application) {


    private var isRun = true
    private var job: Job? = null

    private val _button1StateStartStop = MutableLiveData("Start")
    val button1StateStartStop: LiveData<String> = _button1StateStartStop

    private val _button2StateResetCircle = MutableLiveData("Reset")
    val button2StateResetCircle: LiveData<String> = _button2StateResetCircle


    private var _timer = MutableLiveData(Triple(0, 0, 0))
    val timer: LiveData<Triple<Int, Int, Int>> = _timer

    private val _itemData = MutableLiveData<MutableList<ItemDataClass>>(mutableListOf())
    val itemData: LiveData<List<ItemDataClass>> = _itemData as LiveData<List<ItemDataClass>>

    private var _lapCount = MutableLiveData(0)
    val lapCount: LiveData<Int> = _lapCount


    private var _isVisible = MutableLiveData(false)
    var isVisible: LiveData<Boolean> = _isVisible

    private var _isGreenOr = MutableLiveData(true)
    var isGreenOr: LiveData<Boolean> = _isGreenOr



    val dataStoreManager: DataStoreManager = DataStoreManager(application)

    init {
        viewModelScope.launch {
            dataStoreManager.getSettings().collect { data ->
                _isGreenOr.value = data.isGreenOr
                _button1StateStartStop.value = data.button1StateStartStop
                _button2StateResetCircle.value = data.button2StateResetCircle
                _isVisible.value = data.isVisible
                _timer.value = Triple(data.timerHours, data.timerMin, data.timerSec)
                isRun = true

            }
        }
    }


    fun toggleButton1( btnSound: MediaPlayer, btnSound2: MediaPlayer) {
        if (_button1StateStartStop.value == "Start") {
            btnSound.start()
            viewModelScope.launch {
                dataStoreManager.saveBtnState2("Lap")
                dataStoreManager.saveIsVisible(true)
                dataStoreManager.saveBtn1("Stop")
                dataStoreManager.saveColor(false)
            }
            start()

        } else {
            btnSound2.start()

            pause()
            viewModelScope.launch {
                dataStoreManager.saveBtnState2("Reset")
                dataStoreManager.saveIsVisible(true)
                dataStoreManager.saveBtn1("Start")
                dataStoreManager.saveColor(true)
                dataStoreManager.saveIsRun(false)
            }
        }
    }

    fun toggleButton2(btnSoundLap: MediaPlayer, btnSoundReset: MediaPlayer) {
        if (_button2StateResetCircle.value == "Reset") {
            btnSoundReset.start()
            viewModelScope.launch {
                dataStoreManager.saveBtn1("Start")
                dataStoreManager.saveIsVisible(false)
                dataStoreManager.saveColor(true)
            }
            reset()
        } else if(isRun == true) {
            btnSoundLap.start()
            setItemData(lapCount.value ?: 0, _timer.value)
        }
    }


    fun start() {
        isRun = true
        job = viewModelScope.launch(Dispatchers.IO) {
            while (isRun) {
                delay(1000)
                _timer.postValue(_timer.value?.let { (hours, min, sec) ->
                    val newSec = (sec + 1) % 60
                    val newMin = if (newSec == 0) (min + 1) % 60 else min
                    val newHour = if (newMin == 0 && newSec == 0) hours + 1 else hours

                    val newTimer = Triple(newHour, newMin, newSec)
                    setTimer(newTimer)
                    return@let newTimer
                })
            }
        }
    }

    fun pause() {
        setIsRun(false)
        job?.cancel()
    }

    fun reset() {
        pause()
        setLapCount(0)
        viewModelScope.launch {
            dataStoreManager.saveLapCount(0)
            dataStoreManager.saveTimer(0,0,0)
        }
        _itemData.value = mutableListOf()

    }


    fun setIsRun(value: Boolean) {
        isRun = value
        viewModelScope.launch { dataStoreManager.saveIsRun(value) }
    }

    fun setTimer(timer: Triple<Int, Int, Int>) {
        _timer.postValue(timer)
        viewModelScope.launch {
            dataStoreManager.saveTimer(timer.first, timer.second, timer.third)
        }
    }

    fun setLapCount(count: Int) {
        _lapCount.value = count
        viewModelScope.launch { dataStoreManager.saveLapCount(count) }
    }

    fun setItemData(lapCount: Int, time: Triple<Int, Int, Int>?) {
        val timeSt = "%02d:%02d:%02d".format(time?.first, time?.second, time?.third)
        _lapCount.value = lapCount+1
        setLapCount(lapCount+1)

        val newCircle = ItemDataClass("Lap ${lapCount+1}", timeSt)
        _itemData.value = (_itemData.value ?: mutableListOf()).apply { add(newCircle) }
        viewModelScope.launch {
            dataStoreManager.saveItemData(newCircle.toString())
        }
    }
}

