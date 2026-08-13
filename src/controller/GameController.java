package controller;

import model.Game;
import model.Move;
import model.Piece;
import ui.ChessBoardView;

public class GameController {
    private final Game game;
    private final ChessBoardView boardView;
    private int selectedRow = -1;
    private int selectedCol = -1;

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

                boardView.getSquare(row, col).setOnMouseClicked(event ->
                        handleSquareClick(clickedRow, clickedCol)
                );
            }
        }
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

            for (Move move : piece.getLegalMoves(game.getBoard(), row, col)) {
                int targetRow = move.getToRow();
                int targetCol = move.getToCol();

                if (game.getBoard().getPiece(targetRow, targetCol) != null) {
                    boardView.highlightCapture(targetRow, targetCol);
                } else {
                    boardView.highlightSquare(targetRow, targetCol);
                }
            }
            System.out.println("Selected: " + row + ", " + col);
            return;
        }

        // Second click: attempt the move
        Move move = new Move(selectedRow, selectedCol, row, col);

        boolean successful = game.makeMove(move);

        if (successful) {
            System.out.println("Move successful");
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

    private void refreshBoard() {
        boardView.refresh();
    }
}