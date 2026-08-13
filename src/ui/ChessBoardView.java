package ui;

import javafx.scene.layout.GridPane;
import model.Game;
import model.Board;
import model.Piece;

public class ChessBoardView extends GridPane {
    private static final int BOARD_SIZE = 8;
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
                Piece piece = board.getPiece(row, col);
                if (piece != null) {
                    PieceView pieceView = new PieceView(piece);
                    square.getChildren().add(pieceView);
                }
                add(square, col, row);
            }
        }
    }
}