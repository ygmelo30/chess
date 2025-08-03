package main;

import piece.King;
import piece.Piece;
import piece.Rook;

public class Castling {
    public void updateWhiteRightRookPosition(int currentColor){
        for(Piece piece : GamePanel.simPieces) {
            if (piece.col == 7 && piece.row == 7 && piece instanceof Rook && piece.color == currentColor) {
                if(((Rook) piece).canCastle && isRightRowEmpty(currentColor)) {
                    piece.prevCol = 5;
                    piece.col = 5;
                    piece.updatePosition();
                }
            }
        }

    }
    public void updateWhiteLeftRookPosition(int currentColor){
        for(Piece piece : GamePanel.simPieces) {
            if (piece.col == 0 && piece.row == 7 && piece instanceof Rook && piece.color == currentColor) {
                if(((Rook) piece).canCastle && isLeftRowEmpty(currentColor)) {
                    piece.prevCol = 3;
                    piece.col = 3;
                    piece.updatePosition();
                }
            }
        }

    }
    public void updateBlackRightRookPosition(int currentColor){
        for(Piece piece : GamePanel.simPieces) {
            if (piece.col == 7 && piece.row == 0 && piece instanceof Rook && piece.color == currentColor) {
                if(((Rook) piece).canCastle && isRightRowEmpty(currentColor)) {
                    piece.prevCol = 5;
                    piece.col = 5;
                    piece.updatePosition();
                }
            }
        }

    }
    public void updateBlackLeftRookPosition(int currentColor){
        for(Piece piece : GamePanel.simPieces) {
            if (piece.col == 0 && piece.row == 0 && piece instanceof Rook && piece.color == currentColor) {
                if(((Rook) piece).canCastle && isLeftRowEmpty(currentColor)) {
                    piece.prevCol = 3;
                    piece.col = 3;
                    piece.updatePosition();
                }
            }
        }

    }

    public boolean isRightRowEmpty(int currentColor) {
        if(currentColor == 1) {
            for (int i = 1; i < 4; i++) {
                for (Piece piece : GamePanel.simPieces) {
                    if (piece.row == 7 && piece.col == 4 + i) {
                        if (piece instanceof King) {
                            if (piece.color != currentColor) {
                                return false;
                            }
                        } else if (piece instanceof Rook && piece.color == currentColor && piece.col == 7) {
                            return ((Rook) piece).canCastle;
                        } else {
                            return false;
                        }
                    }

                }
            }
        } else {
            for (int i = 1; i < 4; i++) {
                for (Piece piece : GamePanel.simPieces) {
                    if (piece.row == 0 && piece.col == 4 + i) {
                        if (piece instanceof King) {
                            if (piece.color != currentColor) {
                                return false;
                            }
                        } else if (piece instanceof Rook && piece.color == currentColor && piece.col == 7) {
                            return ((Rook) piece).canCastle;
                        } else {
                            return false;
                        }
                    }

                }
            }
        }
        return false;
    }
    public boolean isLeftRowEmpty(int currentColor) {
        if(currentColor == 1) {
            for (int i = 1; i < 5; i++) {
                for (Piece piece : GamePanel.simPieces) {
                    if (piece.row == 7 && piece.col == 4 - i) {
                        if (piece instanceof King) {
                            if (piece.color != currentColor) {
                                return false;
                            }
                        } else if (piece instanceof Rook && piece.color == currentColor && piece.col == 0) {
                            return ((Rook) piece).canCastle;
                        } else {
                            return false;
                        }
                    }

                }
            }
        } else {
            for (int i = 1; i < 5; i++) {
                for (Piece piece : GamePanel.simPieces) {
                    if (piece.row == 0 && piece.col == 4 - i) {
                        if (piece instanceof King) {
                            if (piece.color != currentColor) {
                                return false;
                            }
                        } else if (piece instanceof Rook && piece.color == currentColor && piece.col == 0) {
                            return ((Rook) piece).canCastle;
                        } else {
                            return false;
                        }
                    }

                }
            }
        }
        return false;
    }
}
