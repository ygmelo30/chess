package piece;

import main.Castling;
import main.GamePanel;

public class King extends Piece {
    Castling castleHandler = new Castling();
    public boolean canCastle = true;

    public King(int color, int col, int row) {
        super(color, col, row);

        if (color == GamePanel.WHITE) {
            bufferedImage = getImage("/piece/w-king");
        } else {
            bufferedImage = getImage("/piece/b-king");
        }
    }

    @Override
    public boolean canMove(int targetCol, int targetRow) {

        if (isWithinBoard(targetCol, targetRow)) {

            if((targetCol == 6 && targetRow == 7) || (targetCol == 2 && targetRow == 0) ||
                    (targetCol == 6 && targetRow == 0) || (targetCol == 2 && targetRow == 7)) {
                if(canCastle) {
                    return checkForCastling(targetCol, targetRow);
                }
            }

            if (Math.abs(targetCol - prevCol) + Math.abs(targetRow - prevRow) == 1 ||
                    Math.abs(targetCol - prevCol) * Math.abs(targetRow - prevRow) == 1) {
                if (NoEnemyKingNear(targetCol, targetRow)) {
                    return isValidSquare(targetCol, targetRow);
                }
            }
        }
        return false;
    }

    private boolean NoEnemyKingNear(int targetCol, int targetRow) {
        // a king cannot be adjacent to another king, this checks that

        for(Piece piece : GamePanel.simPieces) {
            if (piece instanceof King && piece != this) {

                if(piece.col == targetCol && piece.row == targetRow - 1) {
                    return false;
                } else if (piece.col == targetCol + 1 && piece.row ==  targetRow - 1) {
                    return false;
                } else if (piece.col == targetCol - 1 && piece.row ==  targetRow - 1) {
                    return false;
                } else if (piece.col == targetCol - 1 && piece.row == targetRow) {
                    return false;
                } else if (piece.col == targetCol - 1 && piece.row ==  targetRow + 1) {
                    return false;
                } else if (piece.col == targetCol && piece.row == targetRow + 1) {
                    return false;
                } else if (piece.col == targetCol + 1 && piece.row ==  targetRow + 1) {
                    return false;
                } else if (piece.col == targetCol + 1 && piece.row ==  targetRow) {
                    return false;
                }

            }
        }
        return true;
    }

    private boolean checkForCastling(int targetCol, int targetRow) {
        if(isWithinBoard(targetCol, targetRow)) {
            if (targetCol == 6 && targetRow == 7) {
                if (canCastle && castleHandler.isRightRowEmpty(color)) {
                    if (!checkHandler.inCheck(5, row, color) && !checkHandler.inCheck(6, row, color)) {
                        return true;
                    }
                } else {
                    return false;
                }
            } else if (targetCol == 2 && targetRow == 7) {
                if (canCastle && castleHandler.isLeftRowEmpty(color)) {
                    if (!checkHandler.inCheck(3, row, color) && !checkHandler.inCheck(2, row, color)) {
                        return true;
                    }
                } else {
                    return false;
                }
            }

            if (targetCol == 6 && targetRow == 0) {
                if (canCastle && castleHandler.isRightRowEmpty(color)) {
                    if (!checkHandler.inCheck(5, row, color) && !checkHandler.inCheck(6, row, color)) {
                        return true;
                    }
                } else {
                    return false;
                }
            } else if (targetCol == 2 && targetRow == 0) {
                if (canCastle && castleHandler.isLeftRowEmpty(color)) {
                    if (!checkHandler.inCheck(3, row, color) && !checkHandler.inCheck(2, row, color)) {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
        return false;
    }


}

