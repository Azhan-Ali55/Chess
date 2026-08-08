package model;

import java.util.List;

public class ChessRules {
    private final Board board;

    public ChessRules(Board board) {
        this.board = board;
    }

    private int[] findKing(Color color) {
        // Loop through all 64 squares to get kings position
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece currentPiece = board.getPiece(row, col);
                if (currentPiece instanceof King && currentPiece.getColor() == color) {
                    return new int[] {row , col};
                }
            }
        }
        return null;
    }

    public boolean isKingInCheck(Color color) {
        int[] kingPosition = findKing(color);
        if (kingPosition == null) return false;
        int kingRow = kingPosition[0];
        int kingCol = kingPosition[1];
        // Loop through all 64 square to get legal moves of enemy pieces
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece currentPiece = board.getPiece(row, col);
                if (currentPiece == null || currentPiece.getColor() == color) continue;

                // Logic to check only diagonal movement for pawn attacks
                if (currentPiece instanceof Pawn) {
                    int direction = (currentPiece.getColor() == Color.WHITE) ? -1 : 1;
                    if (row + direction == kingRow && (col - 1 == kingCol || col + 1 == kingCol)) return true;
                } else {
                    List<Move> legalMoves = currentPiece.getLegalMoves(board, row, col);
                    for (Move move : legalMoves) {
                        if (move.getToRow() == kingRow && move.getToCol() == kingCol) return true;
                    }
                }
            }
        }
        return false;
    }

    private void temporaryMove(int fromRow, int fromCol, int toRow, int toCol, Piece piece) {
        board.removePiece(fromRow, fromCol);
        board.setPiece(toRow, toCol, piece);
    }

    private boolean hasLegalMove(Color color) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece currentPiece = board.getPiece(row, col);
                if (currentPiece == null || currentPiece.getColor() != color) continue;
                List<Move> legalMoves = currentPiece.getLegalMoves(board, row, col);
                for (Move move : legalMoves) {
                    int fromRow = move.getFromRow();
                    int fromCol = move.getFromCol();
                    int toRow = move.getToRow();
                    int toCol = move.getToCol();
                    Piece capturedPiece = board.getPiece(toRow, toCol);

                    // Temp move
                    temporaryMove(fromRow, fromCol, toRow, toCol, currentPiece);
                    boolean isStillInCheck = isKingInCheck(color);

                    // Undo the move
                    temporaryMove(toRow, toCol, fromRow, fromCol, currentPiece);
                    board.setPiece(toRow, toCol, capturedPiece);

                    if (!isStillInCheck) return true;
                }
            }
        }
        return false;
    }

    public boolean isCheckmate(Color color) {
        return isKingInCheck(color) && !hasLegalMove(color);
    }

    public boolean isStalemate(Color color) {
        return !isKingInCheck(color) && !hasLegalMove(color);
    }

    public boolean isKingSideCastlePossible(Color color) {
        int row = (color == Color.WHITE) ? 7 : 0;
        Piece king = board.getPiece(row, 4);
        Piece rook = board.getPiece(row, 7);
        if (!(king instanceof King) || !(rook instanceof Rook) || king.hasMoved() || rook.hasMoved()) return false;
        if (board.getPiece(row, 5) != null || board.getPiece(row, 6) != null) return false; // There is no piece b/w king and rook
        if (isKingInCheck(color)) return false;

        // Make sure king doesn't move through check
        // Temporarily move king to f1/f8
        temporaryMove(row, 4, row, 5, king);

        boolean passesThroughCheck = isKingInCheck(color);

        // Undo
        temporaryMove(row, 5, row, 4, king);

        if (passesThroughCheck) return false;

        // Make sure king doesn't move into check position
        // Temporarily move king to g1/g8
        temporaryMove(row, 4, row, 6, king);

        boolean endsInCheck = isKingInCheck(color);

        // Undo
        temporaryMove(row, 6, row, 4, king);
        return !endsInCheck;
    }

    public boolean isQueenSideCastlePossible(Color color) {
        int row = (color == Color.WHITE) ? 7 : 0;
        Piece king = board.getPiece(row, 4);
        Piece rook = board.getPiece(row, 0);
        if (!(king instanceof King) || !(rook instanceof Rook) || king.hasMoved() || rook.hasMoved()) return false;
        if (board.getPiece(row, 1) != null || board.getPiece(row, 2) != null ||
                board.getPiece(row, 3) != null) return false; // There is no piece b/w king and rook

        if (isKingInCheck(color)) return false;

        // Make sure king doesn't move through check
        // Temporarily move king to d1/d8
        temporaryMove(row, 4, row, 3, king);

        boolean passesThroughCheck = isKingInCheck(color);

        // Undo
        temporaryMove(row, 3, row, 4, king);

        if (passesThroughCheck) return false;

        // Make sure king doesn't move into check position
        // Temporarily move king to c1/c8
        temporaryMove(row, 4, row, 2, king);

        boolean endsInCheck = isKingInCheck(color);

        // Undo
        temporaryMove(row, 2, row, 4, king);
        return !endsInCheck;
    }

    public boolean isEnPassantPossible(Color color, int fromRow, int fromCol, int toRow, int toCol, Move lastMove) {
        if (lastMove == null) return false;
        Piece pawn = board.getPiece(fromRow, fromCol);
        if (!(pawn instanceof Pawn) || pawn.getColor() != color) return false;

        // Pawn must move diagonally by one square
        if (Math.abs(toCol - fromCol) != 1) return false;

        int direction = (color == Color.WHITE) ? -1 : 1;
        if (toRow - fromRow != direction) return false;

        // Destination must be empty
        if (board.getPiece(toRow, toCol) != null) return false;

        // Check if pawn is beside the current pawn
        Piece enemyPawn = board.getPiece(fromRow, toCol);
        if (!(enemyPawn instanceof Pawn) || enemyPawn.getColor() == color) return false;

        // The enemy pawn must have just moved
        if (lastMove.getFromRow() != fromRow || lastMove.getToRow() != toRow) return false;

        return Math.abs(lastMove.getFromRow() - lastMove.getToRow()) == 2;
    }
}
