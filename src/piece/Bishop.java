package piece;

import main.CheckHandler;
import main.GamePanel;

public class Bishop  extends Piece{
    CheckHandler checkHandler = new CheckHandler();
    public Bishop(int color, int col, int row) {
        super(color, col, row);

        if (color == GamePanel.WHITE) {
            bufferedImage = getImage("/piece/w-bishop");
        }
        else {
            bufferedImage = getImage("/piece/b-bishop");
        }
    }
    @Override
    public boolean canMove(int targetCol, int targetRow) {

        if(isWithinBoard(targetCol, targetRow)) {

            if((Math.abs(targetCol - prevCol)) == (Math.abs(targetRow - prevRow))) {
                if(isValidSquare(targetCol, targetRow) && !isPieceInPath(targetCol, targetRow)) {
                    return true;
                }
            }
        }
        return false;
        }
    @Override
    public boolean isPieceInPath(int targetCol, int targetRow) {
        if(targetRow > prevRow && targetCol > prevCol) {
            for(int i = 0; i < Math.abs(targetRow - prevRow); i++) {
                for(Piece piece : GamePanel.simPieces) {
                    if(piece.row == prevRow + i && piece.col == prevCol + i && piece != this) {
                        return true;
                    }
                }
            }
        } else if(targetRow > prevRow && targetCol < prevCol) {
            for(int i = 0; i < Math.abs(targetRow - prevRow); i++) {
                for(Piece piece : GamePanel.simPieces) {
                    if(piece.row == prevRow + i && piece.col == prevCol - i && piece != this) {
                        return true;
                    }
                }
            }
        } else if(targetRow < prevRow && targetCol < prevCol) {
            for(int i = 0; i < Math.abs(targetRow - prevRow); i++) {
                for(Piece piece : GamePanel.simPieces) {
                    if(piece.row == prevRow - i && piece.col == prevCol - i && piece != this) {
                        return true;
                    }
                }
            }
        } else if(targetRow < prevRow && targetCol > prevCol) {
            for(int i = 0; i < Math.abs(targetRow - prevRow); i++) {
                for(Piece piece : GamePanel.simPieces) {
                    if(piece.row == prevRow - i && piece.col == prevCol + i && piece != this) {
                        return true;
                    }
                }
            }
        }
        return false;
        }

    }

