package model;

import java.util.List;
import java.util.ArrayList;

public class Pawn extends Piece {
    public Pawn (Color color) {
        super(color);
    }
    @Override
    public List<Move> getLegalMoves(Board board, int row, int col) {
        List<Move> legalMoves = new ArrayList<>();
        int direction = (getColor() == Color.WHITE) ? -1 : 1;

        // Handling one square movement
        if (board.isInsideBoard(row + direction, col) && board.getPiece(row + direction, col) == null) {
            legalMoves.add(new Move(row, col, row+direction, col));
        }

        // Two square movement
        if ((getColor() == Color.WHITE && board.getPiece(row + 2 * direction, col) == null && row == 6) ||
                (getColor() == Color.BLACK && board.getPiece(row + 2 * direction, col) == null && row == 1)) {
            legalMoves.add(new Move(row, col, row + 2 * direction, col));
        }

        // Right Diagonal movement
        if (board.isInsideBoard(row + direction, col + 1)) {
            Piece rightPiece = board.getPiece(row + direction, col + 1);
            if (rightPiece != null && rightPiece.getColor() != getColor()) {
                legalMoves.add(new Move(row, col, row + direction, col + 1));
            }
        }

        // Left Diagonal movement
        if (board.isInsideBoard(row + direction, col - 1)) {
            Piece leftPiece = board.getPiece(row + direction, col - 1);
            if (leftPiece != null && leftPiece.getColor() != getColor()) {
                legalMoves.add(new Move(row, col, row + direction, col - 1));
            }
        }
        return legalMoves;
    }
}
