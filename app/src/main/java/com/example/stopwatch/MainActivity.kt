package com.example.stopwatch

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {

  var adapter: ItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val buttonCircle: Button = findViewById(R.id.button_circle)
        val buttonStartContinue: Button = findViewById(R.id.button_start_continue)
        val textHours: TextView = findViewById(R.id.text_hours)
        val textMin: TextView = findViewById(R.id.text_min)
        val textSec:TextView = findViewById(R.id.text_sec)


        val mediaPlayerStart = MediaPlayer.create(this, R.raw.btn1)
        val mediaPlayerLap = MediaPlayer.create(this, R.raw.btn2)
        val mediaPlayerReset = MediaPlayer.create(this, R.raw.btn3)
        val mediaPlayerStop = MediaPlayer.create(this, R.raw.btn4)


        val recycleView: RecyclerView = findViewById(R.id.recycle_list)

        val viewModel: StopwatchViewModel = ViewModelProvider(this)[StopwatchViewModel::class.java]


        viewModel.button1StateStartStop.observe(this){new ->
            buttonStartContinue.text = new
        }

        viewModel.button2StateResetCircle.observe(this){new ->
            buttonCircle.text = new
        }

        viewModel.itemData.observe(this){new ->
            Log.d("Abc23", new.toString())
            adapter?.updateData(new)
        }

        viewModel.timer.observe(this){(hours, min,sec)->
            textHours.text = "%02d".format(hours)
            textMin.text = "%02d".format(min)
            textSec.text = "%02d".format(sec)
        }

        viewModel.isVisible.observe(this) {isVisible ->
            if(!isVisible) {
                buttonStartContinue.visibility = View.VISIBLE
                buttonCircle.visibility = View.GONE
            }else{
                buttonStartContinue.visibility = View.VISIBLE
                buttonCircle.visibility = View.VISIBLE
            }
        }

        viewModel.isGreenOr.observe(this) { isGreenOr ->
            if (isGreenOr) {
                buttonStartContinue.setTextColor(ContextCompat.getColor(this, R.color.button_text_green))
                buttonStartContinue.backgroundTintList = ContextCompat.getColorStateList(this, R.color.button_bg_green)
            } else {
                buttonStartContinue.setTextColor(ContextCompat.getColor(this, R.color.button_text_red))
                buttonStartContinue.backgroundTintList = ContextCompat.getColorStateList(this, R.color.button_bg_red)
            }
        }


        adapter = ItemAdapter(emptyList())


        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.adapter = adapter

        buttonStartContinue.setOnClickListener{
            viewModel.toggleButton1(mediaPlayerStart, mediaPlayerStop)
        }

        buttonCircle.setOnClickListener{
            viewModel.toggleButton2(mediaPlayerLap, mediaPlayerReset)
        }

    }

}