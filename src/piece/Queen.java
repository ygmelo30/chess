package piece;

import main.GamePanel;

import java.io.Serial;

public class Queen  extends Piece{
    public Queen(int color, int col, int row) {
        super(color, col, row);

        if (color == GamePanel.WHITE) {
            bufferedImage = getImage("/piece/w-queen");
        }
        else {
            bufferedImage = getImage("/piece/b-queen");
        }
    }
    @Override
    public boolean canMove(int targetCol, int targetRow) {

        if(isWithinBoard(targetCol, targetRow)) {

            if((prevCol == targetCol || prevRow == targetRow)) {
                if(isValidSquare(targetCol, targetRow)) {
                    return true;
                }
            } else if ((Math.abs(targetCol - prevCol)) == (Math.abs(targetRow - prevRow))) {
                if(isValidSquare(targetCol, targetRow)) {
                    return true;
                }
            }
        }
        return false;
    }
}