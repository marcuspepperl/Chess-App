package main.piece;

import java.util.HashSet;
import java.util.Set;

import main.board.BoardWrapper;
import main.board.Coordinates;

public class Knight extends APiece {

    private static Set<Coordinates> moves;

    public Knight(Coordinates coordinates, int color) {
        super(coordinates, color);
    }

    public char getType() {
        return 'N';
    }

    public void updateValidMoves(BoardWrapper<Integer> colorBoard) {

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
        this.validMoves = validSquares;
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
