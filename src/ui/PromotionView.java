package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.image.ImageView;
import model.Bishop;
import model.Color;
import model.Knight;
import model.Queen;
import model.Rook;
import util.ImageLoader;

public class PromotionView extends HBox {
    private static final double BAR_WIDTH = 280;
    private static final double BAR_HEIGHT = 75;
    private final Button queenButton;
    private final Button rookButton;
    private final Button bishopButton;
    private final Button knightButton;

    public PromotionView() {
        setSpacing(5);
        setAlignment(Pos.CENTER);

        queenButton = new Button();
        rookButton = new Button();
        bishopButton = new Button();
        knightButton = new Button();

        getChildren().addAll(queenButton, rookButton, bishopButton, knightButton);
        setPrefSize(BAR_WIDTH, BAR_HEIGHT);
        setMinSize(BAR_WIDTH, BAR_HEIGHT);
        setMaxSize(BAR_WIDTH, BAR_HEIGHT);
        setStyle(
                "-fx-background-color: #f0d9b5;" +
                        "-fx-border-color: #333333;" +
                        "-fx-border-width: 2;" +
                        "-fx-padding: 5;"
        );
        setButtonSize(queenButton);
        setButtonSize(rookButton);
        setButtonSize(bishopButton);
        setButtonSize(knightButton);
        setVisible(false);
    }

    public void setColor(Color color) {
        ImageView queenImage =
                new ImageView(ImageLoader.loadPieceImage(new Queen(color)));

        ImageView rookImage =
                new ImageView(ImageLoader.loadPieceImage(new Rook(color)));

        ImageView bishopImage =
                new ImageView(ImageLoader.loadPieceImage(new Bishop(color)));

        ImageView knightImage =
                new ImageView(ImageLoader.loadPieceImage(new Knight(color)));

        setImageSize(queenImage);
        setImageSize(rookImage);
        setImageSize(bishopImage);
        setImageSize(knightImage);

        queenButton.setGraphic(queenImage);
        rookButton.setGraphic(rookImage);
        bishopButton.setGraphic(bishopImage);
        knightButton.setGraphic(knightImage);
    }

    public void positionAt(int row, int col) {
        double squareSize = 80;
        double x = col * squareSize + (squareSize / 2) - (BAR_WIDTH / 2);
        double y;

        // White promotes at row 0.
        // Put the bar BELOW the promotion square.
        if (row == 0) {
            y = squareSize;
        }

        // Black promotes at row 7.
        // Put the bar ABOVE the promotion square.
        else {
            y = row * squareSize - BAR_WIDTH;
        }

        // Keep the bar inside the board.
        x = Math.max(0, Math.min(x, 640 - BAR_WIDTH));
        setTranslateX(x);
        setTranslateY(y);
    }

    private void setImageSize(ImageView imageView) {
        imageView.setFitWidth(60);
        imageView.setFitHeight(60);
        imageView.setPreserveRatio(true);
    }

    private void setButtonSize(Button button) {
        button.setPrefSize(65, 65);
        button.setMinSize(65, 65);
        button.setMaxSize(65, 65);
    }

    // Getters
    public Button getQueenButton() { return queenButton; }
    public Button getRookButton() { return rookButton; }
    public Button getBishopButton() { return bishopButton; }
    public Button getKnightButton() { return knightButton; }
}