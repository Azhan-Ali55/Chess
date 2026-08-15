package controller;

import model.Game;
import model.Move;
import model.Piece;
import util.SoundManager;
import ui.ChessBoardView;
import ui.SquareView;

import java.util.List;

public class GameController {
    private final Game game;
    private final ChessBoardView boardView;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private boolean dragging = false;
    private double pressX;
    private double pressY;
    private int pressRow = -1;
    private int pressCol = -1;

    public GameController(Game game, ChessBoardView boardView) {
        this.game = game;
        this.boardView = boardView;
        setupBoardClickHandlers();
    }

    private void setupBoardClickHandlers() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int clickedRow = row;
                int clickedCol = col;

                SquareView square = boardView.getSquare(row, col);

                // Mouse pressed
                square.setOnMousePressed(event -> {
                    pressX = event.getSceneX();
                    pressY = event.getSceneY();

                    pressRow = clickedRow;
                    pressCol = clickedCol;

                    dragging = false;
                });

                // Mouse dragged
                square.setOnMouseDragged(event -> {
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
                    }
                });

                // Mouse released
                square.setOnMouseReleased(event -> {

                    if (dragging) {
                        double x = event.getSceneX();
                        double y = event.getSceneY();

                        javafx.geometry.Point2D point =
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

                    // So that drag is not treated as a click
                    if (!dragging) handleSquareClick(clickedRow, clickedCol);
                    dragging = false;
                });
            }
        }
    }

    private void handleDragMove(int toRow, int toCol) {
        // We need a selected starting square
        if (selectedRow == -1) return;

        Move move = getMove(toRow, toCol);

        boolean isCapture = game.getBoard().getPiece(toRow, toCol) != null;
        boolean successful = game.makeMove(move);

        if (successful) {
            System.out.println("Move successful");
            if (isCapture) SoundManager.playCaptureSound();
            SoundManager.playMoveSound();
            refreshBoard();
        } else {
            System.out.println("Illegal move");
            boardView.getSquare(toRow, toCol).showIllegalMove();
        }

        boardView.clearHighlights();
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

        boolean isCapture = game.getBoard().getPiece(row, col) != null;
        boolean successful = game.makeMove(move);

        if (successful) {
            System.out.println("Move successful");
            if (isCapture) SoundManager.playCaptureSound();
            SoundManager.playMoveSound();
            refreshBoard();
        } else {
            System.out.println("Illegal move");
            boardView.getSquare(row, col).showIllegalMove();
        }

        boardView.clearHighlights();
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
        for (Move move : getLegalMoves(row, col, piece)) {

            int targetRow = move.getToRow();
            int targetCol = move.getToCol();

            if (game.getBoard().getPiece(targetRow, targetCol) != null) {
                boardView.highlightCapture(targetRow, targetCol);
            } else {
                boardView.highlightSquare(targetRow, targetCol);
            }
        }
    }

    private void refreshBoard() {
        boardView.refresh();
    }
}