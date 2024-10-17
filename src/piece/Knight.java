package piece;

import main.GamePanel;

public class Knight  extends Piece{
    public Knight(int color, int col, int row) {
        super(color, col, row);

        if (color == GamePanel.WHITE) {
            bufferedImage = getImage("/piece/w-knight");
        }
        else {
            bufferedImage = getImage("/piece/b-knight");
        }
    }
    @Override
    public boolean canMove(int targetCol, int targetRow) {

        if(isWithinBoard(targetCol, targetRow)) {

            if(Math.abs(targetCol - prevCol) * Math.abs(targetRow - prevRow) == 2) {
                if(isValidSquare(targetCol, targetRow)) {
                    return true;
                }
            }
        }
        return false;
    }
}


