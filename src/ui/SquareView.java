package ui;

import javafx.scene.layout.StackPane;

public class SquareView extends StackPane {
    private final int row;
    private final int col;

    public SquareView(int row, int col) {
        this.row = row;
        this.col = col;
        setPrefSize(80, 80);

        if ((row + col) % 2 == 0) {
            setStyle("-fx-background-color: #F0D9B5;");
        } else {
            setStyle("-fx-background-color: #B58863;");
        }
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}