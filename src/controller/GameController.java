package controller;

import javafx.animation.KeyFrame;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import model.*;
import ui.*;
import util.SoundManager;
import util.StockfishEngine;
import util.FenConverter;
import java.io.IOException;
import java.util.List;
import javafx.concurrent.Task;
import javafx.animation.Timeline;

public class GameController {
    private final Game game;
    private final ChessBoardView boardView;
    private final PromotionView promotionView;
    private final GameOverView gameOverView;
    private final StockfishEngine stockfish;
    private final Color stockfishColor = Color.BLACK;
    private final Difficulty difficulty;
    private final int stockfishThinkTime = 3000;
    private final Clock clock;
    private final ClockView clockView;
    private Timeline clockTimeline;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private boolean dragging = false;
    private double pressX;
    private double pressY;
    private int pressRow = -1;
    private int pressCol = -1;

    public GameController(Game game, ChessBoardView boardView, PromotionView promotionView, GameOverView gameOverView, Difficulty difficulty, ClockView clockView, int minutesPerSide) {
        this.game = game;
        this.boardView = boardView;
        this.promotionView = promotionView;
        this.gameOverView = gameOverView;
        this.clockView = clockView;
        this.difficulty = difficulty;

        clock = new Clock(minutesPerSide);
        clockView.update(clock);

        // Start stockfish
        stockfish = new StockfishEngine();
        try {
            stockfish.start();
            stockfish.setDifficulty(difficulty);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setupPromotionHandlers();
        setupBoardClickHandlers();
        setupClock();
    }

    private void setupBoardClickHandlers() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int clickedRow = row;
                int clickedCol = col;

                SquareView square = boardView.getSquare(row, col);

                // Mouse pressed
                square.setOnMousePressed(event -> {
                    // Don't let user click when game is over
                    if (game.isGameOver() || game.isPromotionPending()) return;

                    pressX = event.getSceneX();
                    pressY = event.getSceneY();

                    pressRow = clickedRow;
                    pressCol = clickedCol;

                    dragging = false;
                });

                // Mouse dragged
                square.setOnMouseDragged(event -> {
                    // Don't let user click when game is over
                    if (game.isGameOver() || game.isPromotionPending()) return;

                    double distanceX = event.getSceneX() - pressX;
                    double distanceY = event.getSceneY() - pressY;

                    double distance = Math.sqrt(
                            distanceX * distanceX +
                                    distanceY * distanceY
                    );

                    if (distance > 5 && !dragging) {
                        Piece piece = game.getBoard().getPiece(pressRow, pressCol);

                        if (piece == null) return;
                        if (piece.getColor() != game.getCurrentTurn()) return;

                        dragging = true;
                        selectedRow = pressRow;
                        selectedCol = pressCol;

                        showLegalMoves(piece, pressRow, pressCol);
                        boardView.startDragging(pressRow, pressCol, event.getSceneX(), event.getSceneY());
                    }

                    if (dragging) {
                        boardView.updateDraggingPiece(event.getSceneX(), event.getSceneY());
                    }
                });

                // Mouse released
                square.setOnMouseReleased(event -> {
                    if (game.isGameOver() || game.isPromotionPending()) {
                        dragging = false;
                        return;
                    }

                    if (dragging) {
                        // Stop the dragging animation
                        boardView.stopDragging(pressRow, pressCol);

                        // Fields
                        double x = event.getSceneX();
                        double y = event.getSceneY();

                        Point2D point =
                                boardView.sceneToLocal(x, y);

                        int toCol = (int) (point.getX() / 80);
                        int toRow = (int) (point.getY() / 80);

                        if (toRow >= 0 && toRow < 8 && toCol >= 0 && toCol < 8) {
                            handleDragMove(toRow, toCol);
                        } else {
                            boardView.clearHighlights();
                            selectedRow = -1;
                            selectedCol = -1;
                        }

                        dragging = false;
                    }
                });

                // Normal click
                square.setOnMouseClicked(event -> {
                    // Don't let user click when game is over
                    if (game.isGameOver() || game.isPromotionPending()) return;

                    // So that drag is not treated as a click
                    if (!dragging) handleSquareClick(clickedRow, clickedCol);
                    dragging = false;
                });
            }
        }
    }

    private void setupClock() {
        clockTimeline = new Timeline(new KeyFrame(
                Duration.seconds(1), event -> tickClock()));
        clockTimeline.setCycleCount(Timeline.INDEFINITE);
        clock.start(Color.WHITE);
        clockTimeline.play();
    }

    private void tickClock() {
        if (game.isGameOver() || game.isPromotionPending()) return;

        boolean timedOut = clock.tick();
        clockView.update(clock);

        if (timedOut) {
            clockTimeline.stop();
            game.loseOnTime(clock.getActiveColor());
            refreshBoard();
            handleGameOver();
        }
    }

    private void handleDragMove(int toRow, int toCol) {
        // We need a selected starting square
        if (selectedRow == -1) return;

        Move move = getMove(toRow, toCol);

        boolean isCapture = game.isCapture(move);
        boolean successful = game.makeMove(move);

        if (successful) {
            handleSuccessfulMove(isCapture);
        } else {
            boardView.clearHighlights();
            System.out.println("Illegal move");
            boardView.getSquare(toRow, toCol).showIllegalMove();
        }

        selectedRow = -1;
        selectedCol = -1;
    }

    private Move getMove(int toRow, int toCol) {
        Move move = new Move(selectedRow, selectedCol, toRow, toCol);
        return move;
    }

    private void handleSquareClick(int row, int col) {
        // First click: select a piece
        if (selectedRow == -1) {

            if (game.getBoard().getPiece(row, col) == null) {
                return;
            }

            if (game.getBoard().getPiece(row, col).getColor()
                    != game.getCurrentTurn()) {
                return;
            }

            selectedRow = row;
            selectedCol = col;
            Piece piece = game.getBoard().getPiece(row, col);

            highlightLegalMoves(piece, row, col);
            System.out.println("Selected: " + row + ", " + col);
            return;
        }

        // Second click: attempt the move
        Move move = getMove(row, col);

        boolean isCapture = game.isCapture(move);
        boolean successful = game.makeMove(move);

        if (successful) {
            handleSuccessfulMove(isCapture);
        } else {
            boardView.clearHighlights();
            System.out.println("Illegal move");
            boardView.getSquare(row, col).showIllegalMove();
        }

        // Clear selection
        selectedRow = -1;
        selectedCol = -1;
    }

    private List<Move> getLegalMoves(int row, int col, Piece piece) {
        return piece.getLegalMoves(game.getBoard(), row, col);
    }

    private void showLegalMoves(Piece piece, int row, int col) {
        highlightLegalMoves(piece, row, col);
    }

    private void highlightLegalMoves(Piece piece, int row, int col) {
        // Remove only old legal-move indicators
        boardView.clearMoveHighlights();

        for (Move move : game.getLegalMoves(row, col)) {
            int targetRow = move.getToRow();
            int targetCol = move.getToCol();
            boolean isCapture = game.isCapture(move);

            if (isCapture) {
                boardView.highlightCapture(targetRow, targetCol);
            } else {
                boardView.highlightSquare(targetRow, targetCol);
            }

            // Castling
            if (piece instanceof King &&
                    Math.abs(targetCol - col) == 2) {
                int direction = targetCol > col ? 1 : -1;

                // Highlight the square the king passes through.
                boardView.highlightSquare(row, col + direction);
            }
        }
    }

    private void highlightKingInCheck() {
        if (!game.isCurrentPlayerInCheck()) return;
        int[] kingPosition = game.getKingPosition(game.getCurrentTurn());

        if (kingPosition != null) {
            boardView.highlightCheck(kingPosition[0], kingPosition[1]);
        }
    }

    private void highlightLastMove() {
        Move lastMove = game.getLastMove();
        if (lastMove == null) return;

        // Remove yellow highlighting from the previous move
        boardView.clearLastMoveHighlight();

        boardView.highlightLastMove(
                lastMove.getFromRow(),
                lastMove.getFromCol(),
                lastMove.getToRow(),
                lastMove.getToCol()
        );
    }

    private void setupPromotionHandlers() {
        promotionView.getQueenButton().setOnAction(event -> {
            promote('Q');
        });

        promotionView.getRookButton().setOnAction(event -> {
            promote('R');
        });

        promotionView.getBishopButton().setOnAction(event -> {
            promote('B');
        });

        promotionView.getKnightButton().setOnAction(event -> {
            promote('N');
        });
    }

    private void makeStockfishMove() {
        String fen = FenConverter.boardToFen(game);

        Task<String> task = new Task<>() {
            @Override
            protected String call() throws Exception {
                return stockfish.getBestMove(fen, stockfishThinkTime, difficulty.getBlunderChance());
            }
        };

        task.setOnSucceeded(event -> {

            String bestMove = task.getValue();

            System.out.println(
                    "Stockfish best move: " + bestMove
            );

            Move move = stockfish.convertToMove(bestMove);
            boolean isCapture = game.isCapture(move);
            boolean successful = game.makeMove(move);

            if (successful) {
                handleSuccessfulMove(isCapture);
            } else {
                System.out.println(
                        "Stockfish returned an illegal move!"
                );
            }
        });

        task.setOnFailed(event -> {
            System.out.println(
                    "Stockfish failed."
            );

            task.getException().printStackTrace();
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void promote(char choice) {
        game.promotePawn(choice);
        promotionView.setVisible(false);
        refreshBoard();
        highlightLastMove();
        if (game.isGameOver()) {
            if (game.isCurrentPlayerInCheck()) highlightKingInCheck();
            handleGameOver();
            return;
        }

        if (game.isCurrentPlayerInCheck()) {
            SoundManager.playCheckSound();
        } else {
            SoundManager.playCaptureSound(); // promotion move — treat as non-quiet, or use playMoveSound() if you prefer
        }
        clock.switchTo(game.getCurrentTurn());
        clockView.update(clock);

        if (game.getCurrentTurn() == stockfishColor) makeStockfishMove();
    }

    private void handleSuccessfulMove(boolean isCapture) {
        System.out.println("Move successful");
        // Remove legal-move dots/rings
        boardView.clearMoveHighlights();

        if (game.isPromotionPending()) {
            promotionView.setColor(game.getPromotionColor());
            promotionView.positionAt(game.getPromotionRow(), game.getPromotionCol());
            promotionView.setVisible(true);
            refreshBoard();
            highlightLastMove();
            return;
        }

        if (game.isGameOver()) {
            refreshBoard();
            highlightLastMove();
            if (game.isCurrentPlayerInCheck()) highlightKingInCheck();
            handleGameOver();
            return;
        }

        if (game.isCurrentPlayerInCheck()) {
            SoundManager.playCheckSound();
        } else if (isCapture) {
            SoundManager.playCaptureSound();
        } else {
            SoundManager.playMoveSound();
        }
        refreshBoard();
        highlightLastMove();
        if (game.isCurrentPlayerInCheck()) highlightKingInCheck();
        clock.switchTo(game.getCurrentTurn());
        clockView.update(clock);
        if (game.getCurrentTurn() == stockfishColor) makeStockfishMove();
    }

    private void handleGameOver() {
        if (game.getWinner() != null) {
            SoundManager.playCheckmateSound();
            gameOverView.showCheckmate(game.getWinner());
        } else {
            gameOverView.showStalemate();
        }
    }

    private void refreshBoard() {
        boardView.refresh();
    }
}