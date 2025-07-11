package model.game;

import controller.game.GameLoop;
import view.View;

import javax.swing.*;

class _Timer {
    static long elapsedTime = 0L;

    private final static GameLoop gl = GameLoop.getInstance(); // shortcut

    /** <p>Timer needed to count the elapsed time in the game.</p>
     * <b>BEHAVIOR</b>:
     * <ul>
     *     <li>MODEL -> when game ends, the elapsed time is needed to calculate the score</li>
     *     <li>VIEW -> update the view with the elapsed time each second</li>
     * </ul>
     */
    static final Timer timer  = new Timer(10, e -> {
        // Increment the elapsed time each centisecond
        elapsedTime++;
        // Update the timer label in the view with the elapsed
        View.getInstance().getGamePanel().setElapsedTime(elapsedTime); //time in seconds
    });

    public static void start() {
        assert gl.isRunning() : "GameLoop should be running when starting the timer";
        assert elapsedTime == 0L : "Elapsed time should be 0 when starting the timer";
        timer.start();
    }
    public static void stop() {
        elapsedTime = 0L; // Reset elapsed seconds when stopping the timer
        if (timer.isRunning()) {
            timer.stop();
        }
    }

    public static void pause() {
        if (elapsedTime == 0L) return; // Do not toggle pause if the game has just started
        if (isRunning()) {
            timer.stop();
        }
    }
    public static void resume() {
        if (elapsedTime == 0L) return; // Do not toggle pause if the game has just started

        if (!isRunning()) {
            timer.start();
        }
    }

    public static boolean isRunning() {
        return timer.isRunning();
    }


}