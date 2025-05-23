package controller;

import model.Model;
import view.View;

/**
 * Represents the main game loop responsible for controlling the update and rendering
 * processes of a game. The class maintains a fixed update rate to ensure consistent game behavior
 * regardless of hardware performance. It also handles smooth rendering while optimizing CPU usage.
 *
 * @implNote Implements the Runnable interface to allow the loop to run in a dedicated thread.
 */
class GameLoop implements Runnable {
    // Flag to control game loop execution
    private boolean running = false;
    // Target frames per second
    private final int targetFPS = 120;
    // Time per frame in nanoseconds
    private final long targetTime = 1000000000 / targetFPS; // nano seconds
    // Thread for running the game loop
    private Thread gameThread;

    /**
     * Starts the game loop if it's not already running.
     * Creates and starts a new thread for the game loop.
     */
    public void start() {
        if (running) return;
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Stops the game loop and waits for the game thread to finish.
     */
    public void stop() {
        if (!running) return;
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main game loop implementation.
     * Uses an accumulator to maintain a consistent update rate.
     * Handles updates and rendering while managing CPU usage.
     */
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        long accumulator = 0;

        while (running) {
            // Calculate elapsed time since last iteration
            long currentTime = System.nanoTime();
            long elapsedTime = currentTime - lastTime;
            lastTime = currentTime;
            accumulator += elapsedTime;

            // Update game state at fixed time steps
            while (accumulator >= targetTime) {
                updateModel();
                accumulator -= targetTime;
            }

            // Render the current game state
            renderView();

            // Sleep to not max out CPU
            try {
                long sleepTime = (targetTime - (System.nanoTime() - currentTime)) / 1000000;
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates the game model/state.
     * This method is called at a fixed time step.
     */
    private void updateModel() {
        Model.getInstance().getGame().updateState();
    }

    /**
     * Renders the current game state.
     * This method is called as often as possible while maintaining the target FPS.
     */
    private void renderView() {
        View.getInstance().notifyView();
    }

}