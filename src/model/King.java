package model;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    public King(Color color) {
        super(color);
    }
    @Override
    public List<Move> getLegalMoves(Board board, int row, int col) {
        List<Move> legalMoves = new ArrayList<>();
        int[][] directions = {
                {-1, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, 0}, {1, 0}, {0, -1}, {0, 1}
        };
        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];
            if (board.isInsideBoard(newRow, newCol)) {
                Piece piece = board.getPiece(newRow, newCol);
                if (piece == null || piece.getColor() != getColor()) {
                    legalMoves.add(new Move(row, col, newRow, newCol));
                }
            }
        }
        return legalMoves;
    }
}
