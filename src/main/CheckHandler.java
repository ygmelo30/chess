package main;
import piece.*;

import java.util.ArrayList;
import java.awt.Point;

public class CheckHandler {

    private Piece checkingPiece;
    private ArrayList<Point> checkingPath = new ArrayList<>();

    private void getPath (int kingCol, int kingRow) {
        int checkCol = kingCol;
        int checkRow = kingRow;
        Point square = new Point(0, 0);
        if(checkingPiece instanceof Bishop || checkingPiece instanceof Queen) {
            if((checkingPiece.col > checkCol) && (checkingPiece.row > checkRow)) {
                for(int i = (checkingPiece.row - checkRow); i > 0; i--) {
                        int x = checkCol + i;
                        int y = checkRow + i;
                        checkingPath.add(new Point(x,y));
                }
            }
            else if (checkingPiece.col < checkCol && checkingPiece.row < checkRow) {
                for(int i = (checkRow - checkingPiece.row); i > 0; i--) {
                    int x = checkCol - i;
                    int y = checkRow - i;
                    checkingPath.add(new Point(x,y));
                }
            }
            else if (checkingPiece.col > checkCol && checkingPiece.row < checkRow) {
                for(int i = (checkRow - checkingPiece.row); i > 0; i--) {
                    int x = checkCol + i;
                    int y = checkRow - i;
                    checkingPath.add(new Point(x,y));
                }
            }
            else if (checkingPiece.col < checkCol && checkingPiece.row > checkRow) {
                for(int i = (checkingPiece.row - checkRow); i > 0; i--) {
                    int x = checkCol - i;
                    int y = checkRow + i;
                    checkingPath.add(new Point(x,y));
                }
            }
        }


        if (checkingPiece instanceof Queen || checkingPiece instanceof Rook) {
            //all the squares below the king
            if (checkingPiece.col == checkCol && checkingPiece.row > checkRow) {
                for(int i = (checkingPiece.row - checkRow); i > 0; i--) {
                    int x = checkCol;
                    int y = checkRow + i;
                    checkingPath.add(new Point(x,y));
                }
            }
            // all the squares above the king
            else if (checkingPiece.col == checkCol && checkingPiece.row < checkRow) {
                for(int i = (checkRow - checkingPiece.row); i > 0; i--) {
                    int x = checkCol;
                    int y = checkRow - i;
                    checkingPath.add(new Point(x,y));
                }
            }
            // all the squares to the right of the king
            else if (checkingPiece.col > checkCol && checkingPiece.row == checkRow) {
                for(int i = (checkingPiece.col - checkCol); i > 0; i--) {
                    int x = checkCol + i;
                    int y = checkRow;
                    checkingPath.add(new Point(x,y));
                }
            }
            // all the squares to the left of king
            else if (checkingPiece.col < checkCol && checkingPiece.row == checkRow) {
                for(int i = (checkCol - checkingPiece.col); i > 0; i--) {
                    int x = checkCol - i;
                    int y = checkRow;
                    checkingPath.add(new Point(x,y));
                }
            }

        }
        if (checkingPiece instanceof Knight || checkingPiece instanceof Pawn) {
            int x = checkingPiece.col;
            int y = checkingPiece.row;
            checkingPath.add(new Point(x,y));
        }

    }

    public boolean checkMate (int kingCol, int kingRow, int color) {
        //Piece king = getKing(kingCol, kingRow);

        if(kingCanMove(kingCol, kingRow, kingCol, kingRow)) {
            checkingPath.clear();
            return false;
        }

        for(Point point : checkingPath) {
            for(Piece piece : GamePanel.simPieces) {
                if(piece.color == color) {
                    if(piece.canMove(point.x, point.y)) {
                        int orgCol = piece.col;
                        int orgRow = piece.row;
                        piece.col = point.x;
                        piece.row = point.y;
                        if(!piece.canLegallyMoveTo(kingCol, kingRow, point.x, point.y)) {
                            checkingPath.clear();
                            piece.col = orgCol;
                            piece.row = orgRow;
                            return false;
                        } else {
                            piece.col = orgCol;
                            piece.row = orgRow;
                        }
                    }
                }
            }
        }
        return true;
    }
    public Piece getKing(int kingCol, int kingRow) {
        for(Piece piece : GamePanel.simPieces) {
            if(piece.row == kingRow && piece.col == kingCol) {
                return piece;
            }
        }
        return null;
    }


