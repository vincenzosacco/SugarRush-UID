package View;

import javax.swing.*;

public class MainFrame {
    public static void launch(){
        int rowCount=21;
        int columnCount=19;
        int tileSize=32;
        int boardWidth= columnCount * tileSize;
        int boardHeight= rowCount * tileSize;

        JFrame frame= new JFrame("Sugar Rush");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SugarPanel panel= new SugarPanel();
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        JOptionPane.showMessageDialog(frame,"Try to reach the sugar piece",null,JOptionPane.INFORMATION_MESSAGE);
    }
}
