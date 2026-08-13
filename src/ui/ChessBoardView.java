package ui;

import javafx.scene.layout.GridPane;
import model.Game;
import model.Board;
import model.Piece;

public class ChessBoardView extends GridPane {
    private static final int BOARD_SIZE = 8;
    private final SquareView[][] squares = new SquareView[8][8];
    private final Game game;

    public ChessBoardView(Game game) {
        this.game = game;
        createBoard();
    }

    private void createBoard() {
        Board board = new Board();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                SquareView square = new SquareView(row, col);
                squares[row][col] = square;
                Piece piece = board.getPiece(row, col);
                if (piece != null) {
                    PieceView pieceView = new PieceView(piece);
                    square.getChildren().add(pieceView);
                }
                add(square, col, row);
            }
        }
    }

    public SquareView getSquare(int row, int col) {
        return squares[row][col];
    }

    public void refresh() {
        Board board = game.getBoard();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                SquareView square = squares[row][col];

                // Remove the old piece image
                square.getChildren().clear();

                Piece piece = board.getPiece(row, col);

                // Add the new piece image if there is a piece
                if (piece != null) {
                    square.getChildren().add(new PieceView(piece));
                }
            }
        }
    }
}