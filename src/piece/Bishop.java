package piece;

import main.GamePanel;

public class Bishop  extends Piece{
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
            int itr = Math.abs(targetRow - prevRow);
            for(int i = 0; i < itr; i++) {
                System.out.println("i is " +  i);
                for(Piece piece : GamePanel.simPieces) {
                    System.out.println("row is " + prevRow + i);
                    System.out.println("col is " + prevRow + i);
                    System.out.println("end of this iteration");
                    System.out.println("i is " +  i);
                    System.out.println("first" );
                    if(piece.row == prevRow + i && piece.col == prevCol + i && piece != this) {
                        return true;
                    }
                }
                System.out.println("end of checking this sqaure");
            }
        } else if(targetRow > prevRow && targetCol < prevCol) {
            int itr = Math.abs(targetRow - prevRow);
            for(int i = 0; i < itr; i++) {
                System.out.println("i is " +  i);
                for(Piece piece : GamePanel.simPieces) {
                    System.out.println("row is " + prevRow + i);
                    System.out.println("col is " + (targetCol - i));
                    System.out.println("end of this iteration");
                    System.out.println("i is " +  i);
                    System.out.println("second");
                    if(piece.row == prevRow + i && piece.col == targetCol - i && piece != this) {
                        return true;
                    }
                }
                System.out.println("end of checking this sqaure");
            }
        } else if(targetRow < prevRow && targetCol < prevCol) {
            int itr = Math.abs(targetRow - prevRow);
            for(int i = 0; i < itr; i++) {
                System.out.println("i is " +  i);
                for(Piece piece : GamePanel.simPieces) {
                    System.out.println("i is " +  i);
                    System.out.println("row is " + (targetRow - i));
                    System.out.println("col is " + (targetCol- i));
                    System.out.println("prevrow is " + prevRow);
                    System.out.println("prevcol is " + prevCol);
                    System.out.println("end of this iteration");
                    System.out.println("third");
                    if(piece.row == (prevRow - i) && piece.col == (prevCol - i) && piece != this) {
                        return true;
                    }
                }
            }
        } else if(targetRow < prevRow && targetCol > prevCol) {
            int itr = Math.abs(targetRow - prevRow);
            for(int i = 0; i < itr; i++) {
                System.out.println("i is " +  i);
                for(Piece piece : GamePanel.simPieces) {
                    System.out.println("i is " +  i);
                    System.out.println("row is " + (prevRow - i));
                    System.out.println("col is " + (targetCol+ i));
                    System.out.println("end of this iteration");
                    System.out.println("fourth");
                    if(piece.row == prevRow + i && piece.col == targetCol - i && piece != this) {
                        return true;
                    }
                }
                System.out.println("end of checking this sqaure");
            }
        }

        return false;
        }
    }

