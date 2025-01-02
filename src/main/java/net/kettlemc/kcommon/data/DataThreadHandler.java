package net.kettlemc.kcommon.data;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A thread handler for data operations containing a queue.
 */
public class DataThreadHandler {

    private static final Logger LOGGER = Logger.getLogger(DataThreadHandler.class.getName());

    private BlockingQueue<Runnable> taskQueue;
    private ExecutorService executorService;
    private AtomicBoolean running;

    /**
     * Initializes the DataThreadHandler by creating a new task queue and executor service.
     */
    public void init() {
        if (taskQueue != null || executorService != null) {
            throw new IllegalStateException("DataThreadHandler already initialized!");
        }

        taskQueue = new LinkedBlockingQueue<>();
        executorService = Executors.newSingleThreadExecutor();
        running = new AtomicBoolean(true);

        executorService.execute(() -> {
            while (running.get()) {
                try {
                    Runnable task = taskQueue.poll(1, TimeUnit.SECONDS);
                    if (task != null) {
                        task.run();
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error processing task: " + e.getMessage(), e);
                }
            }
        });
    }

    /**
     * Queues a runnable to be executed by the executor service.
     *
     * @param runnable the runnable to queue
     */
    public void queue(Runnable runnable) {
        if (taskQueue == null || executorService == null || running == null || !running.get()) {
            throw new IllegalStateException("DataThreadHandler not initialized or already shut down!");
        }
        taskQueue.add(runnable);
    }

    /**
     * Shuts down the executor service and ensures task queue is processed gracefully before termination.
     */
    public void shutdown() {
        if (taskQueue == null || executorService == null || running == null || !running.get()) {
            throw new IllegalStateException("DataThreadHandler not initialized or already shut down!");
        }

        System.out.println(taskQueue.size() + " tasks remaining in queue. Attempting shutdown...");
        running.set(false);

        // Drain any remaining tasks in the queue before shutting down
        taskQueue.forEach(runnable -> {
            try {
                runnable.run();
            } catch (Exception e) {
                System.err.println("Error executing task during shutdown: " + e.getMessage());
                e.printStackTrace();
            }
        });
        taskQueue.clear();

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                System.err.println("Executor did not terminate in the allocated time. Forcing shutdown...");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.err.println("Shutdown interrupted. Forcing shutdown...");
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
