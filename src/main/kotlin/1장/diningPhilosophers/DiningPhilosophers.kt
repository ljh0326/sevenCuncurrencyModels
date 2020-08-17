package `1장`.diningPhilosophers

import java.util.*

data class Chopstick(val id: Int) {}

internal class DiningPhilosophers(private val left: Chopstick, private val right: Chopstick) : Thread() {
    private val random: Random = Random()

    // END:philosopher
    private var thinkCount = 0
    override fun run() {
        while (true) {
// END:philosopher
            ++thinkCount
            if (thinkCount % 10 == 0) println("Philosopher $this has thought $thinkCount times")
            // START:philosopher
            sleep(random.nextInt(1000).toLong()) // Think for a while
            synchronized(left) {                    // Grab left chopstick // <label id="code.syncleft"/>
                synchronized(right) {                 // Grab right chopstick // <label id="code.syncright"/>
                    sleep(random.nextInt(1000).toLong()) // Eat for a while
                }
            }
        }
    }
}
// END:philosopher

/**
 *     p1,   p2,   p3,   p4,   p5
 *    0,1   1,2   2,3   3,4   4,0
 *    각각의 철학자들이 0, 1, 2, 3, 4를 동시에들면 자원을 사용할수 없어서 데드락 걸린다.
 */
fun main(args: Array<String>) {
    val philosophers = arrayOfNulls<DiningPhilosophers>(5)
    val chopsticks = arrayOfNulls<Chopstick>(5)
    for (i in 0..4) chopsticks[i] = Chopstick(i)
    for (i in 0..4) {
        philosophers[i] = DiningPhilosophers(chopsticks[i]!!, chopsticks[(i + 1) % 5]!!)
        philosophers[i]!!.start()
    }
    for (i in 0..4) philosophers[i]!!.join()
}