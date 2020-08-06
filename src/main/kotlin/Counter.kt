data class Counter(var  count: Int){

    @Synchronized
    fun increment() {
        ++ count
    }
}

fun main() {
    val counter = Counter(0)
    val countingThread1 = Thread() {
        for(i in 1..10000)
            counter.increment()
    }
    val countingThread2 = Thread() {
        for(i in 1..10000)
            counter.increment()
    }

    countingThread1.start()
    countingThread2.start()
    countingThread1.join()
    countingThread2.join()

    println(counter.count)
}