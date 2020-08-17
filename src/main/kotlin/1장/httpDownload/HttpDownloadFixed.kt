package com.paulbutcher

import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import java.util.*

internal interface ProgressListener {
    fun onProgress(current: Int)
}

internal class FixedDownloader(url: URL, outputFilename: String?) : Thread() {
    private val `in`: InputStream
    private val out: OutputStream
    private val listeners: ArrayList<ProgressListener>

    @Synchronized
    fun addListener(listener: ProgressListener) {
        listeners.add(listener)
    }

    @Synchronized
    fun removeListener(listener: ProgressListener) {
        listeners.remove(listener)
    }

    // START:updateProgress
    private fun updateProgress(n: Int) {
        var listenersCopy: ArrayList<ProgressListener>

        /**
         * 동기화가 메서드 전체를 감싸는게 아니라 클론할떄만 감싸기 때문에
         * 외부메서드를 호출하지않고, 잠금장치를 손에 쥐고있는 시간도 줄여주기때문에 일석이조의 효과를 줄 수 있다.
         */
        synchronized(this) {
            // START_HIGHLIGHT
            listenersCopy = listeners.clone() as ArrayList<ProgressListener>
        }
        for (listener in listenersCopy) listener.onProgress(n)
    }

    // END:updateProgress
    override fun run() {
        var n = 0
        var total = 0
        val buffer = ByteArray(1024)
        try {
            while (`in`.read(buffer).also { n = it } != -1) {
                out.write(buffer, 0, n)
                total += n
                updateProgress(total)
            }
            out.flush()
        } catch (e: IOException) {
        }
    }

    init {
        `in` = url.openConnection().getInputStream()
        out = FileOutputStream(outputFilename)
        listeners = ArrayList<ProgressListener>()
    }
}