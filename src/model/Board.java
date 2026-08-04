package model;

public class Board {
    private Piece[][] board;
    public Board() {
        board = new Piece[8][8];
        initializeBoard();
    }

    public boolean isInsideBoard(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    public void setPiece(int row, int col, Piece piece) {
        board[row][col] = piece;
    }

    public void removePiece(int row, int col) {
        board[row][col] = null;
    }

    public Piece getPiece(int row, int col) {
        return board[row][col];
    }

    public void initializeBoard() {
        // Black pieces
        setPiece(0, 0, new Rook(Color.BLACK));
        setPiece(0, 1, new Knight(Color.BLACK));
        setPiece(0, 2, new Bishop(Color.BLACK));
        setPiece(0, 3, new Queen(Color.BLACK));
        setPiece(0, 4, new King(Color.BLACK));
        setPiece(0, 5, new Bishop(Color.BLACK));
        setPiece(0, 6, new Knight(Color.BLACK));
        setPiece(0, 7, new Rook(Color.BLACK));

        // White pieces
        setPiece(7, 0, new Rook(Color.WHITE));
        setPiece(7, 1, new Knight(Color.WHITE));
        setPiece(7, 2, new Bishop(Color.WHITE));
        setPiece(7, 3, new Queen(Color.WHITE));
        setPiece(7, 4, new King(Color.WHITE));
        setPiece(7, 5, new Bishop(Color.WHITE));
        setPiece(7, 6, new Knight(Color.WHITE));
        setPiece(7, 7, new Rook(Color.WHITE));

        // Loop to set pawns
        for (int col = 0; col < 8; col++) {
            setPiece(1, col, new Pawn(Color.BLACK));
            setPiece(6, col, new Pawn(Color.WHITE));
        }
    }
}
