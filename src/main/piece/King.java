package main.piece;

import java.util.HashSet;
import java.util.Set;

import main.board.BoardWrapper;
import main.board.Coordinates;

public class King extends APiece {

    private static Set<Coordinates> moves;

    public King(Coordinates coordinates, int color) {
        super(coordinates, color);
    }

    public char getType() {
        return 'K';
    }

    public void updateValidMoves(BoardWrapper<Integer> colorBoard) {

        int xSize = colorBoard.getXSize(), ySize = colorBoard.getYSize();
        int xPos = this.coordinates.getXPos(), yPos = this.coordinates.getYPos();
        int newXPos, newYPos;

        Set<Coordinates> validSquares = new HashSet<>();

        for (Coordinates move : this.getMoves()) {
            newXPos = xPos + move.getXPos();
            newYPos = yPos + move.getYPos();
            if (this.inBounds(newXPos, newYPos, xSize, ySize) &&
                colorBoard.getValue(newXPos, newYPos) != this.color) {
                validSquares.add(new Coordinates(newXPos, newYPos));
            }
        }
       this.validMoves = validSquares;
    }

    private Set<Coordinates> getMoves() {
        if (moves == null) {
            moves = new HashSet<>();
            moves.add(new Coordinates(0, 1));
            moves.add(new Coordinates(0, -1));
            moves.add(new Coordinates(1, 0));
            moves.add(new Coordinates(-1, 0));
            moves.add(new Coordinates(1, 1));
            moves.add(new Coordinates(1, -1));
            moves.add(new Coordinates(-1, 1));
            moves.add(new Coordinates(-1, -1));
        }
        return moves;
    }
}
