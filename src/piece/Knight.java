package piece;

import java.util.HashSet;
import java.util.Set;

import board.BoardWrapper;
import board.Coordinates;

public class Knight extends APiece {

    private static Set<Coordinates> moves;

    public Knight(Coordinates coordinates, int color) {
        super(coordinates, color);
    }

    public Set<Coordinates> validMoves(BoardWrapper<Integer> colorBoard) {

        Set<Coordinates> validSquares = new HashSet<>();
        int xSize = colorBoard.getXSize(), ySize = colorBoard.getYSize();
        int xPos = this.coordinates.getXPos(), yPos = this.coordinates.getYPos();
        int newXPos, newYPos;

        for (Coordinates move : this.getMoves()) {
            newXPos = xPos + move.getXPos();
            newYPos = yPos + move.getYPos();
            if (this.inBounds(newXPos, newYPos, xSize, ySize) && colorBoard.getValue
                (newXPos, newYPos) != this.color) {
                validSquares.add(new Coordinates(newXPos, newYPos));
            }
        }
        return validSquares;
    }

    private Set<Coordinates> getMoves() {
        if (moves == null) {
            moves = new HashSet<>();
            moves.add(new Coordinates(1, 2));
            moves.add(new Coordinates(1, -2));
            moves.add(new Coordinates(-1, 2));
            moves.add(new Coordinates(-1, -2));
            moves.add(new Coordinates(2, 1));
            moves.add(new Coordinates(2, -1));
            moves.add(new Coordinates(-2, 1));
            moves.add(new Coordinates(-2, -1));
        }
        return moves;
    }
}
