package controller;

import model.Game;
import model.King;
import model.Move;
import model.Piece;
import ui.ChessBoardView;

public class HighlightController {
    private final Game game;
    private final ChessBoardView boardView;

    public HighlightController(Game game, ChessBoardView boardView) {
        this.game = game;
        this.boardView = boardView;
    }

    public void highlightLegalMoves(Piece piece, int row, int col) {
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

    public void highlightKingInCheck() {
        if (!game.isCurrentPlayerInCheck()) return;
        int[] kingPosition = game.getKingPosition(game.getCurrentTurn());

        if (kingPosition != null) {
            boardView.highlightCheck(kingPosition[0], kingPosition[1]);
        }
    }

    public void highlightLastMove() {
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

    public void clearMoveHighlights() {
        boardView.clearMoveHighlights();
    }

    public void clearAllHighlights() {
        boardView.clearHighlights();
    }
}