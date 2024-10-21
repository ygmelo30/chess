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
                        isRowEmpty();
                    return true;
                }
            }
        }
        return false;
    }
//    public boolean inCheck() {
//        for()
//    }
    public boolean isRowEmpty() {
        for(int i = 0; i < 3; i++) {
            for(Piece piece : GamePanel.simPieces) {
                if(piece.row == prevRow && piece.col == prevCol + 1) {
                    if(piece instanceof Rook) {
                        if(((Rook) piece).canCastle) {
                            System.out.println("rook can castle");
                            return true;
                        }
                    } else {
                        System.out.println("inner false");
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
                            System.out.println("second rook can castle");
                            return true;
                        }
                    } else {
                        System.out.println("second inner false");
                        return false;
                    }
                }
            }
        }
        System.out.println("second outer false");
        return false;
    }

}