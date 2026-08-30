package util;

import model.*;

public class FenConverter {
    public static String boardToFen(Game game) {
        Board board = game.getBoard();
        StringBuilder fen = new StringBuilder();

        // Board position
        for (int row = 0; row < 8; row++) {
            int emptySquares = 0;

            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);

                if (piece == null) {
                    emptySquares++;
                    continue;
                }

                if (emptySquares > 0) {
                    fen.append(emptySquares);
                    emptySquares = 0;
                }

                fen.append(getPieceSymbol(piece));
            }

            if (emptySquares > 0) {
                fen.append(emptySquares);
            }

            if (row < 7) {
                fen.append("/");
            }
        }

        // Active color
        fen.append(" ");
        fen.append(game.getCurrentTurn() == Color.WHITE ? "w" : "b");

        // Castling rights
        fen.append(" ");
        fen.append(getCastlingRights(board));

        // En passant
        fen.append(" ");
        fen.append(getEnPassantSquare(game));

        // Halfmove clock
        fen.append(" 0");

        // Fullmove number
        fen.append(" 1");

        return fen.toString();
    }

    private static char getPieceSymbol(Piece piece) {

        char symbol;

        if (piece instanceof Pawn) {
            symbol = 'p';
        } else if (piece instanceof Knight) {
            symbol = 'n';
        } else if (piece instanceof Bishop) {
            symbol = 'b';
        } else if (piece instanceof Rook) {
            symbol = 'r';
        } else if (piece instanceof Queen) {
            symbol = 'q';
        } else if (piece instanceof King) {
            symbol = 'k';
        } else {
            throw new IllegalArgumentException(
                    "Unknown piece type"
            );
        }

        if (piece.getColor() == Color.WHITE) {
            symbol = Character.toUpperCase(symbol);
        }

        return symbol;
    }

    private static String getCastlingRights(Board board) {
        StringBuilder rights = new StringBuilder();

        // White
        Piece whiteKing = board.getPiece(7, 4);

        if (whiteKing instanceof King && !whiteKing.hasMoved()) {

            Piece kingsideRook = board.getPiece(7, 7);

            if (kingsideRook instanceof Rook && !kingsideRook.hasMoved()) {
                rights.append("K");
            }

            Piece queensideRook = board.getPiece(7, 0);

            if (queensideRook instanceof Rook && !queensideRook.hasMoved()) {
                rights.append("Q");
            }
        }

        // Black
        Piece blackKing = board.getPiece(0, 4);

        if (blackKing instanceof King && !blackKing.hasMoved()) {

            Piece kingsideRook = board.getPiece(0, 7);

            if (kingsideRook instanceof Rook && !kingsideRook.hasMoved()) {
                rights.append("k");
            }

            Piece queensideRook = board.getPiece(0, 0);

            if (queensideRook instanceof Rook && !queensideRook.hasMoved()) {
                rights.append("q");
            }
        }

        if (rights.isEmpty()) {
            return "-";
        }

        return rights.toString();
    }

    private static String getEnPassantSquare(Game game) {

        Move lastMove = game.getLastMove();

        if (lastMove == null) {
            return "-";
        }

        int fromRow = lastMove.getFromRow();
        int toRow = lastMove.getToRow();
        int fromCol = lastMove.getFromCol();
        int toCol = lastMove.getToCol();

        Piece piece = game.getBoard().getPiece(toRow, toCol);

        // Last move must have been a pawn moving two squares
        if (!(piece instanceof Pawn)) {
            return "-";
        }

        if (fromCol != toCol) {
            return "-";
        }

        if (Math.abs(fromRow - toRow) != 2) {
            return "-";
        }

        // The square behind the pawn
        int enPassantRow = (fromRow + toRow) / 2;

        return squareToAlgebraic(enPassantRow, toCol);
    }

    private static String squareToAlgebraic(int row, int col) {

        char file = (char) ('a' + col);
        int rank = 8 - row;

        return "" + file + rank;
    }
}