    public boolean inCheck(int kingCol, int kingRow, int currentColor) {
        if(isPawnChecking(kingCol, kingRow, currentColor) || isQueenOrRookChecking(kingCol, kingRow, currentColor) || isBishopOrQueenChecking(kingCol, kingRow, currentColor) || isKnightChecking(kingCol, kingRow, currentColor)) {
            if(Thread.currentThread().getStackTrace()[2].getMethodName().equals("checkForCheck")) {
                getPath(kingCol, kingRow);
            }
            return true;
        }
        return false;
    }
    public boolean isBishopOrQueenChecking (int kingCol, int kingRow, int currentColor) {

        int checkCol = kingCol;
        int checkRow = kingRow;

        outerLoop:
        for (int i = 1; i < 8; i++) {
                for (Piece piece : GamePanel.simPieces) {
                    if (piece.row == checkRow + i && piece.col == checkCol + i) {
                        if (piece.color != currentColor && !piece.isPieceInPath(kingCol, kingRow)) {
                            if (piece instanceof Queen || piece instanceof Bishop) {
                                checkingPiece = piece;
                                return true;
                            }
                            else {
                                break outerLoop;
                            }
                        } else {
                            break outerLoop;
                        }
                    }
                }
        }

        // checking bishop or queen on bottom left diagonal
        outerLoop:
        for (int i = 1; i < 8; i++) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.row == checkRow + i && piece.col == checkCol - i) {
                    if (piece.color != currentColor && !piece.isPieceInPath(kingCol, kingRow)) {
                        if (piece instanceof Queen || piece instanceof Bishop) {
                            checkingPiece = piece;
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
        for (int i = 1; i < 8; i++) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.row == checkRow - i && piece.col == checkCol - i) {
                    if (piece.color != currentColor && !piece.isPieceInPath(kingCol, kingRow))  {
                        if (piece instanceof Queen || piece instanceof Bishop) {
                            checkingPiece = piece;
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
        for (int i = 1; i < 8; i++) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.row == checkRow - i && piece.col == checkCol + i) {
                    if (piece.color != currentColor && !piece.isPieceInPath(kingCol, kingRow)) {
                        if (piece instanceof Queen || piece instanceof Bishop) {
                            checkingPiece = piece;
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
    public boolean isQueenOrRookChecking (int kingCol, int kingRow, int currentColor) {
        int checkCol = kingCol;
        int checkRow = kingRow;

        // checking all the squares below the king
        outerLoop:
        for (int i = kingRow + 1; i < 8; i++) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.row == i && piece.col == checkCol) {
                    if (piece.color != currentColor && !piece.isPieceInPath(kingCol, kingRow)) {
                        if (piece instanceof Queen || piece instanceof Rook) {
                            checkingPiece = piece;
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
        for (int i = kingCol - 1; i >= 0; i--) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.row == checkRow && piece.col == i) {
                    if (piece.color != currentColor && !piece.isPieceInPath(kingCol, kingRow)) {
                        if (piece instanceof Queen || piece instanceof Rook) {
                            checkingPiece = piece;
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
        for (int i = kingRow - 1; i >= 0; i--) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.row == i && piece.col == checkCol) {
                    if (piece.color != currentColor && !piece.isPieceInPath(kingCol, kingRow)) {
                        if (piece instanceof Queen || piece instanceof Rook) {
                            checkingPiece = piece;
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
        for (int i = kingCol + 1; i < 8; i++) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.row == checkRow && piece.col == i) {
                    if (piece.color != currentColor && !piece.isPieceInPath(kingCol, kingRow)) {
                        if (piece instanceof Queen || piece instanceof Rook) {
                            checkingPiece = piece;
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
    public boolean isPawnChecking (int kingCol, int kingRow, int currentColor) {
        int checkCol = kingCol;
        int checkRow = kingRow;

        // checking pawn check
        for (Piece piece : GamePanel.simPieces) {
            if(currentColor == 1) {
                if (piece.row == checkRow - 1 && piece.col == checkCol + 1) {
                    if (piece.color != currentColor) {
                        if (piece instanceof Pawn ) {
                            checkingPiece = piece;
                            return true;
                        }
                    }
                } else if (piece.row == checkRow - 1 && piece.col == checkCol - 1) {
                    if (piece.color != currentColor) {
                        if (piece instanceof Pawn) {
                            checkingPiece = piece;
                            return true;
                        }
                    }
                }
            }
            else {
                if (piece.row == checkRow + 1 && piece.col == checkCol - 1) {
                    if (piece.color != currentColor) {
                        if (piece instanceof Pawn) {
                            checkingPiece = piece;
                            return true;
                        }
                    }
                } else if (piece.row == checkRow + 1 && piece.col == checkCol + 1) {
                    if (piece.color != currentColor) {
                        if (piece instanceof Pawn) {
                            checkingPiece = piece;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    public boolean isKnightChecking (int kingCol, int kingRow, int currentColor) {
        int checkCol = kingCol;
        int checkRow = kingRow;
        for (Piece piece : GamePanel.simPieces) {
            if (piece.row == checkRow - 1 && piece.col == checkCol + 2 && piece.color != currentColor) {
                if (piece instanceof Knight) {
                    checkingPiece = piece;
                    return true;
                }
            }
            else if (piece.row == checkRow - 1 && piece.col == checkCol - 2 && piece.color != currentColor) {
                if (piece instanceof Knight) {
                    checkingPiece = piece;
                    return true;
                }
            }
            else if (piece.row == checkRow + 1 && piece.col == checkCol + 2 && piece.color != currentColor) {
                if (piece instanceof Knight) {
                    checkingPiece = piece;
                    return true;
                }
            }
            else if (piece.row == checkRow + 1 && piece.col == checkCol - 2 && piece.color != currentColor) {
                if (piece instanceof Knight) {
                    checkingPiece = piece;
                    return true;
                }
            }
            else if (piece.row == checkRow + 2 && piece.col == checkCol + 1 && piece.color != currentColor) {
                if (piece instanceof Knight) {
                    checkingPiece = piece;
                    return true;
                }
            }
            else if (piece.row == checkRow + 2 && piece.col == checkCol - 1 && piece.color != currentColor) {
                if (piece instanceof Knight) {
                    checkingPiece = piece;
                    return true;
                }
            }
            else if (piece.row == checkRow - 2 && piece.col == checkCol + 1 && piece.color != currentColor) {
                if (piece instanceof Knight) {
                    checkingPiece = piece;
                    return true;
                }
            }
            else if (piece.row == checkRow - 2 && piece.col == checkCol - 1 && piece.color != currentColor) {
                if (piece instanceof Knight) {
                    checkingPiece = piece;
                    return true;
                }
            }
        }
        return false;
    }
    public boolean kingCanMove(int kingCol, int kingRow, int ogCol, int ogRow) {
        Piece king = getKing(kingCol, kingRow);
        assert  king != null;
        if(king.canMove(ogCol, ogRow - 1) && !king.canLegallyMoveTo(kingCol, kingRow, ogCol, ogRow - 1)) {
            return true;
        } else if (king.canMove(ogCol + 1, ogRow - 1) && !king.canLegallyMoveTo(kingCol, kingRow,ogCol + 1, ogRow - 1)) {
            return true;
        } else if (king.canMove(ogCol - 1, ogRow - 1) && !king.canLegallyMoveTo(kingCol, kingRow,ogCol - 1, ogRow - 1)) {
            return true;
        } else if (king.canMove(ogCol - 1, ogRow) && !king.canLegallyMoveTo(kingCol, kingRow,ogCol - 1, ogRow)) {
            return true;
        } else if (king.canMove(ogCol - 1, ogRow + 1) && !king.canLegallyMoveTo(kingCol, kingRow,ogCol - 1, ogRow + 1)) {
            return true;
        } else if (king.canMove(ogCol, ogRow + 1) && !king.canLegallyMoveTo(kingCol, kingRow, ogCol, ogRow + 1)) {
            return true;
        } else if (king.canMove(ogCol + 1, ogRow + 1) && !king.canLegallyMoveTo(kingCol, kingRow,ogCol + 1, ogRow + 1)) {
            return true;
        } else if (king.canMove(ogCol + 1, ogRow) && !king.canLegallyMoveTo(kingCol, kingRow,ogCol + 1, ogRow)) {
            return true;
        }
        return false;
    }

}
