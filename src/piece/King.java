package piece;

import main.GamePanel;

public class King  extends Piece{
    public King(int color, int col, int row) {
        super(color, col, row);

        if (color == GamePanel.WHITE) {
            bufferedImage = getImage("/piece/w-king");
        }
        else {
            bufferedImage = getImage("/piece/b-king");
        }
    }
    @Override
    public boolean canMove(int targetCol, int targetRow) {

        if(isWithinBoard(targetCol, targetRow)) {

            if(Math.abs(targetCol - prevCol) + Math.abs(targetRow - prevRow) == 1 ||
                    Math.abs(targetCol - prevCol) * Math.abs(targetRow - prevRow) == 1) {
                if(isValidSquare(targetCol, targetRow)) {
                    return true;
                }
            }
        }
        return false;
    }
}