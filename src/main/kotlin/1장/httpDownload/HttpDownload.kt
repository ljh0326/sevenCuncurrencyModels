package `1장`.httpDownload

import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import java.util.*

internal interface ProgressListener {
    fun onProgress(current: Int)
}

// START:downloader
internal class Downloader(url: URL, outputFilename: String?) : Thread() {
    private val `in`: InputStream = url.openConnection().getInputStream()
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

    @Synchronized
    private fun updateProgress(n: Int) {
        for (listener in listeners)  // START_HIGHLIGHT
            listener.onProgress(n)
        // END_HIGHLIGHT
    }

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
        out = FileOutputStream(outputFilename)
        listeners = ArrayList<ProgressListener>()
    }
}

fun main(args: Array<String>) {
    val from =
        URL("http://download.wikimedia.org/enwiki/latest/enwiki-latest-pages-articles.xml.bz2")
    val downloader = Downloader(from, "download.out")
    downloader.start()
    downloader.addListener(object : ProgressListener {
        override fun onProgress(n: Int) {
            print("\r" + n)
            System.out.flush()
        }
        fun onComplete(success: Boolean) {}
    })
    downloader.join()
}