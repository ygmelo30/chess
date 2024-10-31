package main;

import piece.*;

import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class GamePanel extends JPanel implements Runnable {

    public static final int WIDTH = 1100;
    public static final int HEIGHT = 800;
    final int FPS = 60;
    Thread gameThread;
    Board board = new Board();
    Mouse mouse = new Mouse();

    // PIECES
    public static ArrayList<Piece> pieces = new ArrayList<Piece>();
    public static ArrayList<Piece> simPieces = new ArrayList<Piece>();
    Piece activeP;
    Piece whiteKing = new King(WHITE, 4, 7);
    Piece blackKing = new King(BLACK, 4, 0);

    // COLOR
    public static final int BLACK = 0;
    public static final int WHITE = 1;
    AtomicInteger currentColor = new AtomicInteger(WHITE);

    // booleans
    boolean canMove;
    boolean validSquare;

    // other
    int previousRow;
    int previousCol;


    public GamePanel () {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
        addMouseListener(mouse);
        addMouseMotionListener(mouse);

        setPieces();
        copyPieces(pieces, simPieces);
    }

    public void launchGame () {
        gameThread = new Thread(this);
        gameThread.start();
    }
    public void setPieces() {
        // White pieces
        pieces.add(new Pawn(WHITE, 0, 6));
        pieces.add(new Pawn(WHITE, 1, 3));
        pieces.add(new Pawn(WHITE, 2, 3));
        pieces.add(new Pawn(WHITE, 3, 6));
        pieces.add(new Pawn(WHITE, 4, 6));
        pieces.add(new Pawn(WHITE, 5, 6));
        pieces.add(new Pawn(WHITE, 6, 6));
        pieces.add(new Pawn(WHITE, 7, 6));
        pieces.add(new Knight(WHITE, 1, 7));
        pieces.add(new Knight(WHITE, 6, 7));
        pieces.add(new Rook(WHITE, 0, 7));
        pieces.add(new Rook(WHITE, 7, 7));
        pieces.add(new Bishop(WHITE, 5, 7));
        pieces.add(new Bishop(WHITE, 2, 7));
        pieces.add(new Queen(WHITE, 3, 7));
        pieces.add(new King(WHITE, 4, 7));

        // Black pieces
        pieces.add(new Pawn(BLACK, 0, 1));
        pieces.add(new Pawn(BLACK, 1, 1));
        pieces.add(new Pawn(BLACK, 2, 4));
        pieces.add(new Pawn(BLACK, 3, 1));
        pieces.add(new Pawn(BLACK, 4, 1));
        pieces.add(new Pawn(BLACK, 5, 1));
        pieces.add(new Pawn(BLACK, 6, 1));
        pieces.add(new Pawn(BLACK, 7, 1));
        pieces.add(new Knight(BLACK, 1, 0));
        pieces.add(new Knight(BLACK, 6, 0));
        pieces.add(new Rook(BLACK, 0, 0));
        pieces.add(new Rook(BLACK, 7, 0));
        pieces.add(new Bishop(BLACK, 2, 0));
        pieces.add(new Bishop(BLACK, 5, 0));
        pieces.add(new Queen(BLACK, 3, 0));
        pieces.add(new King(BLACK, 4, 0));

    }
    private void copyPieces(ArrayList<Piece> source, ArrayList<Piece> target) {

        target.clear();
        for(int i = 0; i < source.size(); i++) {
           target.add(source.get(i));
        }
    }
    @Override
    public void run() {
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime)/drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }
    private void update () {

        // mouse button has been pressed if true
        if(mouse.pressed.get()) {
            if(activeP == null) {
                // if the active piece is null, check if you can pick the piece up
                for (Piece simPiece : simPieces) {
                    // if mouse is on piece of your color, pick it up as the active piece
                    if (simPiece.color == currentColor.get() &&
                            simPiece.col == mouse.x / Board.SQUARE_SIZE &&
                            simPiece.row == mouse.y / Board.SQUARE_SIZE) {

                        activeP = simPiece;
                        previousRow = simPiece.row;
                        previousCol = simPiece.col;
                    }
                }
            }
            else{
                // if the player is holding a piece then simulate the move
                simulate();
            }
        }

        // mouse button has been released
        if(!mouse.pressed.get()) {

            if(activeP != null) {

                if(validSquare && isNotSameSquare(activeP.col, activeP.row)) {
                    if(activeP.hittingPiece != null) {
                        simPieces.remove(activeP.hittingPiece.getIndex());
                    }
                    copyPieces(simPieces, pieces);
                    if(activeP instanceof King) {
                        if(activeP.color == WHITE) {
                            whiteKing.updatePosition();
                        }
                        else {
                            blackKing.updatePosition();
                        }
                    }
                    activeP.updatePosition();
                    toggleTurn();

                    // Check if the king of the current player is in check
                    if (currentColor.get() == WHITE) {
                        System.out.println("AHHH WHITE!");
                        System.out.println(whiteKing.col + " and row is " + whiteKing.row);
                        if(inCheck(whiteKing.col, whiteKing.row)) {
                            // Handle check situation, e.g., notify player or restrict moves
                            System.out.println("White King is in check!");
                        }
                    }
                    else {
                        System.out.println("AHHH  BLACK!");
                        // Check if the king of the current player is in check
                        System.out.println(blackKing.col + " and row is " + blackKing.row);
                        if (inCheck(blackKing.col, blackKing.row)) {
                            // Handle check situation, e.g., notify player or restrict moves
                            System.out.println("Black King is in check!");
                        }
                    }

                    canMove = false;
                    validSquare = false;

                }
                else {
                    activeP.resetPosition();
                    activeP = null;
                 }
            }
        }
    }

    private void toggleTurn() {
        if(currentColor.get() == WHITE){
            currentColor.set(BLACK);
        } else {
            currentColor.set(WHITE);
        }
    }
    //checks if the current active piece is trying to move into the same square
    public boolean isNotSameSquare(int targetCol, int targetRow) {
        if(targetCol != previousCol || targetRow != previousRow) {
            return true;
        }
        return false;
    }

    private void simulate() {

        canMove = false;
        validSquare = false;

        copyPieces(pieces, simPieces);

        //if a piece is being held, update its position
        activeP.x = mouse.x - Board.HALF_SQUARE_SIZE;
        activeP.y = mouse.y - Board.HALF_SQUARE_SIZE;
        activeP.col = activeP.getCol(activeP.x);
        activeP.row = activeP.getRow(activeP.y);

        // check if piece is hovering over a reachable and legal square
        if(activeP.canMove(activeP.col, activeP.row)) {

                    canMove = true;
                    validSquare = true;
                }

        }


    public void paintComponent (Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        // Board
            board.draw(g2);

        // Pieces
        for (Piece p : simPieces) {
                p.draw(g2);
        }

        if(activeP != null) {
            if(canMove && isNotSameSquare(activeP.col, activeP.row)) {
                g2.setColor(Color.white);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .7f));
                g2.fillRect(activeP.col*Board.SQUARE_SIZE, activeP.row*Board.SQUARE_SIZE,
                        Board.SQUARE_SIZE, Board.SQUARE_SIZE);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }

            // draw the active piece in the end, so it won't be hidden by board or colored square
                activeP.draw(g2);
            }
        }
    public boolean inCheck(int prevCol, int prevRow) {
        return isQueenOrRookChecking(prevCol,prevRow) || isBishopOrQueenChecking(prevCol,prevRow) || isPawnChecking(prevCol,prevRow) || isKnightChecking(prevCol,prevRow);
    }
    public boolean isBishopOrQueenChecking (int prevCol, int prevRow) {
        outerLoop:
        for (int i = 0; i < 8; i++) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.row == prevRow + i && piece.col == prevCol + i) {
                    if (piece.color != currentColor.get()) {
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
                if (piece.row == prevRow + i && piece.col == prevCol - i) {
                    if (piece.color != currentColor.get()) {
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
                if (piece.row == prevRow - i && piece.col == prevCol - i) {
                    if (piece.color != currentColor.get()) {
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
                if (piece.row == prevRow - i && piece.col == prevCol + i) {
                    if (piece.color != currentColor.get()) {
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
    public boolean isQueenOrRookChecking (int prevCol, int prevRow) {
        // checking all the squares below the king
        outerLoop:
        for (int i = prevRow + 1; i < 8; i++) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.row == i && piece.col == prevCol) {
                    if (piece.color != currentColor.get()) {
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
                    if (piece.color != currentColor.get()) {
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
                    if (piece.color != currentColor.get()) {
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
                    if (piece.color != currentColor.get()) {
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
    public boolean isPawnChecking (int prevCol, int prevRow) {
        // checking pawn check
        for (Piece piece : GamePanel.simPieces) {
            if(currentColor.get() == WHITE) {
                if (piece.row == prevRow - 1 && piece.col == prevCol + 1) {
                    if (piece.color != currentColor.get()) {
                        if (piece instanceof Pawn) {
                            System.out.println("pawn checking from top right.");
                            return true;
                        }
                    }
                } else if (piece.row == prevRow - 1 && piece.col == prevCol - 1) {
                    if (piece.color != currentColor.get()) {
                        if (piece instanceof Pawn) {
                            System.out.println("pawn checking from top left.");
                            return true;
                        }
                    }
                }
            }
            else {
                if (piece.row == prevRow + 1 && piece.col == prevCol - 1) {
                    if (piece.color != currentColor.get()) {
                        if (piece instanceof Pawn) {
                            System.out.println("pawn checking from top right.");
                            return true;
                        }
                    }
                } else if (piece.row == prevRow + 1 && piece.col == prevCol + 1) {
                    if (piece.color != currentColor.get()) {
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
    public boolean isKnightChecking (int prevCol, int prevRow) {
        for (Piece piece : GamePanel.simPieces) {
            if (piece.row == prevRow - 1 && piece.col == prevCol + 2 && piece.color != currentColor.get()) {
                if (piece instanceof Knight) {
                    System.out.println("knight checking from up 1 two right");
                    return true;
                }
            }
            else if (piece.row == prevRow - 1 && piece.col == prevCol - 2 && piece.color != currentColor.get()) {
                if (piece instanceof Knight) {
                    System.out.println("knight checking from up 1 two left");
                    return true;
                }
            }
            else if (piece.row == prevRow + 1 && piece.col == prevCol + 2 && piece.color != currentColor.get()) {
                if (piece instanceof Knight) {
                    System.out.println("knight checking from down 1 right 2");
                    return true;
                }
            }
            else if (piece.row == prevRow + 1 && piece.col == prevCol - 2 && piece.color != currentColor.get()) {
                if (piece instanceof Knight) {
                    System.out.println("knight checking from down 1 left 2");
                    return true;
                }
            }
            else if (piece.row == prevRow + 2 && piece.col == prevCol + 1 && piece.color !=currentColor.get()) {
                if (piece instanceof Knight) {
                    System.out.println("knight checking from down 2 right 1");
                    return true;
                }
            }
            else if (piece.row == prevRow + 2 && piece.col == prevCol - 1 && piece.color != currentColor.get()) {
                if (piece instanceof Knight) {
                    System.out.println("knight checking from up 2 left 1");
                    return true;
                }
            }
            else if (piece.row == prevRow - 2 && piece.col == prevCol + 1 && piece.color != currentColor.get()) {
                if (piece instanceof Knight) {
                    System.out.println("knight checking from down 2 right 1");
                    return true;
                }
            }
            else if (piece.row == prevRow - 2 && piece.col == prevCol - 1 && piece.color != currentColor.get()) {
                if (piece instanceof Knight) {
                    System.out.println("knight checking down 2 left 1");
                    return true;
                }
            }
        }
        return false;
    }
    }


