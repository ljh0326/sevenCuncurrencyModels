package `1장`.unInterruptible

/**
 * 내재된 잠금장치는 블로킹된 쓰레드를 원상복귀시킬 방법이 없다.
 */
fun main(args: Array<String>) {
    val o1 = Any()
    val o2 = Any()
    val t1: Thread = object : Thread() {
        override fun run() {
            try {
                synchronized(o1) {
                    sleep(1000)
                    synchronized(o2) {}
                }
            } catch (e: InterruptedException) {
                println("t1 interrupted")
            }
        }
    }
    val t2: Thread = object : Thread() {
        override fun run() {
            try {
                synchronized(o2) {
                    sleep(1000)
                    synchronized(o1) {}
                }
            } catch (e: InterruptedException) {
                println("t2 interrupted")
            }
        }
    }
    t1.start()
    t2.start()
    Thread.sleep(2000)
    t1.interrupt()
    t2.interrupt()
    t1.join()
    t2.join()
}