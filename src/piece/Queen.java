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
                if(isValidSquare(targetCol, targetRow) && !isPieceInPath(targetCol, targetRow)) {
                    return true;
                }
            } else if ((Math.abs(targetCol - prevCol)) == (Math.abs(targetRow - prevRow))) {
                if(isValidSquare(targetCol, targetRow) && !isPieceInPath(targetCol, targetRow)) {
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public boolean isPieceInPath(int targetCol, int targetRow) {
        if((prevCol == targetCol) || (prevRow == targetRow)) {
            if(prevRow > targetRow) {
                for (int i = prevRow; i > targetRow; i--) {
                    for(Piece piece : GamePanel.simPieces) {
                        if(piece.row == i && piece.col == targetCol && piece != this) {
                            return true;
                        }
                    }
                }
            } else if(targetRow > prevRow) {
                for (int i = prevRow; i < targetRow; i++) {
                    for(Piece piece : GamePanel.simPieces) {
                        if(piece.row == i && piece.col == targetCol && piece != this) {
                            return true;
                        }
                    }
                }
            } else if(targetCol > prevCol) {
                for (int i = prevCol; i < targetCol; i++) {
                    for(Piece piece : GamePanel.simPieces) {
                        if(piece.row == targetCol && piece.col == i && piece != this) {
                            return true;
                        }
                    }
                }
            } else if(prevCol > targetCol) {
                for (int i = prevCol; i > targetCol; i--) {
                    for(Piece piece : GamePanel.simPieces) {
                        if(piece.row == targetRow && piece.col == i && piece != this) {
                            return true;
                        }
                    }
                }
            }
        }
        else {
            if (targetRow > prevRow && targetCol > prevCol) {
                for (int i = 0; i < Math.abs(targetRow - prevRow); i++) {
                    for (Piece piece : GamePanel.simPieces) {
                        if (piece.row == prevRow + i && piece.col == prevCol + i && piece != this) {
                            return true;
                        }
                    }
                }
            } else if (targetRow > prevRow && targetCol < prevCol) {
                for (int i = 0; i < Math.abs(targetRow - prevRow); i++) {
                    for (Piece piece : GamePanel.simPieces) {
                        if (piece.row == prevRow + i && piece.col == prevCol - i && piece != this) {
                            return true;
                        }
                    }
                }
            } else if (targetRow < prevRow && targetCol < prevCol) {
                for (int i = 0; i < Math.abs(targetRow - prevRow); i++) {
                    for (Piece piece : GamePanel.simPieces) {
                        if (piece.row == prevRow - i && piece.col == prevCol - i && piece != this) {
                            return true;
                        }
                    }
                }
            } else if (targetRow < prevRow && targetCol > prevCol) {
                for (int i = 0; i < Math.abs(targetRow - prevRow); i++) {
                    for (Piece piece : GamePanel.simPieces) {
                        if (piece.row == prevRow - i && piece.col == prevCol + i && piece != this) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}