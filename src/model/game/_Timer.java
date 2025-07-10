package model.game;

import controller.game.GameLoop;
import view.View;

import javax.swing.Timer;

class _Timer {
    static int elapsedTime = 0;

    private final static GameLoop gl = GameLoop.getInstance(); // shortcut

    /** <p>Timer needed to count the elapsed time in the game.</p>
     * <b>BEHAVIOR</b>:
     * <ul>
     *     <li>MODEL -> when game ends, the elapsed time is needed to calculate the score</li>
     *     <li>VIEW -> update the view with the elapsed time each second</li>
     * </ul>
     */
    static final Timer timer  = new Timer(1000, e -> {
        // Increment the elapsed time in seconds
        elapsedTime++;
        // Update the timer label in the view with the elapsed seconds
        View.getInstance().getGamePanel().setElapsedTime(elapsedTime);
    });

    public static void start() {
        assert gl.isRunning() : "GameLoop should be running when starting the timer";
        assert elapsedTime == 0 : "Elapsed time should be 0 when starting the timer";
        timer.start();
    }
    public static void stopTimer() {
        elapsedTime = 0; // Reset elapsed seconds when stopping the timer
        if (timer.isRunning()) {
            timer.stop();
        }
    }
    /** Set pause if the Game is running, otherwise resume*/
    public static void togglePause() {
        if (elapsedTime == 0) return; // Do not toggle pause if the game has just started

        if (isRunning()){
            timer.stop();
        }
        else {
            timer.start();
        }
    }

    public static boolean isRunning() {
        return timer.isRunning();
    }


}