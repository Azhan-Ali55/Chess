package model;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    public Knight (Color color) {
        super(color);
    }
    @Override
    public List<Move> getLegalMoves(Board board, int row, int col) {
        List<Move> legalMoves = new ArrayList<>();
        int[][] directions = {
                {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {2, -1}, {2, 1}
        };
        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];
            if (board.isInsideBoard(newRow, newCol)) {
                Piece piece = board.getPiece(newRow, newCol);
                if (piece == null) {
                    legalMoves.add(new Move(row, col, newRow, newCol));
                } else if (piece.getColor() != getColor()) {
                    legalMoves.add(new Move(row, col, newRow, newCol));
                }
            }
        }
        return legalMoves;
    }
}
