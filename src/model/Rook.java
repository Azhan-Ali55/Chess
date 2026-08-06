package model;

import java.util.List;
import java.util.ArrayList;

public class Rook extends Piece {
    public Rook (Color color) {
        super(color);
    }
    @Override
    public List<Move> getLegalMoves(Board board, int row, int col) {
        List<Move> legalMoves = new ArrayList<>();
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}
        };

        // Using loop to check each direction
        for (int[] direction : directions) {
            int currentRow = row + direction[0];
            int currentCol = col + direction[1];

            // Loop to check rook moves
            while (board.isInsideBoard(currentRow, currentCol)) {
                if (board.getPiece(currentRow, currentCol) == null) {
                    legalMoves.add(new Move(row, col, currentRow, currentCol));
                    currentRow += direction[0];
                    currentCol += direction[1];
                } else if(board.getPiece(currentRow, currentCol).getColor() != getColor()) {
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
