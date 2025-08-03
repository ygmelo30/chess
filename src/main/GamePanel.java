package main;

import piece.*;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class GamePanel extends JPanel implements Runnable {

    public static final int WIDTH = 1100;
    public static final int HEIGHT = 800;
    final int FPS = 60;
    Thread gameThread;
    Board board = new Board();
    Mouse mouse = new Mouse();
    Listener listener = new Listener();
    CheckHandler checkHandler = new CheckHandler();
    ExecutorService executorService = Executors.newFixedThreadPool(1);
    Castling castleHandler = new Castling();
    EndGameHandler endGameHandler;

    // PIECES
    public static CopyOnWriteArrayList<Piece> pieces = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<Piece> simPieces = new CopyOnWriteArrayList<>();
    public static ArrayList<Piece> promotionPieces = new ArrayList<>();
    Piece activeP;
    Piece whiteKing = new King(WHITE, 4, 7);
    Piece blackKing = new King(BLACK, 4, 0);
    public static Piece lastPieceMoved;

    // COLOR
    public static final int BLACK = 0;
    public static final int WHITE = 1;
    AtomicInteger currentColor = new AtomicInteger(WHITE);
    AtomicInteger currentMove = new AtomicInteger(1);

    // booleans
    boolean canMove;
    boolean validSquare;
    boolean canLegallyMove;
    boolean promotion = false;


    // other
    int previousRow;
    int previousCol;
    int movesSinceLastPieceTaken = 0;


    public GamePanel () throws UnsupportedAudioFileException, LineUnavailableException, IOException, URISyntaxException {
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
        pieces.add(new Pawn(WHITE, 1, 6));
        pieces.add(new Pawn(WHITE, 2, 6));
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
        pieces.add(new Pawn(BLACK, 2, 1));
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
    private void copyPieces(CopyOnWriteArrayList<Piece> source, CopyOnWriteArrayList<Piece> target) {

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
                try {
                    update();
                } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
                    throw new RuntimeException(e);
                }
                repaint();
                delta--;
            }
        }
    }
    private void update () throws LineUnavailableException, IOException, UnsupportedAudioFileException {


        if(promotion) {
            promoting(lastPieceMoved);
            checkForCheck();
            checkForEndGame();
        }
        else {
            // mouse button has been pressed if true
            if (mouse.pressed.get()) {
                if (activeP == null) {
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
                } else {
                    // if the player is holding a piece then simulate the move
                    simulate();
                }
            }

            // mouse button has been released
            if (!mouse.pressed.get()) {
                if (activeP != null) {
                    if (activeP.color == WHITE) {
                        if (activeP.canLegallyMoveTo(whiteKing.col, whiteKing.row, activeP.col, activeP.row)) {
                            activeP.resetPosition();
                        } else {
                            canLegallyMove = true;
                        }
                    } else {
                        if (activeP.canLegallyMoveTo(blackKing.col, blackKing.row, activeP.col, activeP.row)) {
                            activeP.resetPosition();
                        } else {
                            canLegallyMove = true;
                        }
                    }

                    //comment 2
                    if (canLegallyMove && validSquare && isNotSameSquare(activeP.col, activeP.row)) {
                        if (activeP.hittingPiece != null) {
                            simPieces.remove(activeP.hittingPiece.getIndex());
                            movesSinceLastPieceTaken = 0;
                            setMoveMusic(true);
                        } else if(activeP instanceof Pawn && lastPieceMoved instanceof Pawn && ((Pawn) activeP).enpassant) {
                            ((Pawn) activeP).enpassant = false;
                            simPieces.remove(lastPieceMoved.getIndex());
                            movesSinceLastPieceTaken = 0;
                            setMoveMusic(true);
                        }
                        else {
                            movesSinceLastPieceTaken++;
                            setMoveMusic(false);
                        }
                        copyPieces(simPieces, pieces);
                        activeP.updatePosition();

                        if (activeP instanceof King || activeP instanceof Rook) {
                            updateKingOrRookCastle(activeP.col, activeP.row, currentColor.get());
                        }
                        if (canPromote()) {
                            promotion = true;
                        } else {
                            toggleTurn();
                        }

                        checkForCheck();
                        checkForEndGame();


                        lastPieceMoved = activeP;
                        activeP = null;
                        canMove = false;
                        validSquare = false;
                        canLegallyMove = false;



                    } else {
                        activeP.resetPosition();
                        activeP = null;
                    }
                }

            }
        }
    }
    private boolean canPromote() {
        if(activeP instanceof Pawn) {
            if(activeP.color == WHITE && activeP.row == 0 || activeP.color == 0 && activeP.row == 7) {
                promotionPieces.clear();
                promotionPieces.add(new Queen(currentColor.get(), 9, 2));
                promotionPieces.add(new Rook(currentColor.get(), 9, 3));
                promotionPieces.add(new Bishop(currentColor.get(), 9, 4));
                promotionPieces.add(new Knight(currentColor.get(), 9, 5));
                return true;
            }
        }
        return false;
    }

    private void toggleTurn() {
        if(currentColor.get() == WHITE){
            currentColor.set(BLACK);
        } else {
            currentColor.set(WHITE);
        }
        currentMove.incrementAndGet();
    }
    private boolean checkForCheck () {
        if(currentColor.get() == 1) {
            boolean check = checkHandler.inCheck(whiteKing.col, whiteKing.row, 1);
            setCheckMusic(check);
            return check;
            } else {
            boolean check = checkHandler.inCheck(blackKing.col, blackKing.row, 0);
            setCheckMusic(check);
            return check;
        }

    }
    private boolean checkForCheckmate() {
        if (currentColor.get() == 1) {
            return checkHandler.checkMate(whiteKing.col, whiteKing.row, 1);
        } else {
            return checkHandler.checkMate(blackKing.col, blackKing.row, 0);
        }
    }
    private boolean checkForEndGame() {
        if(checkForCheck()) {
            if (checkForCheckmate()) {
                if (currentColor.get() == 1) {
                    endGameHandler = new EndGameHandler("black");
                    handleEndGameResponse();
                    return true;
                } else {
                    endGameHandler = new EndGameHandler("white");
                    handleEndGameResponse();
                    return true;
                }
            }
        }


        if(stalemate()) {
            endGameHandler = new EndGameHandler("stalemate");
            handleEndGameResponse();
            return true;
        }

        if(movesSinceLastPieceTaken >= 100) {
            endGameHandler = new EndGameHandler("50");
            handleEndGameResponse();
            return true;
        }
        return false;
    }
    private void setCheckMusic (boolean isInCheck) {
        executorService.execute(() -> {
            try {
                listener.setCheck(isInCheck);
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        });
    }
    private void setMoveMusic (boolean move) {
        if(move) {
            executorService.execute(() -> {
                try {
                    listener.setMoveType(true);
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            executorService.execute(() -> {
                try {
                    listener.setMoveType(false);
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                    throw new RuntimeException(e);
                }
            });
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

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(new Font("Arial Black", Font.PLAIN, 40));
        g2.setColor(Color.white);

        if(promotion) {
            g2.drawString("Promote to: ", 840, 150);
            for(Piece piece : promotionPieces) {
                g2.drawImage(piece.bufferedImage, piece.getX(piece.col), piece.getY(piece.row),
                        Board.SQUARE_SIZE, Board.SQUARE_SIZE, null);
            }
        } else {
            if(currentColor.get() ==  WHITE) {
                g2.drawString("White's turn", 820, 550);
            } else {
                g2.drawString("Black's turn", 820, 250);
            }
        }


        }
        private void promoting(Piece activeP) {
            if(mouse.pressed.get()) {
                for(Piece piece : promotionPieces) {
                    if (piece.col == mouse.x/Board.SQUARE_SIZE && piece.row == mouse.y/Board.SQUARE_SIZE) {
                        switch (piece.row) {
                            case 2: simPieces.add(new Queen(currentColor.get(), activeP.col, activeP.row)); break;
                            case 3: simPieces.add(new Rook(currentColor.get(), activeP.col, activeP.row)); break;
                            case 4: simPieces.add(new Bishop(currentColor.get(), activeP.col, activeP.row)); break;
                            case 5: simPieces.add(new Knight(currentColor.get(), activeP.col, activeP.row)); break;
                            default: break;
                        }
                        simPieces.remove(activeP.getIndex());
                        copyPieces(simPieces, pieces);
                        activeP = null;
                        promotion = false;
                        toggleTurn();
                    }
                }
            }
        }

    public void updateKingOrRookCastle (int targetCol, int targetRow, int currentColor) {
        if(activeP instanceof King) {
            if (currentColor == 1) {
                whiteKing.col = targetCol;
                whiteKing.row = targetRow;

                if(((King) activeP).canCastle && activeP.col == 6 && activeP.row == 7) {
                    castleHandler.updateWhiteRightRookPosition(currentColor);
                    copyPieces(simPieces, pieces);
                } else if(((King) activeP).canCastle && activeP.col == 2 && activeP.row == 7) {
                    castleHandler.updateWhiteLeftRookPosition(currentColor);
                }
            }

            else {
                blackKing.col = targetCol;
                blackKing.row = targetRow;

                if(((King) activeP).canCastle && activeP.col == 6 && activeP.row == 0) {
                    castleHandler.updateBlackRightRookPosition(currentColor);
                } else if(((King) activeP).canCastle && activeP.col == 2 && activeP.row == 0) {
                    castleHandler.updateBlackLeftRookPosition(currentColor);
                }
            }

            ((King) activeP).canCastle = false;
        } else {
                ((Rook) activeP).canCastle = false;
        }
    }
    private boolean stalemate() {
        int numOfPieces = 0;
        int kingCol = 0; int kingRow = 0;
        for(Piece piece : GamePanel.simPieces) {
            if(piece.color == currentColor.get()) {
                numOfPieces++;
                if(numOfPieces == 1) {
                    kingCol = piece.col;
                    kingRow = piece.row;
                }
            }
        }

        if(numOfPieces == 1) {
            return !checkHandler.kingCanMove(kingCol, kingRow, kingCol, kingRow);
        }

        return false;
    }
    private void resetGame() {
        simPieces.clear();
        pieces.clear();
        setPieces();
        copyPieces(pieces, simPieces);
        lastPieceMoved = null;
        movesSinceLastPieceTaken = 0;
        setCheckMusic(false);
    }
    private void handleEndGameResponse() {
        if(endGameHandler.handleResponse() == 0) {
            resetGame();
        } else {
            System.exit(0);
        }
    }





}


