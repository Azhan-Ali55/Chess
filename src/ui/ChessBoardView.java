package ui;

import javafx.scene.layout.GridPane;
import model.Game;
import model.Board;
import model.Piece;

public class ChessBoardView extends GridPane {
    private static final int BOARD_SIZE = 8;
    private final SquareView[][] squares = new SquareView[8][8];
    private final Game game;
    private PieceView draggingPiece;
    private Piece draggingModelPiece;

    public ChessBoardView(Game game) {
        this.game = game;
        createBoard();
    }

    private void createBoard() {
        Board board = new Board();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                SquareView square = new SquareView(row, col);
                squares[row][col] = square;
                Piece piece = board.getPiece(row, col);
                if (piece != null) {
                    PieceView pieceView = new PieceView(piece);
                    square.getChildren().add(pieceView);
                }
                add(square, col, row);
            }
        }
    }

    public SquareView getSquare(int row, int col) {
        return squares[row][col];
    }

    public void refresh() {
        Board board = game.getBoard();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                SquareView square = squares[row][col];

                // Remove the old piece image
                square.getChildren().clear();

                Piece piece = board.getPiece(row, col);

                // Add the new piece image if there is a piece
                if (piece != null) {
                    square.getChildren().add(new PieceView(piece));
                }
            }
        }
    }

    public void highlightSquare(int row, int col) {
        squares[row][col].highlight();
    }

    public void clearHighlights() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].clearHighlight();
            }
        }
    }

    public void startDragging(int row, int col, double sceneX, double sceneY) {
        Piece piece = game.getBoard().getPiece(row, col);
        if (piece == null) return;
        draggingModelPiece = piece;
        draggingPiece = new PieceView(piece);

        // Make it float above the board
        draggingPiece.setManaged(false);
        draggingPiece.setMouseTransparent(true);

        getChildren().add(draggingPiece);
        updateDraggingPiece(sceneX, sceneY);

        // Hide the original piece
        SquareView square = squares[row][col];

        if (!square.getChildren().isEmpty()) {
            square.getChildren().get(0).setVisible(false);
        }
    }

    public void updateDraggingPiece(double sceneX, double sceneY) {
        if (draggingPiece == null) return;
        javafx.geometry.Point2D point = sceneToLocal(sceneX, sceneY);

        // Center the 70x70 piece under the mouse
        draggingPiece.setLayoutX(point.getX() - 35);
        draggingPiece.setLayoutY(point.getY() - 35);
    }

    public void stopDragging(int row, int col) {
        if (draggingPiece != null) {
            getChildren().remove(draggingPiece);
            draggingPiece = null;
            draggingModelPiece = null;
        }

        SquareView square = squares[row][col];

        if (!square.getChildren().isEmpty()) {
            square.getChildren().get(0).setVisible(true);
        }
    }

    public void highlightCapture(int row, int col) {
        squares[row][col].highlightCapture();
    }
}