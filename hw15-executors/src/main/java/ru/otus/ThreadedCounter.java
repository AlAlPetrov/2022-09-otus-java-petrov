package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadedCounter {
    private static final Logger logger = LoggerFactory.getLogger(ThreadedCounter.class);
    private String last = "second";

    private synchronized void action(String thread) {
        Integer counter = 1;
        int addon = 1;
        while(!Thread.currentThread().isInterrupted()) {
            try {
                while (last.equals(thread)) {
                    this.wait();
                }
                logger.info(counter.toString());

                counter = counter + addon;
                if (counter == 10)
                {
                    addon = -1;
                }
                if (counter == 1)
                {
                    addon = 1;
                }

                last = thread;
                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        ThreadedCounter threadedCounter = new ThreadedCounter();
        new Thread(() -> threadedCounter.action("first")).start();
        new Thread(() -> threadedCounter.action("second")).start();
    }

    private static void sleep() {
        try {
            Thread.sleep(3_00);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
