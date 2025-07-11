package view.impl._common.panels;

import javax.swing.*;
import java.awt.*;

public class TimerPanel extends JPanel {
    private final JLabel label =  new JLabel("Time: ");

    public TimerPanel(){
        super();
        Font timerFont = new Font("Arial", Font.BOLD, 20);
        label.setFont(timerFont);
        label.setForeground(Color.BLACK);
        add(label);

        setOpaque(false);
    }

    public void setTime(long cents){
        // Format in "ss:cc" format
        String formattedTime = String.format("%02d:%02d", (cents / 100) % 60, cents % 100);
        label.setText("Time: " + formattedTime);
    }

}
