package piece;

import main.Board;
import main.CheckHandler;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Piece {

    public BufferedImage bufferedImage;
    public int x, y;
    public int col, row, prevCol, prevRow;
    public int color;
    public Piece hittingPiece;
    CheckHandler checkHandler = new CheckHandler();

    public Piece (int color, int col, int row) {

        this.color = color;
        this.col = col;
        this.row = row;
        x = getX(col);
        y = getY(row);
        prevCol = col;
        prevRow = row;
    }
    public BufferedImage getImage(String imagePath) {

        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    public int getX(int col) {
        return (col * Board.SQUARE_SIZE);
    }
    public int getY(int row) {
        return (row * Board.SQUARE_SIZE);
    }
    public int getCol(int x) {
        return (x + Board.HALF_SQUARE_SIZE)/Board.SQUARE_SIZE;
    }
    public int getRow(int y) {
        return (y + Board.HALF_SQUARE_SIZE)/Board.SQUARE_SIZE;
    }
    public int getIndex() {
        for(int i = 0; i < GamePanel.simPieces.size(); i++) {
            if(GamePanel.simPieces.get(i) == this) {
                return i;
            }
        }
        return 0;
    }
    public void updatePosition() {

            x = getX(col);
            y = getY(row);
            prevCol = getCol(x);
            prevRow = getRow(y);
    }
    public void resetPosition() {
        col = prevCol;
        row = prevRow;
        x = getX(col);
        y = getY(row);
    }
    public boolean canMove(int targetCol, int targetRow) {
        return false;
    }
    //checks if there is a piece in the path so that pieces cannot go through each other
    public boolean isPieceInPath(int targetCol, int targetRow) {
        return false;
    }
    public boolean isWithinBoard(int targetCol, int targetRow) {
        if((targetRow <= 7 && targetRow >= 0) && (targetCol <= 7 && targetCol >= 0)) {
            return true;
        }
        return false;
    }
    public Piece getOccupiedSquare(int targetCol, int targetRow) {
        for(Piece piece : GamePanel.simPieces) {
            if(piece.row == targetRow && piece.col == targetCol && piece != this) {
                return piece;
            }
        }
        return null;
    }
    public boolean isValidSquare(int targetCol, int targetRow) {

        hittingPiece = getOccupiedSquare(targetCol, targetRow);

        if(hittingPiece == null) { // the square is empty
            return true;
        }
        else { // the square is occupied
             if(hittingPiece.color != this.color) {
                 return true;
             }
             else {
                 hittingPiece = null;
             }
        }

        return false;
    }

public boolean canLegallyMoveTo(int kingCol, int kingRow, int targetCol, int targetRow)  {

    //checks if a piece can legally move and not cause checkmate. for example a pinned knight cannot move
    if(isWithinBoard(kingCol, kingRow)) {

        if(Thread.currentThread().getStackTrace()[2].getMethodName().equals("kingCanMove")) {
            Piece king = checkHandler.getKing(kingCol, kingRow);
            GamePanel.simPieces.remove(king.getIndex());
            if (this.color == 1) {

                if(checkHandler.inCheck(targetCol, targetRow, 1)) {
                    GamePanel.simPieces.add(king);
                    return true;
                } else {
                    GamePanel.simPieces.add(king);
                    return false;
                }

            }
            else {
                if(checkHandler.inCheck(targetCol, targetRow, 0)) {
                    GamePanel.simPieces.add(king);
                    return true;
                } else {
                    GamePanel.simPieces.add(king);
                    return false;
                }
            }
        }

        if (this instanceof King) {
            if (this.color == 1) {

                kingCol = targetCol;
                kingRow = targetRow;
                return checkHandler.inCheck(kingCol, kingRow, 1);

            } else {
                kingCol = targetCol;
                kingRow = targetRow;
                return checkHandler.inCheck(kingCol, kingRow, 0);
            }

        }

        for (Piece piece : GamePanel.pieces) {
            if (piece.row == targetRow && piece.col == targetCol && piece.color != this.color) {

                GamePanel.simPieces.remove(piece.getIndex());

                boolean inCheck = checkHandler.inCheck(kingCol, kingRow, this.color);

                GamePanel.simPieces.add(piece);

                return inCheck;
            }
        }

            return checkHandler.inCheck(kingCol, kingRow, this.color);

    }
    else {
        return false;
    }

}
    public String toString() {
        return "Piece is of type: " + this.getClass() + " with col and row being: " + this.col + " " + this.row;
    }


    public void draw(Graphics2D g2) {
        g2.drawImage(bufferedImage, x, y, Board.SQUARE_SIZE, Board.SQUARE_SIZE, null);
    }


}
