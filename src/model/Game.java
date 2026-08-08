package model;

import java.util.List;
import java.util.ArrayList;
public class Game {
    private final Board board;
    private Color currentTurn;

    public Game() {
        board = new Board();
        currentTurn = Color.WHITE;
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
                movePiece(fromRow, fromCol, toRow, toCol, piece);
                if (isKingInCheck(currentTurn)) {
                    movePiece(toRow, toCol, fromRow, fromCol, piece);
                    board.setPiece(toRow, toCol, capturedPiece);
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
    }

    private void switchTurn() {
        if (currentTurn == Color.WHITE) {
            currentTurn = Color.BLACK;
        } else {
            currentTurn = Color.WHITE;
        }
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

    private boolean isKingInCheck(Color color) {
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

    // Getters
    public Board getBoard() { return board; }
    public Color getCurrentTurn() { return currentTurn; }
}
