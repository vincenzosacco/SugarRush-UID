package controller;

import model.Model;
import model.game.MapParser;
import utils.audio.GameAudioController;
import view.View;

import javax.swing.*;

/**
 * Represents the main game loop responsible for controlling the update and rendering
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

    // Flag to control game loop execution
    private boolean running = false;
    // Target frames per second
    private final int targetFPS = 120;
    // Time per frame in nanoseconds
    private final long targetTime = 1000000000 / targetFPS; // nano seconds
    // Thread for running the game loop
    private Thread gameThread;

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

    private Timer gameTimer;
    // Variable to keep count of total milliseconds elapsed
    private long totalElapsedMillis = 0;
    // Time when the timer was started or resumed
    private long startTimeMillis;

    private int oldElapsedSeconds = 0; // Used for shutdown logic


    /**
     * Starts the game loop if it's not already running.
     * Creates and starts a new thread for the game loop.
     */
    public void start() {
        // Check if the thread is null OR if it is no longer "alive" (has finished its execution)
        //This allows a new thread to be created only when the previous one is dead or does not exist
        if (gameThread == null || !gameThread.isAlive()) {
            running = true; // Set the execution state to true
            gameThread = new Thread(this);
            gameThread.start();
            Model.getInstance().getGame().start(); // Start the game model (if not already started)
            GameAudioController.getInstance().playGameMusic();
            startGameTimer(); // Start the game timer to track elapsed time
        } else if (running) {
            // Here the game loop is already active with a live thread, do nothing.
        } else {
            // This case means that gameThread exists and is alive, but 'running' is false.
            // This could indicate an inconsistency, but to be safe, reset the state to running=true.
            running = true;
            startGameTimer();
        }
        // Record the current moment as start or resume time
        startTimeMillis = System.currentTimeMillis();
    }


    /**
     * Pauses the game loop.
     */
    public void pause() {
        if (!running) {
            return; // If the game loop is not running, do nothing
        }

        pauseGameTimer(); // Pause the game timer if it was running
        running = false; // Set the execution status to false to terminate the loop in run()
        // Calculates the time elapsed since the last start/resume and adds it to the total
        totalElapsedMillis += (System.currentTimeMillis() - startTimeMillis);

        // Interrupt the thread to wake it from any sleep or block
        if (gameThread != null) {
            gameThread.interrupt();
        }
    }

    /**
     * Stops the game loop and waits for the game thread to finish.
     */
    public void shutdown() {
        // Compute elapsed time before resetting
        oldElapsedSeconds = (int) ((totalElapsedMillis + (System.currentTimeMillis() - startTimeMillis)) / 1000);

        // Reset timer values
        resetGameTimer();
        // update profile

        if (!running) {
            return;
        }

        running = false; // Set the execution status to false to terminate the loop in run()

        // Interrupt the thread to wake it from any sleep or block
        if (gameThread != null) {
            gameThread.interrupt();
        }

        try {
            if (gameThread != null) {
                gameThread.join(2000); // Wait for thread termination for up to 2 seconds
                if (gameThread.isAlive()) {
                    System.err.println("GameLoop thread did not terminate gracefully after join.");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupt state
        } finally {
            gameThread = null; // IMPORTANT: Reset the thread reference after it finishes
        }
    }
    // Check if the time taken to complete the level allows you to get a star
    public void controlTime(){
        // Compute elapsed time
        oldElapsedSeconds = (int) ((totalElapsedMillis + (System.currentTimeMillis() - startTimeMillis)) / 1000);

        // Get the time limit from MapParser
        int timeLimit = MapParser.getTimeLimit();

        // if time is less than the specified time limit, add a star if it's possible
        if (getElapsedSeconds() < timeLimit && timeLimit > 0) { // Ensure timeLimit is positive
            Model.getInstance().getGame().setCoinsCollected(1); // Pos=1 --> Time Trial Star
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
                // Ensure we don't over-accumulate if updates are very slow
                if (accumulator < 0) {
                    accumulator = 0;
                }
            }

            // Render the current game state
            renderView();

            // Sleep to not max out CPU
            try {
                long sleepTime = (targetTime - (System.nanoTime() - currentTime)) / 1_000_000; // Convert to milliseconds
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            } catch (InterruptedException e) {
                // Restore the interrupted status
                Thread.currentThread().interrupt();
                // If interrupted, break out of the loop or handle gracefully
                running = false;
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
        // Calculate total seconds by combining total milliseconds with current milliseconds
        long currentTotalSeconds = (totalElapsedMillis + (System.currentTimeMillis() - startTimeMillis)) / 1000;
        View.getInstance().getGamePanel().setElapsedSeconds(currentTotalSeconds);
        View.getInstance().notifyView();
    }

    /**
     * Checks if the game loop is currently running.
     * @return true if the game loop is running, false otherwise.
     */
    public boolean isRunning() {
        return running;
    }

    private void startGameTimer() {
        if (gameTimer == null) {
            // The timer will update the view every 100ms for smoother display of seconds
            gameTimer = new Timer(100, e -> {
                // `total ElapsedMillis` is only updated when the game is paused/resumed
                // Here we just update the view with the time calculated in renderView()
                renderView();
            });
        }
        if (!gameTimer.isRunning()) {
            gameTimer.start();
        }
    }

    public void pauseGameTimer() {
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }
    }
    // Reset level time
    public void resetGameTimer() {
        this.pauseGameTimer();
        totalElapsedMillis = 0;
        startTimeMillis = System.currentTimeMillis();
        View.getInstance().getGamePanel().setElapsedSeconds(0);
    }


    public int getElapsedSeconds() {
        // Returns the entire elapsed seconds, which were saved at shutdown time
        return oldElapsedSeconds;
    }

}
