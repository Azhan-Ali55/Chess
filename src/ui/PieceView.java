package ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.*;

public class PieceView extends ImageView {

    public PieceView(Piece piece) {
        Image image = getPieceImage(piece);
        setImage(image);
        setFitWidth(70);
        setFitHeight(70);
        setPreserveRatio(true);
    }

    private Image getPieceImage(Piece piece) {
        String color = piece.getColor() == Color.WHITE ? "white" : "black";
        String pieceName;

        if (piece instanceof Pawn) {
            pieceName = "pawn";
        } else if (piece instanceof Rook) {
            pieceName = "rook";
        } else if (piece instanceof Knight) {
            pieceName = "knight";
        } else if (piece instanceof Bishop) {
            pieceName = "bishop";
        } else if (piece instanceof Queen) {
            pieceName = "queen";
        } else if (piece instanceof King) {
            pieceName = "king";
        } else {
            throw new IllegalArgumentException("Unknown piece type");
        }

        String path = "/images/" + color + "-" + pieceName + ".png";

        return new Image(getClass().getResourceAsStream(path));
    }
}