package ui;

import javafx.scene.layout.GridPane;

public class ChessBoardView extends GridPane {
    private static final int BOARD_SIZE = 8;

    public ChessBoardView() {
        createBoard();
    }

    private void createBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                SquareView square = new SquareView(row, col);
                add(square, col, row);
            }
        }
    }
}