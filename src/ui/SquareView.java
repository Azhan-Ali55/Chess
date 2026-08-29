package ui;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class SquareView extends StackPane {
    private final int row;
    private final int col;
    private Rectangle highlightOverlay;
    private Circle moveDot;
    private Circle captureRing;
    private Rectangle checkOverlay;
    private Rectangle lastMoveOverlay;

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
        StackPane.setAlignment(moveDot, Pos.CENTER);

        getChildren().addAll(highlightOverlay, moveDot);
    }

    public void highlightCapture() {
        clearHighlight();

        captureRing = new Circle(38);
        captureRing.setFill(Color.TRANSPARENT);
        captureRing.setStroke(Color.rgb(50, 50, 50, 0.45));
        captureRing.setStrokeWidth(7);
        captureRing.setMouseTransparent(true);

        StackPane.setAlignment(captureRing, Pos.CENTER);
        getChildren().add(captureRing);
    }

    public void highlightCheck() {
        if (checkOverlay != null) {
            getChildren().remove(checkOverlay);
        }

        checkOverlay = new Rectangle();
        checkOverlay.widthProperty().bind(widthProperty());
        checkOverlay.heightProperty().bind(heightProperty());
        checkOverlay.setFill(Color.rgb(220, 50, 50, 0.55));
        checkOverlay.setMouseTransparent(true);

        getChildren().add(checkOverlay);
    }

    public void highlightLastMove() {
        if (lastMoveOverlay != null) {
            getChildren().remove(lastMoveOverlay);
        }

        lastMoveOverlay = new Rectangle();
        lastMoveOverlay.widthProperty().bind(widthProperty());
        lastMoveOverlay.heightProperty().bind(heightProperty());
        lastMoveOverlay.setFill(Color.rgb(255, 255, 0, 0.25));
        lastMoveOverlay.setMouseTransparent(true);

        getChildren().add(0, lastMoveOverlay);
    }

    public void clearMoveHighlight() {
        if (highlightOverlay != null) {
            getChildren().remove(highlightOverlay);
            highlightOverlay = null;
        }

        if (moveDot != null) {
            getChildren().remove(moveDot);
            moveDot = null;
        }

        if (captureRing != null) {
            getChildren().remove(captureRing);
            captureRing = null;
        }
    }

    public void clearCheckHighlight() {
        if (checkOverlay != null) {
            getChildren().remove(checkOverlay);
            checkOverlay = null;
        }
    }

    public void clearLastMoveHighlight() {
        if (lastMoveOverlay != null) {
            getChildren().remove(lastMoveOverlay);
            lastMoveOverlay = null;
        }
    }

    public void clearHighlight() {
        clearMoveHighlight();
        clearCheckHighlight();
        clearLastMoveHighlight();
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