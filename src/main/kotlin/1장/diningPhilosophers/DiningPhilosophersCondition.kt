package `1장`.diningPhilosophers

import java.util.*

// START:philosopher
internal class DiningPhilosophersCondition(left: Chopstick, right: Chopstick) : Thread() {
    // START_HIGHLIGHT
    private var first: Chopstick? = null
    private var second: Chopstick? = null

    // END_HIGHLIGHT
    private val random: Random

    // END:philosopher
    private var thinkCount = 0
    override fun run() {
        while (true) {
            // END:philosopher
            ++thinkCount
            if (thinkCount % 10 == 0) println("Philosopher $this has thought $thinkCount times")
            // START:philosopher
            sleep(random.nextInt(1000).toLong()) // Think for a while
            // START_HIGHLIGHT
            synchronized(first!!) {                   // Grab first chopstick
                synchronized(second!!) {                // Grab second chopstick
                    // END_HIGHLIGHT
                    sleep(random.nextInt(1000).toLong()) // Eat for a while
                }
            }
        }
    }

    // START:philosopher
    init {
        // START_HIGHLIGHT
        if (left.id < right.id) {
            first = left
            second = right
        } else {
            first = right
            second = left
        }
        // END_HIGHLIGHT
        random = Random()
    }
}

/**
 *     p1,   p2,   p3,   p4,   p5 -> p1이 0사용중이면 0에서부터 대기하고 있음
 *    0,1   1,2   2,3   3,4   0,4
 */
fun main(args: Array<String>) {
    val philosophers = arrayOfNulls<DiningPhilosophersCondition>(5)
    val chopsticks = arrayOfNulls<Chopstick>(5)
    for (i in 0..4) chopsticks[i] = Chopstick(i)
    for (i in 0..4) {
        philosophers[i] = DiningPhilosophersCondition(chopsticks[i]!!, chopsticks[(i + 1) % 5]!!)
        philosophers[i]!!.start()
    }
    for (i in 0..4) philosophers[i]!!.join()
}