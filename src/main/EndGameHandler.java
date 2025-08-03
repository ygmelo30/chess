package main;

import javax.swing.*;

public class EndGameHandler {
    private int playAgain;
    public EndGameHandler(String type) {
        if(type.equalsIgnoreCase("stalemate")) {
            playAgain = JOptionPane.showConfirmDialog(null, "Game ended in a draw due to a stalemate." +
                    " Would you like to play again?");
        } else if(type.equalsIgnoreCase("50")) {
            playAgain = JOptionPane.showConfirmDialog(null, "Game ended in a draw due to a stalemate." +
                    " Would you like to play again?");
        } else if(type.equalsIgnoreCase("white")) {
            playAgain = JOptionPane.showConfirmDialog(null, "White has won the game" +
                    " Would you like to play again?");
        } else if(type.equalsIgnoreCase("black")) {
            playAgain = JOptionPane.showConfirmDialog(null, "Black has won the game" +
                    " Would you like to play again?");
        }
    }

    public int handleResponse () {
        if (playAgain == 0) {
            return 0;
        }
        return 1;
    }
}
