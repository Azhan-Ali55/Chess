package controller;

import javafx.geometry.Point2D;
import model.Game;
import model.Move;
import model.Piece;
import ui.ChessBoardView;
import ui.SquareView;
import java.util.function.BiFunction;

public class BoardInputController {
    private final Game game;
    private final ChessBoardView boardView;
    private final HighlightController highlightController;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private boolean dragging = false;
    private double pressX;
    private double pressY;
    private int pressRow = -1;
    private int pressCol = -1;

    // True if the move was legal and applied
    private BiFunction<Move, Boolean, Boolean> onMoveAttempted;

    public BoardInputController(Game game, ChessBoardView boardView, HighlightController highlightController) {
        this.game = game;
        this.boardView = boardView;
        this.highlightController = highlightController;
    }

    public void setOnMoveAttempted(BiFunction<Move, Boolean, Boolean> onMoveAttempted) {
        this.onMoveAttempted = onMoveAttempted;
    }

    public void setup() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int clickedRow = row;
                int clickedCol = col;
                SquareView square = boardView.getSquare(row, col);

                square.setOnMousePressed(event -> {
                    if (game.isGameOver() || game.isPromotionPending()) return;
                    pressX = event.getSceneX();
                    pressY = event.getSceneY();
                    pressRow = clickedRow;
                    pressCol = clickedCol;
                    dragging = false;
                });

                square.setOnMouseDragged(event -> {
                    if (game.isGameOver() || game.isPromotionPending()) return;

                    double distanceX = event.getSceneX() - pressX;
                    double distanceY = event.getSceneY() - pressY;
                    double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

                    if (distance > 5 && !dragging) {
                        Piece piece = game.getBoard().getPiece(pressRow, pressCol);
                        if (piece == null) return;
                        if (piece.getColor() != game.getCurrentTurn()) return;

                        dragging = true;
                        selectedRow = pressRow;
                        selectedCol = pressCol;

                        highlightController.highlightLegalMoves(piece, pressRow, pressCol);
                        boardView.startDragging(pressRow, pressCol, event.getSceneX(), event.getSceneY());
                    }

                    if (dragging) {
                        boardView.updateDraggingPiece(event.getSceneX(), event.getSceneY());
                    }
                });

                square.setOnMouseReleased(event -> {
                    if (game.isGameOver() || game.isPromotionPending()) {
                        dragging = false;
                        return;
                    }

                    if (dragging) {
                        boardView.stopDragging(pressRow, pressCol);

                        Point2D point = boardView.sceneToLocal(event.getSceneX(), event.getSceneY());
                        int toCol = (int) (point.getX() / 80);
                        int toRow = (int) (point.getY() / 80);

                        if (toRow >= 0 && toRow < 8 && toCol >= 0 && toCol < 8) {
                            attemptMove(toRow, toCol);
                        } else {
                            boardView.clearHighlights();
                            selectedRow = -1;
                            selectedCol = -1;
                        }

                        dragging = false;
                    }
                });

                square.setOnMouseClicked(event -> {
                    if (game.isGameOver() || game.isPromotionPending()) return;
                    if (!dragging) handleSquareClick(clickedRow, clickedCol);
                    dragging = false;
                });
            }
        }
    }

    private void handleSquareClick(int row, int col) {
        if (selectedRow == -1) {
            Piece piece = game.getBoard().getPiece(row, col);
            if (piece == null || piece.getColor() != game.getCurrentTurn()) return;

            selectedRow = row;
            selectedCol = col;
            highlightController.highlightLegalMoves(piece, row, col);
            System.out.println("Selected: " + row + ", " + col);
            return;
        }

        attemptMove(row, col);
    }

    private void attemptMove(int toRow, int toCol) {
        if (selectedRow == -1) return;

        Move move = new Move(selectedRow, selectedCol, toRow, toCol);
        boolean isCapture = game.isCapture(move);

        boolean successful = onMoveAttempted != null && onMoveAttempted.apply(move, isCapture);

        if (!successful) {
            boardView.clearHighlights();
            System.out.println("Illegal move");
            boardView.getSquare(toRow, toCol).showIllegalMove();
        }

        selectedRow = -1;
        selectedCol = -1;
    }
}