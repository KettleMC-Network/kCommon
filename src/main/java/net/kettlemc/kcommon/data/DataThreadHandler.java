package net.kettlemc.kcommon.data;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A thread handler for data operations containing a queue.
 */
public class DataThreadHandler {

    private BlockingQueue<Runnable> taskQueue;
    private ExecutorService executorService;

    private Thread taskProcessingThread;

    /**
     * Initializes the DataThreadHandler by creating a new task queue and executor service.
     */
    public void init() {
        if (taskQueue != null || executorService != null) {
            throw new IllegalStateException("DataThreadHandler already initialized!");
        }
        this.taskQueue = new LinkedBlockingQueue<>();
        this.executorService = Executors.newSingleThreadExecutor();

        this.taskProcessingThread = new Thread(() -> {
            while (true) {
                try {
                    Runnable task = taskQueue.take();
                    executorService.execute(task);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        this.taskProcessingThread.start();

    }

    /**
     * Queues a runnable to be executed by the executor service.
     *
     * @param runnable the runnable to queue
     */
    public void queue(Runnable runnable) {
        if (this.taskQueue == null || this.executorService == null) {
            throw new IllegalStateException("DataThreadHandler not initialized!");
        }
        this.taskQueue.add(runnable);
    }

    /**
     * Shuts down the executor service and waits for the task processing thread to finish.
     */
    public void shutdown() {
        if (this.taskQueue == null && this.executorService == null) {
            throw new IllegalStateException("DataThreadHandler not initialized!");
        }
        try {
            this.taskProcessingThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        this.executorService.shutdown();
    }


}
