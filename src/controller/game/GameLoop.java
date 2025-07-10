package controller.game;

import model.game.Game;
import view.View;

/**
 * Represents the mainPanels game loop responsible for controlling the update and rendering
 * processes of a game. This class is implemented as a Singleton to ensure only one
 * instance of the game loop exists throughout the application, preventing multiple
 * game loops from operating on the same game model.
 * It maintains a fixed update rate to ensure consistent game behavior
 * regardless of hardware performance. It also handles smooth rendering while optimizing CPU usage.
 *
 * @implNote Implements the Runnable interface to allow the loop to run in a dedicated thread.
 */
public class GameLoop implements Runnable {
    // Singleton instance of GameLoop
    private static GameLoop instance;

    // Target frames per second
    private final int targetFPS = 120;
    // Time per frame in nanoseconds
    private final long targetTime = 1000000000 / targetFPS; // nano seconds
    // Thread for running the game loop
    private Thread gameThread;
    /** flag to correctly terminate the thread*/
    private boolean terminate = true; // default true, so isRunning() returns false at the start


    // SINGLETON //
    private GameLoop() {
        // Private constructor for Singleton pattern
    }

    public static synchronized GameLoop getInstance() {
        if (instance == null) {
            instance = new GameLoop();
        }
        return instance;
    }

    /**
     * Starts the game loop if it's not already running.
     * Creates and starts a new thread for the game loop.
     */
    synchronized void start() {
        assert !isRunning() : "GameLoop should be terminated when starting";
        gameThread = new Thread(this);
        gameThread.setDaemon(true);
        gameThread.start();
        terminate = false; // Reset terminate state
    }



    /**
     * Stops the game loop and waits for the game thread to finish.
     */
    synchronized void shutdown() {
        assert gameThread != null;
        assert gameThread.isAlive();

//        // Interrupt the thread to wake it from any sleep or block
//        gameThread.interrupt();

        // Say thread to stop
        terminate = true;

        // Wait for the thread to finish execution
        try {
            gameThread.join(2000); // Wait for thread termination for up to 2 seconds
            if (gameThread.isAlive()) {
                System.err.println("GameLoop thread did not terminate gracefully after join.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupt state
        } finally {
            gameThread = null; // IMPORTANT: Reset the thread reference after it finishes
        }
    }


    /**
     * Main game loop implementation.
     * Uses an accumulator to maintain a consistent update rate.
     * Handles updates and rendering while managing CPU usage.
     */
    @Override
    public void run() { //must be public
        long lastTime = System.nanoTime();
        long accumulator = 0;

        while (gameThread.isAlive() && !terminate) {
            // PAUSE STATE //
            if (!Game.getInstance().isRunning()) {
                continue;
            }

            // Calculate elapsed time since last iteration
            long currentTime = System.nanoTime();
            long elapsedTime = currentTime - lastTime;
            lastTime = currentTime;
            accumulator += elapsedTime;

            // Update game state at fixed time steps
            while (accumulator >= targetTime) {
                updateModel();
                accumulator -= targetTime;
                // Ensure we don't over-accumulate if updates are very slow
                if (accumulator < 0) {
                    accumulator = 0;
                }
            }

            // Render the current game state
            notifyView();

            // Sleep to not max out CPU
            try {
                long sleepTime = (targetTime - (System.nanoTime() - currentTime)) / 1_000_000; // Convert to milliseconds
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            } catch (InterruptedException e) {
                // Restore the interrupted status
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Updates the game model/state.
     * This method is called at a fixed time step.
     */
    private void updateModel() {
        Game.getInstance().updateState();
    }

    /**
     * Renders the current game state.
     * This method is called as often as possible while maintaining the target FPS.
     */
    private void notifyView() {

        View.getInstance().refresh();
    }

    /**
     * Checks if the game loop is currently running.
     * @return true if the game loop is running, false otherwise.
     */
    public synchronized boolean isRunning() {
        return gameThread !=null && gameThread.isAlive() && !terminate ;
    }
}