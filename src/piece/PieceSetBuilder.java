package piece;

import board.Coordinates;
import piece.*;

import java.util.HashSet;
import java.util.Set;

public class PieceSetBuilder {
    private static Set<APiece> standardWhite;
    private static Set<APiece> standardBlack;

    public static Set<APiece> getStandardWhite() {
        if (standardWhite == null) {
            standardWhite = new HashSet<>();
            standardWhite.add(new Rook(new Coordinates(0, 0), 1));
            standardWhite.add(new Knight(new Coordinates(1, 0), 1));
            standardWhite.add(new Bishop(new Coordinates(2, 0), 1));
            standardWhite.add(new Queen(new Coordinates(3, 0), 1));
            standardWhite.add(new King(new Coordinates(4, 0), 1));
            standardWhite.add(new Bishop(new Coordinates(5, 0), 1));
            standardWhite.add(new Knight(new Coordinates(6, 0), 1));
            standardWhite.add(new Rook(new Coordinates(7, 0), 1));
            for (int xPos = 0; xPos < 8; xPos++) {
                standardWhite.add(new Pawn(new Coordinates(xPos, 1), 1));
            }
        }
        return standardWhite;
    }
    public static Set<APiece> getStandardBlack() {
        if (standardBlack == null) {
            standardBlack = new HashSet<>();
            standardBlack.add(new Rook(new Coordinates(0, 7), -1));
            standardBlack.add(new Knight(new Coordinates(1, 7), -1));
            standardBlack.add(new Bishop(new Coordinates(2, 7), -1));
            standardBlack.add(new Queen(new Coordinates(3, 7), -1));
            standardBlack.add(new King(new Coordinates(4, 7), -1));
            standardBlack.add(new Bishop(new Coordinates(5, 7), -1));
            standardBlack.add(new Knight(new Coordinates(6, 7), -1));
            standardBlack.add(new Rook(new Coordinates(7, 7), -1));
            for (int xPos = 0; xPos < 8; xPos++) {
                standardBlack.add(new Pawn(new Coordinates(xPos, 6), -1));
            }
        }
        return standardBlack;
    }

    public static APiece createPiece(int pieceType, int color) {
        switch (pieceType) {
            case 0:
                return new Pawn(null, color);
            case 1:
                return new Knight(null, color);
            case 2:
                return new Bishop(null, color);
            case 3:
                return new Rook(null, color);
            case 4:
                return new Queen(null, color);
            case 5:
                return new King(null, color);
            default:
                return null;
        }
    }
}
