package model;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {
    public Queen(Color color) {
        super(color);
    }
    @Override
    public List<Move> getLegalMoves(Board board, int row, int col) {
        List<Move> legalMoves = new ArrayList<>();
        int[][] directions = {
                {-1, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, 0}, {1, 0}, {0, -1}, {0, 1}
        };

        // Using loop to check each direction
        for (int[] direction : directions) {
            int currentRow = row + direction[0];
            int currentCol = col + direction[1];

            // Loop to check moves
            while (board.isInsideBoard(currentRow, currentCol)) {
                Piece piece = board.getPiece(currentRow, currentCol);
                if (piece == null) {
                    legalMoves.add(new Move(row, col, currentRow, currentCol));
                    currentRow += direction[0];
                    currentCol += direction[1];
                } else if(piece.getColor() != getColor()) {
                    legalMoves.add(new Move(row, col, currentRow, currentCol));
                    break;
                } else {
                    break;
                }
            }
        }
        return legalMoves;
    }
}
