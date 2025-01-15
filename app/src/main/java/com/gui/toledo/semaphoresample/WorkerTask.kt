package com.gui.toledo.semaphoresample

import android.os.Handler
import java.util.concurrent.Semaphore

class WorkerTask(
    private val taskId: Int,
    private val semaphore: Semaphore,
    private val handler: Handler
) : Runnable {
    override fun run() {
        try {
            semaphore.acquire()

            val message = handler.obtainMessage()
            message.obj = "Task $taskId is working on ${Thread.currentThread().name}"
            handler.sendMessage(
                message
            )

            Thread.sleep(2000)

            semaphore.release()

            val message2 = handler.obtainMessage()
            message2.obj = "Task $taskId is completed  on ${Thread.currentThread().name}"
            handler.sendMessage(
                message2
            )
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}