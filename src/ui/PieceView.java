package ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Piece;
import util.ImageLoader;

public class PieceView extends ImageView {

    public PieceView(Piece piece) {
        super(ImageLoader.loadPieceImage(piece));
        setFitWidth(70);
        setFitHeight(70);
        setPreserveRatio(true);
    }
}