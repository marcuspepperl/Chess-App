package main.piece;

import main.board.Coordinates;

import java.util.HashSet;
import java.util.Set;

public class PieceSetBuilder {

    public static Set<APiece> getStandardGame() {
        Set<APiece> allPieces = getStandardWhite();
        allPieces.addAll(getStandardBlack());
        return allPieces;
    }

    public static Set<APiece> getStandardWhite() {

        Set<APiece> standardWhite = new HashSet<>();
        standardWhite.add(createPiece(3, 1, 0, 0));
        standardWhite.add(createPiece(1, 1, 1, 0));
        standardWhite.add(createPiece(2, 1, 2, 0));
        standardWhite.add(createPiece(4, 1, 3, 0));
        standardWhite.add(createPiece(5, 1, 4, 0));
        standardWhite.add(createPiece(2, 1, 5, 0));
        standardWhite.add(createPiece(1, 1, 6, 0));
        standardWhite.add(createPiece(3, 1, 7, 0));
        for (int xPos = 0; xPos < 8; xPos++) {
            standardWhite.add(createPiece(0, 1, xPos, 1));
        }
        return standardWhite;
    }
    public static Set<APiece> getStandardBlack() {

        Set<APiece> standardBlack = new HashSet<>();
        standardBlack.add(createPiece(3, -1, 0, 7));
        standardBlack.add(createPiece(1, -1, 1, 7));
        standardBlack.add(createPiece(2, -1, 2, 7));
        standardBlack.add(createPiece(4, -1, 3, 7));
        standardBlack.add(createPiece(5, -1, 4, 7));
        standardBlack.add(createPiece(2, -1, 5, 7));
        standardBlack.add(createPiece(1, -1, 6, 7));
        standardBlack.add(createPiece(3, -1, 7, 7));
        for (int xPos = 0; xPos < 8; xPos++) {
            standardBlack.add(createPiece(0, -1, xPos, 6));
        }
        return standardBlack;
    }

    public static Set<APiece> getStandardFoolsMate() {

        Set<APiece> foolsMateWhite = new HashSet<>();
        Set<APiece> foolsMateBlack = new HashSet<>();

        foolsMateWhite.add(createPiece(3, 1, 0, 0));
        foolsMateWhite.add(createPiece(1, 1, 1, 0));
        foolsMateWhite.add(createPiece(2, 1, 2, 0));
        foolsMateWhite.add(createPiece(4, 1, 3, 0));
        foolsMateWhite.add(createPiece(5, 1, 4, 0));
        foolsMateWhite.add(createPiece(2, 1, 5, 0));
        foolsMateWhite.add(createPiece(1, 1, 6, 0));
        foolsMateWhite.add(createPiece(3, 1, 7, 0));
        for (int xPos = 0; xPos < 5; xPos++) {
            foolsMateWhite.add(createPiece(0, 1, xPos, 1));
        }
        foolsMateWhite.add(createPiece(0, 1, 5, 2));
        foolsMateWhite.add(createPiece(0, 1, 6, 3));
        foolsMateWhite.add(createPiece(0, 1, 7, 1));

        foolsMateBlack.add(createPiece(3, -1, 0, 7));
        foolsMateBlack.add(createPiece(1, -1, 1, 7));
        foolsMateBlack.add(createPiece(2, -1, 2, 7));
        foolsMateBlack.add(createPiece(4, -1, 7, 3));
        foolsMateBlack.add(createPiece(5, -1, 4, 7));
        foolsMateBlack.add(createPiece(2, -1, 5, 7));
        foolsMateBlack.add(createPiece(1, -1, 6, 7));
        foolsMateBlack.add(createPiece(3, -1, 7, 7));
        for (int xPos = 0; xPos < 4; xPos++) {
            foolsMateBlack.add(createPiece(0, -1, xPos, 6));
        }
        for (int xPos = 5; xPos < 8; xPos++) {
            foolsMateBlack.add(createPiece(0, -1, xPos, 6));
        }
        foolsMateBlack.add(createPiece(0, -1, 4, 4));

        foolsMateWhite.addAll(foolsMateBlack);
        return foolsMateWhite;
    }

    public static APiece createPiece(int pieceType, int color, int xPos, int yPos) {
        APiece newPiece = createPiece(pieceType, color);
        assert newPiece != null;
        newPiece.setCoordinates(new Coordinates(xPos, yPos));
        return newPiece;
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
