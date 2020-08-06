package `1장`

class HelloWorld {

}

fun main() {
    val myThread = Thread() {
        run { println("Hello from new thread") }
    }

    myThread.start()
//    Thread.yield()
    Thread.sleep(1)
    println("Hello from main thread")
    myThread.join()
}