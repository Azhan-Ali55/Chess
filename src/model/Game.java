package model;

import java.util.List;
import java.util.ArrayList;

public class Game {
    private final Board board;
    private final ChessRules rules;
    private Color currentTurn;
    private Move lastMove;

    // Promotion state
    private boolean promotionPending = false;
    private int promotionRow = -1;
    private int promotionCol = -1;

    // Win state
    private boolean gameOver = false;
    private Color winner = null;

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
        if (piece == null || piece.getColor() != currentTurn || gameOver || promotionPending) return false;

        // Castling
        if (piece instanceof King) {
            int row = (currentTurn == Color.WHITE) ? 7 : 0;

            // Kingside
            if (fromRow == row && fromCol == 4 && toRow == row && toCol == 6 && rules.isKingSideCastlePossible(currentTurn)) {
                // Move king
                movePiece(row, 4, row, 6, piece);

                // Move rook
                Piece rook = board.getPiece(row, 7);
                movePiece(row, 7, row, 5, rook);

                lastMove = move;
                switchTurn();
                checkGameOver();
                return true;
            }

            // Queenside
            if (fromRow == row && fromCol == 4 && toRow == row && toCol == 2 && rules.isQueenSideCastlePossible(currentTurn)) {
                // Move king
                movePiece(row, 4, row, 2, piece);

                // Move rook
                Piece rook = board.getPiece(row, 0);
                movePiece(row, 0, row, 3, rook);

                lastMove = move;
                switchTurn();
                checkGameOver();
                return true;
            }
        }

        // En Passant
        if (piece instanceof Pawn && rules.isEnPassantPossible(currentTurn, fromRow, fromCol, toRow, toCol, lastMove)) {
            Piece capturedPawn = board.getPiece(fromRow, toCol);
            boolean movementStatus = piece.hasMoved();
            movePiece(fromRow, fromCol, toRow, toCol, piece);
            board.removePiece(fromRow, toCol); // Remove the pawn right beside current pawn
            if (rules.isKingInCheck(currentTurn)) {
                movePiece(toRow, toCol, fromRow, fromCol, piece);
                board.setPiece(fromRow, toCol, capturedPawn);
                piece.setHasMoved(movementStatus);
                return false;
            }
            lastMove = move;
            switchTurn();
            checkGameOver();
            return true;
        }

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

                lastMove = move;
                if (piece instanceof Pawn && ((Pawn) piece).canPromote(toRow)) {
                    promotionPending = true;
                    promotionRow = toRow;
                    promotionCol = toCol;
                    return true;
                }
                switchTurn();
                checkGameOver();
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
        switch (Character.toUpperCase(choice)) {
            case 'Q' -> board.setPiece(row, col, new Queen(color));
            case 'R' -> board.setPiece(row, col, new Rook(color));
            case 'B' -> board.setPiece(row, col, new Bishop(color));
            case 'N' -> board.setPiece(row, col, new Knight(color));
            default -> throw new IllegalArgumentException("Invalid promotion choice");
        }
    }

    public void promotePawn(char choice) {
        if (!promotionPending) return;
        Piece pawn = board.getPiece(promotionRow, promotionCol);
        if (!(pawn instanceof Pawn)) return;

        Color color = pawn.getColor();
        promotePawn(promotionRow, promotionCol, color, choice);

        promotionPending = false;
        promotionRow = -1;
        promotionCol = -1;
        switchTurn();
        checkGameOver();
    }

    private void checkGameOver() {
        if (rules.isCheckmate(currentTurn)) {
            gameOver = true;
            if (currentTurn == Color.WHITE) {
                winner = Color.BLACK;
            } else {
                winner = Color.WHITE;
            }
            return;
        }

        if (rules.isStalemate(currentTurn)) {
            gameOver = true;
            winner = null;
        }
    }

    public boolean isCurrentPlayerInCheck() {
        return rules.isKingInCheck(currentTurn);
    }

    // Getters
    public List<Move> getLegalMoves(int row, int col) {
        Piece piece = board.getPiece(row, col);
        if (piece == null || piece.getColor() != currentTurn) return new ArrayList<>();
        List<Move> moves = new ArrayList<>(piece.getLegalMoves(board, row, col));

        // En passant
        if (piece instanceof Pawn && lastMove != null) {
            int direction = (piece.getColor() == Color.WHITE) ? -1 : 1;
            int[] columns = {col - 1, col + 1};

            for (int targetCol : columns) {
                if (!board.isInsideBoard(row + direction, targetCol)) continue;

                if (rules.isEnPassantPossible(currentTurn, row, col, row + direction, targetCol, lastMove)) {
                    moves.add(new Move(row, col, row + direction, targetCol));
                }
            }
        }
        return moves;
    }
    public Board getBoard() { return board; }
    public Color getCurrentTurn() { return currentTurn; }
    public boolean isPromotionPending() { return promotionPending; }
    public Color getPromotionColor() {
        if (!promotionPending) return null;
        Piece pawn = board.getPiece(promotionRow, promotionCol);
        if (!(pawn instanceof Pawn)) return null;

        return pawn.getColor();
    }
    public int getPromotionRow() { return promotionRow; }
    public int getPromotionCol() { return promotionCol; }
    public boolean isGameOver() { return gameOver; }
    public Color getWinner() { return winner; }
}
