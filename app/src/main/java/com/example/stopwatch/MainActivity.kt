package com.example.stopwatch

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.graphics.toColorInt


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

        val recycleView: RecyclerView = findViewById(R.id.recycle_list)

        val viewModel: StopwatchViewModel = ViewModelProvider(this)[StopwatchViewModel::class.java]

        viewModel.button1StateStartStop.observe(this){new ->
            buttonStartContinue.text = new
        }

        viewModel.button2StateResetCircle.observe(this){new ->
            buttonCircle.text = new
        }

        viewModel.itemData.observe(this){new ->
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
            viewModel.toggleButton1()
        }

        buttonCircle.setOnClickListener{
            viewModel.toggleButton2()
        }

    }
}