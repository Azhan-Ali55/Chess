package model;

import java.util.List;

public class Game {
    private final Board board;
    private final ChessRules rules;
    private Color currentTurn;

    public Game() {
        board = new Board();
        currentTurn = Color.WHITE;
        rules = new ChessRules(board);
    }

    // Helper methods
    public boolean makeMove(Move move) {
        int fromRow = move.getFromRow();
        int fromCol = move.getFromCol();
        int toRow = move.getToRow();
        int toCol = move.getToCol();
        Piece piece = board.getPiece(fromRow, fromCol);
        Piece capturedPiece = board.getPiece(toRow, toCol);
        if (piece == null || piece.getColor() != currentTurn) return false;
        List<Move> legalMoves = piece.getLegalMoves(board, fromRow, fromCol);
        for (Move m : legalMoves) {
            if (move.equals(m)) {
                boolean movementStatus = piece.hasMoved();
                movePiece(fromRow, fromCol, toRow, toCol, piece);
                if (rules.isKingInCheck(currentTurn)) {
                    movePiece(toRow, toCol, fromRow, fromCol, piece);
                    board.setPiece(toRow, toCol, capturedPiece);
                    piece.setHasMoved(movementStatus);
                    return false;
                }
                switchTurn();
                return true;
            }
        }
        return false;
    }

    private void movePiece(int fromRow, int fromCol, int toRow, int toCol, Piece piece) {
        board.removePiece(fromRow, fromCol);
        board.setPiece(toRow, toCol, piece);
        piece.setHasMoved(true);
    }

    private void switchTurn() {
        if (currentTurn == Color.WHITE) {
            currentTurn = Color.BLACK;
        } else {
            currentTurn = Color.WHITE;
        }
    }



    private void promotePawn(int row, int col, Color color, char choice) {
        switch (choice) {
            case 'Q' -> board.setPiece(row, col, new Queen(color));
            case 'R' -> board.setPiece(row, col, new Rook(color));
            case 'B' -> board.setPiece(row, col, new Bishop(color));
            case 'K' -> board.setPiece(row, col, new Knight(color));
        }
    }

    // Getters
    public Board getBoard() { return board; }
    public Color getCurrentTurn() { return currentTurn; }
}
