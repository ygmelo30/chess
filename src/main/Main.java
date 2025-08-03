package main;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) throws LineUnavailableException, UnsupportedAudioFileException, IOException, URISyntaxException {

        JFrame window = new JFrame("My Chess");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);



        // Adding the game panel to the window
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);
        gamePanel.launchGame();


    }

}