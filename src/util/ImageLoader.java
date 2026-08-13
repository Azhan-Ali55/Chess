package util;

import javafx.scene.image.Image;
import model.Bishop;
import model.Color;
import model.King;
import model.Knight;
import model.Pawn;
import model.Piece;
import model.Queen;
import model.Rook;

public class ImageLoader {

    public static Image loadPieceImage(Piece piece) {
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

        return new Image(
                ImageLoader.class.getResourceAsStream(path)
        );
    }
}