import View.MainFrame;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RenderLoop implements Runnable {
    private final ScheduledExecutorService executor;
    private final static double UPDATE_RATE = 1.0/60.0; // 60 FPS
    private volatile boolean running = false;
    private ScheduledFuture<?> gameLoop;

    // SINGLETON
    private static RenderLoop instance = null;

    public static RenderLoop getInstance() {
        if (instance == null) {
            instance = new RenderLoop();
        }
        return instance;
    }

    
    private RenderLoop() {
        // Create a single-threaded executor for predictable scheduling
        executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setName("GameLoop-Thread");
            return thread;
        });
    }


    public void start() {
        if (!running) {
            running = true;
            // Schedule the game loop at fixed rate
            gameLoop = executor.scheduleAtFixedRate(
                this,
                0,                                              // initial delay
                (long)(UPDATE_RATE * 1000),                    // period in milliseconds
                TimeUnit.MILLISECONDS
            );
        }
    }
    
    public void stop() {
        running = false;
        if (gameLoop != null) {
            gameLoop.cancel(false);
        }
        // Shutdown executor gracefully
        executor.shutdown();
        try {
            if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    @Override
    public void run() {
        try {
            render();
        } catch (Exception e) {
            // Log the exception but don't let it terminate the game loop
            e.printStackTrace();
        }
    }

    /**
     * Renders the game graphics to the screen.
     * <p>
     * This method is part of the game's rendering pipeline and triggers the repainting of the main
     * application frame. It ensures that the game visuals are updated by calling the repaint method
     * on the singleton instance of {@code MainFrame}.
     * </p>
     * @see <a href="https://www.oracle.com/java/technologies/painting.html">Java painting documentation</a>.
     */
    private void render() {
        // Render game graphics //

        // Remember that update() :
        // - Schedules a paint request for the component
        // - Asynchronous - doesn't immediately paint -> (maybe can cause some issue, let's see...)
        // - Used for visual updates only
        // - Lighter weight than `revalidate()`
        MainFrame.getInstance().repaint();

    }
}