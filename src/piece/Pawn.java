package piece;

import main.Board;
import main.GamePanel;


public class Pawn extends Piece{

    public Pawn(int color, int col, int row) {
        super(color, col, row);

        if (color == GamePanel.WHITE) {
            bufferedImage = getImage("/piece/w-pawn");
        }
        else {
            bufferedImage = getImage("/piece/b-pawn");
        }
    }
    @Override
    public boolean canMove(int targetCol, int targetRow) {
         if(this.color == GamePanel.WHITE) {
             return canMoveWhite(targetCol, targetRow) || whiteCanCapture(targetCol, targetRow);
         } else if(this.color == GamePanel.BLACK) {
             return canMoveBlack(targetCol, targetRow) || blackCanCapture(targetCol, targetRow);
         }
        return false;
    }

    public boolean canMoveWhite (int targetCol, int targetRow) {
        if(isWithinBoard(targetCol, targetRow)) {
            if(prevRow == 6) {
                if (((!isSquareOccupied(targetCol, prevRow-1) && prevCol == targetCol && Math.abs(prevRow - targetRow) == 2)) ||
                        ((prevCol == targetCol) && Math.abs(prevRow - targetRow) == 1 && (prevRow > targetRow))) {
                     if (isValidSquare(targetCol, targetRow) && !isSquareOccupied(targetCol, targetRow)) {
                         return true;
                    }
                }
            }
            else {
                if (((prevCol == targetCol) && Math.abs(prevRow - targetRow) == 1) && (prevRow > targetRow)) {
                    if (isValidSquare(targetCol, targetRow) && !isSquareOccupied(targetCol, targetRow)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean canMoveBlack (int targetCol, int targetRow) {
        if(isWithinBoard(targetCol, targetRow)) {
            if(prevRow == 1) {
                if ((!isSquareOccupied(targetCol, prevRow+1) && prevCol == targetCol) && Math.abs(prevRow - targetRow) == 2 ||
                        ((prevCol == targetCol) && Math.abs(prevRow - targetRow) == 1) && (prevRow < targetRow)) {
                    if (isValidSquare(targetCol, targetRow) && !isSquareOccupied(targetCol, targetRow)) {
                        return true;
                    }
                }
            }
            else {
                if (((prevCol == targetCol) && Math.abs(prevRow - targetRow) == 1) && (prevRow < targetRow)) {
                    if (isValidSquare(targetCol, targetRow) && !isSquareOccupied(targetCol, targetRow)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean whiteCanCapture(int targetCol, int targetRow) {
        if(isWithinBoard(targetCol, targetRow)) {
            if(isSquareOccupied(targetCol, targetRow)) {
                if ((Math.abs(prevCol - targetCol) == 1) && Math.abs(prevRow - targetRow) == 1) {
                    if (isValidSquare(targetCol, targetRow)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean blackCanCapture(int targetCol, int targetRow) {
        if(isWithinBoard(targetCol, targetRow)) {
            if(isSquareOccupied(targetCol, targetRow)) {
                if ((Math.abs(prevCol - targetCol) == 1) && Math.abs(prevRow - targetRow) == 1) {
                    if (isValidSquare(targetCol, targetRow)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean isSquareOccupied(int targetCol, int targetRow) {
        for(Piece piece : GamePanel.simPieces) {
            if(piece.row == targetRow && piece.col == targetCol && piece != this) {
                return true;
            }
        }
        return false;
    }
}
