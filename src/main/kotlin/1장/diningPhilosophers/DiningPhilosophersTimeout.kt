package `1장`.diningPhilosophers

import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

/**
 * ReentrantLock을 사용하면 블로킹된 쓰레드를 일시정지 할 수 있다.
 * tryLock()을 사용하면 무한이 지속되는 데드락은 피할 수있지만 좋은방법은아니다.
 * 모든 스레드가 동시에 타임아웃을 발생시키면 데드락 상태에 빠지는것도 가능하다.
 * 데드락 상황 제체를 발생시키지 않는것이 가장 좋다.
 */
internal class PhilosopherTimeout(private val leftChopstick: ReentrantLock, private val rightChopstick: ReentrantLock) :
    Thread() {
    private val random: Random = Random()

    // END:philosopher
    private var thinkCount = 0
    override fun run() {
        try {
            while (true) {
                ++thinkCount
                if (thinkCount % 10 == 0) println("Philosopher $this has thought $thinkCount times")
                // START:philosopher
                sleep(random.nextInt(1000).toLong()) // Think for a while
                leftChopstick.lock()
                try {
                    // START_HIGHLIGHT
                    /**
                     * tryLock() 잠금장치(락)을 얻는데 실패하면 타임아웃을 발생시킨다.
                     */
                    if (rightChopstick.tryLock(1000, TimeUnit.MILLISECONDS)) {
                        // END_HIGHLIGHT
                        // Got the right chopstick
                        try {
                            sleep(random.nextInt(1000).toLong()) // Eat for a while
                        } finally {
                            rightChopstick.unlock()
                        }
                    } else {
                        // Didn't get the right chopstick - give up and go back to thinking
                        // END_HIGHLIGHT
                        // END:philosopher
                        println("Philosopher $this timed out")
                        // START:philosopher
                    }
                } finally {
                    leftChopstick.unlock()
                }
            }
        } catch (e: InterruptedException) {
        }
    }
}