package ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Piece;

public class PieceView extends ImageView {

    public PieceView(Piece piece) {
        Image image = getPieceImage(piece);
        setImage(image);
        setFitWidth(70);
        setFitHeight(70);
        setPreserveRatio(true);
    }

    private Image getPieceImage(Piece piece) {
        // Will be implemented later. For now only returns null
        return null;
    }
}