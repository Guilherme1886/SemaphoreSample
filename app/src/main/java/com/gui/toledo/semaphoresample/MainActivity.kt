package com.gui.toledo.semaphoresample

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import androidx.activity.ComponentActivity
import java.util.concurrent.Semaphore

class MainActivity : ComponentActivity() {

    private var startButton: Button? = null
    private var ln: LinearLayout? = null
    private lateinit var semaphore: Semaphore
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ln = findViewById(R.id.ln)
        startButton = findViewById(R.id.startButton)

        semaphore = Semaphore(3)

        handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                updateStatus(msg.obj.toString())
            }
        }

        startButton?.setOnClickListener { _ -> startTasks() }
    }

    private fun updateStatus(status: String) {
        val newText = status + getString(R.string.semaphore_binded, Thread.currentThread().name)
        val text = TextView(this).apply {
            text = SpannableString(newText).apply {
                val start = "Task".length
                val end = start + 2
                setSpan(ForegroundColorSpan(Color.MAGENTA), start, end, 0)

                if (status.contains("working")) {
                    val start2 = status.indexOf("working")
                    val end2 = status.length
                    setSpan(ForegroundColorSpan(Color.MAGENTA), start, end, 0)
                    setSpan(ForegroundColorSpan(Color.MAGENTA), start2, end2, 0)
                } else {
                    val start2 = status.indexOf("completed")
                    val end2 = status.length
                    setSpan(ForegroundColorSpan(Color.GREEN), start, end, 0)
                    setSpan(ForegroundColorSpan(Color.GREEN), start2, end2, 0)
                }

                val start3 = newText.indexOf("binded")
                val end3 = newText.length
                setSpan(ForegroundColorSpan(Color.CYAN), start3, end3, 0)
            }
            gravity = Gravity.CENTER_HORIZONTAL
            layoutParams =
                LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                    setMargins(0, 32, 0, 0)
                }
        }
        ln?.addView(text)
    }

    private fun startTasks() {
        for (i in 0..9) {
            Thread(
                WorkerTask(
                    taskId = i,
                    semaphore = semaphore,
                    handler = handler
                )
            ).start()
        }
    }
}