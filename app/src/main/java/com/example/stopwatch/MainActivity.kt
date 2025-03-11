package com.example.stopwatch

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val buttonCircle: Button = findViewById(R.id.button_circle)
        val buttonStartContinue: Button = findViewById(R.id.button_start_continue)
        val textHours: TextView = findViewById(R.id.text_hours)
        val textMin: TextView = findViewById(R.id.text_min)
        val textSec:TextView = findViewById(R.id.text_sec)

        val viewModel: StopwatchViewModel = ViewModelProvider(this)[StopwatchViewModel::class.java]

        viewModel.button1StateStartStop.observe(this){new ->
            buttonStartContinue.text = new
        }

        viewModel.button2StateResetCircle.observe(this){new ->
            buttonCircle.text = new
        }

        viewModel.timer.observe(this){(hours, min,sec)->
            textHours.text = "%02d".format(hours)
            textMin.text = "%02d".format(min)
            textSec.text = "%02d".format(sec)
        }

        buttonStartContinue.setOnClickListener{
            viewModel.toggleButton1()
        }

        buttonCircle.setOnClickListener{
            viewModel.toggleButton2()
        }

    }
}