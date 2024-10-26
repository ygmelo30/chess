package piece;

import main.GamePanel;

public class King  extends Piece{
    boolean canCastle = true;
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
                    canCastle = false;
                    inCheck();
                    return true;
                }
            }
        }
        return false;
    }
    public boolean inCheck() {
        // checking all the squares in to the right of the king
        for(int i = prevRow + 1; i < 7; i++) {
            for(Piece piece : GamePanel.simPieces) {
                if(piece.row == i && piece.col == prevCol) {
                    if(piece.color != this.color) {
                        if(piece instanceof Queen || piece instanceof Rook) {
                            System.out.println("queen or rook are checking.");
                            return true;
                        }
                    }
                }
            }
        }
        // checking all the squares to the left of the king
        for(int i = prevRow - 1; i > 0; i--) {
            for(Piece piece : GamePanel.simPieces) {
                if(piece.row == i && piece.col == prevCol) {
                    if(piece.color != this.color) {
                        if(piece instanceof Queen || piece instanceof Rook) {
                            System.out.println("queen or rook are checking.");
                            return true;
                        }
                    }
                }
            }
        }
        // checking all the squares above the king
        for(int i = prevCol + 1; i < 8; i++) {
            for(Piece piece : GamePanel.simPieces) {
                if(piece.row == prevRow && piece.col == i) {
                    if(piece.color != this.color) {
                        if(piece instanceof Queen || piece instanceof Rook) {
                            System.out.println("queen or rook are checking.");
                            return true;
                        }
                    }
                }
            }
        }
        // checking all the squares behind the king
        for(int i = prevCol - 1; i > 7; i--) {
            for(Piece piece : GamePanel.simPieces) {
                if(piece.row == i && piece.col == prevCol) {
                    if(piece.color != this.color) {
                        if(piece instanceof Queen || piece instanceof Rook) {
                            System.out.println("queen or rook are checking.");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    public boolean isRowEmpty() {
        for(int i = 0; i < 3; i++) {
            for(Piece piece : GamePanel.simPieces) {
                if(piece.row == prevRow && piece.col == prevCol + 1) {
                    if(piece instanceof Rook) {
                        if(((Rook) piece).canCastle) {
                            return true;
                        }
                    } else {
                        return false;
                    }
                }
            }
        }
        for(int i = 3; i != 0; i--) {
            for(Piece piece : GamePanel.simPieces) {
                if(piece.row == prevRow && piece.col == prevCol - 1) {
                    if(piece instanceof Rook) {
                        if(((Rook) piece).canCastle) {
                            return true;
                        }
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }

}