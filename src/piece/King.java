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
        return isQueenOrRookChecking() || isBishopOrQueenChecking() || isPawnChecking() || isKnightChecking();
    }
    public boolean isBishopOrQueenChecking () {
        outerLoop:
        for (int i = 0; i < 8; i++) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.row == prevRow + i && piece.col == prevCol + i && piece != this) {
                    if (piece.color != this.color) {
                        if (piece instanceof Queen || piece instanceof Bishop) {
                            System.out.println("queen or bishop are checking king from bottom right.");
                            return true;
                        }
                        else {
                            break outerLoop;
                        }
                    }
                    else {
                        break outerLoop;
                    }
                }
            }
        }
        // checking bishop or queen on bottom left diagonal
        outerLoop:
        for (int i = 0; i < 8; i++) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.row == prevRow + i && piece.col == prevCol - i && piece != this) {
                    if (piece.color != this.color) {
                        if (piece instanceof Queen || piece instanceof Bishop) {
                            System.out.println("queen or bishop are checking king from bottom left.");
                            return true;
                        }
                        else {
                            break outerLoop;
                        }
                    }
                    else {
                        break outerLoop;
                    }
                }
            }
        }
        // checking bishop or queen on top left diagonal
        outerLoop:
        for (int i = 0; i < 8; i++) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.row == prevRow - i && piece.col == prevCol - i && piece != this) {
                    if (piece.color != this.color) {
                        if (piece instanceof Queen || piece instanceof Bishop) {
                            System.out.println("queen or bishop are checking king from top left.");
                            return true;
                        }
                        else {
                            break outerLoop;
                        }
                    }
                    else {
                        break outerLoop;
                    }
                }
            }
        }
        // checking bishop or queen on top right diagonal
        outerLoop:
        for (int i = 0; i < 8; i++) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.row == prevRow - i && piece.col == prevCol + i && piece != this) {
                    if (piece.color != this.color) {
                        if (piece instanceof Queen || piece instanceof Bishop) {
                            System.out.println("queen or bishop or are checking king from top right.");
                            return true;
                        }
                        else {
                            break outerLoop;
                        }
                    }
                    else {
                        break outerLoop;
                    }
                }
            }
        }
        return false;
    }
    public boolean isQueenOrRookChecking () {
        // checking all the squares below the king
        outerLoop:
        for (int i = prevRow + 1; i < 8; i++) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.row == i && piece.col == prevCol) {
                    if (piece.color != this.color) {
                        if (piece instanceof Queen || piece instanceof Rook) {
                            System.out.println("queen or rook are checking below king.");
                            return true;
                        }
                        else {
                            break outerLoop;
                        }
                    }
                    else {
                        break outerLoop;
                    }
                }
            }
        }
        // checking all the squares to the left of the king
        outerLoop:
        for (int i = prevCol - 1; i >= 0; i--) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.row == prevRow && piece.col == i) {
                    if (piece.color != this.color) {
                        if (piece instanceof Queen || piece instanceof Rook) {
                            System.out.println("queen or rook are checking left of king.");
                            return true;
                        }
                        else {
                            break outerLoop;
                        }
                    }
                    else {
                        break outerLoop;
                    }
                }
            }
        }
        // checking all the squares above the king
        outerLoop:
        for (int i = prevRow - 1; i >= 0; i--) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.row == i && piece.col == prevCol) {
                    if (piece.color != this.color) {
                        if (piece instanceof Queen || piece instanceof Rook) {
                            System.out.println("queen or rook are checking above king.");
                            return true;
                        }
                        else {
                            break outerLoop;
                        }
                    }
                    else {
                        break outerLoop;
                    }
                }
            }
        }
        // checking all the squares right of the king
        outerLoop:
        for (int i = prevCol + 1; i < 8; i++) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.row == prevRow && piece.col == i) {
                    if (piece.color != this.color) {
                        if (piece instanceof Queen || piece instanceof Rook) {
                            System.out.println("queen or rook are checking right king.");
                            return true;
                        }
                        else {
                            break outerLoop;
                        }
                    }
                    else {
                        break outerLoop;
                    }
                }
            }
        }
        return false;
    }
    public boolean isPawnChecking () {
        // checking pawn check
        for (Piece piece : GamePanel.simPieces) {
            if(color == GamePanel.WHITE) {
                if (piece.row == prevRow - 1 && piece.col == prevCol + 1 && piece != this) {
                    if (piece.color != this.color) {
                        if (piece instanceof Pawn) {
                            System.out.println("pawn checking from top right.");
                            return true;
                        }
                    }
                } else if (piece.row == prevRow - 1 && piece.col == prevCol - 1 && piece != this) {
                    if (piece.color != this.color) {
                        if (piece instanceof Pawn) {
                            System.out.println("pawn checking from top left.");
                            return true;
                        }
                    }
                }
            }
            else {
                if (piece.row == prevRow + 1 && piece.col == prevCol - 1 && piece != this) {
                    if (piece.color != this.color) {
                        if (piece instanceof Pawn) {
                            System.out.println("pawn checking from top right.");
                            return true;
                        }
                    }
                } else if (piece.row == prevRow + 1 && piece.col == prevCol + 1 && piece != this) {
                    if (piece.color != this.color) {
                        if (piece instanceof Pawn) {
                            System.out.println("pawn checking from top left.");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    public boolean isKnightChecking () {
        for (Piece piece : GamePanel.simPieces) {
            if (piece.row == prevRow - 1 && piece.col == prevCol + 2 && piece.color != this.color) {
                if (piece instanceof Knight) {
                    System.out.println("knight checking from up 1 two right");
                    return true;
                }
            }
            else if (piece.row == prevRow - 1 && piece.col == prevCol - 2 && piece.color != this.color) {
                if (piece instanceof Knight) {
                    System.out.println("knight checking from up 1 two left");
                    return true;
                }
            }
            else if (piece.row == prevRow + 1 && piece.col == prevCol + 2 && piece.color != this.color) {
                if (piece instanceof Knight) {
                    System.out.println("knight checking from down 1 right 2");
                    return true;
                }
            }
            else if (piece.row == prevRow + 1 && piece.col == prevCol - 2 && piece.color != this.color) {
                if (piece instanceof Knight) {
                    System.out.println("knight checking from down 1 left 2");
                    return true;
                }
            }
            else if (piece.row == prevRow + 2 && piece.col == prevCol + 1 && piece.color != this.color) {
                if (piece instanceof Knight) {
                    System.out.println("knight checking from down 2 right 1");
                    return true;
                }
            }
            else if (piece.row == prevRow + 2 && piece.col == prevCol - 1 && piece.color != this.color) {
                if (piece instanceof Knight) {
                    System.out.println("knight checking from up 2 left 1");
                    return true;
                }
            }
            else if (piece.row == prevRow - 2 && piece.col == prevCol + 1 && piece.color != this.color) {
                if (piece instanceof Knight) {
                    System.out.println("knight checking from down 2 right 1");
                    return true;
                }
            }
            else if (piece.row == prevRow - 2 && piece.col == prevCol - 1 && piece.color != this.color) {
                if (piece instanceof Knight) {
                    System.out.println("knight checking down 2 left 1");
                    return true;
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