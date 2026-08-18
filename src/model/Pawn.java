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
        int oneStepRow = row + direction;
        int twoStepRow = row + 2 * direction;
        if (board.isInsideBoard(twoStepRow, col) && board.getPiece(oneStepRow, col) == null
                && board.getPiece(twoStepRow, col) == null && ((getColor() == Color.WHITE && row == 6)
                || (getColor() == Color.BLACK && row == 1))) {
            legalMoves.add(new Move(row, col, twoStepRow, col));
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

    public boolean canPromote(int row) {
        return (getColor() == Color.WHITE && row == 0) || (getColor() == Color.BLACK && row == 7);
    }
}
