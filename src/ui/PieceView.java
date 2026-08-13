package ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PieceView extends ImageView {

    public PieceView(Image image) {
        super(image);

        setFitWidth(70);
        setFitHeight(70);
        setPreserveRatio(true);
    }
}