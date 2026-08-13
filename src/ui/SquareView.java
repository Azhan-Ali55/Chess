package ui;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class SquareView extends StackPane {
    private final int row;
    private final int col;
    private Rectangle highlightOverlay;
    private Circle moveDot;

    public SquareView(int row, int col) {
        this.row = row;
        this.col = col;
        setPrefSize(80, 80);
        setBaseColor();
    }

    private void setBaseColor() {
        if ((row + col) % 2 == 0) {
            setStyle("-fx-background-color: #F0D9B5;");
        } else {
            setStyle("-fx-background-color: #B58863;");
        }
    }

    public void highlight() {
        clearHighlight();

        highlightOverlay = new Rectangle();
        highlightOverlay.widthProperty().bind(widthProperty());
        highlightOverlay.heightProperty().bind(heightProperty());
        highlightOverlay.setFill(Color.rgb(0, 0, 0, 0.08));
        highlightOverlay.setMouseTransparent(true);

        moveDot = new Circle(9);
        moveDot.setFill(Color.rgb(50, 50, 50, 0.45));
        moveDot.setMouseTransparent(true);

        getChildren().addAll(highlightOverlay, moveDot);
    }

    public void clearHighlight() {
        if (highlightOverlay != null) {
            getChildren().remove(highlightOverlay);
            highlightOverlay = null;
        }

        if (moveDot != null) {
            getChildren().remove(moveDot);
            moveDot = null;
        }
    }

    public void showIllegalMove() {
        setStyle("-fx-background-color: #D9534F;");

        javafx.animation.PauseTransition pause =
                new javafx.animation.PauseTransition(
                        javafx.util.Duration.millis(250)
                );

        pause.setOnFinished(event -> setBaseColor());
        pause.play();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}