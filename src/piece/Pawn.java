package piece;

import java.util.HashSet;
import java.util.Set;

import board.BoardWrapper;
import board.Coordinates;

public class Pawn extends APiece {

    public Pawn(Coordinates coordinates, int color) {
        super(coordinates, color);
    }

    public Set<Coordinates> validMoves(BoardWrapper<Integer> colorBoard) {

        int xSize = colorBoard.getXSize(), ySize = colorBoard.getYSize();
        int xPos = this.coordinates.getXPos(), yPos = this.coordinates.getYPos();
        int newYPos = yPos + this.color;

        Set<Coordinates> validSquares = new HashSet<>();
        if (this.inBounds(xPos, newYPos, xSize, ySize) && colorBoard.getValue(xPos, newYPos) == 0) {
            validSquares.add(new Coordinates(xPos, yPos + this.color));

            newYPos += this.color;
            if (this.inBounds(xPos, newYPos, xSize, ySize) && colorBoard.getValue(xPos, newYPos) == 0
                && ((this.color == 1 && newYPos == 1) || (this.color == -1 &&
                    (newYPos == ySize - 2)))) {
                validSquares.add(new Coordinates(xPos, newYPos));
            }
        }
        newYPos = yPos + this.color;

        if (this.inBounds(xPos + 1, yPos + this.color, xSize, ySize) &&
            colorBoard.getValue(xPos + 1, newYPos) == -1 * this.color) {
            validSquares.add(new Coordinates(xPos + 1, yPos + this.color));
        }
        if (this.inBounds(xPos - 1, yPos + this.color, xSize, ySize) &&
            colorBoard.getValue(xPos - 1, newYPos) == -1 * this.color) {
            validSquares.add(new Coordinates(xPos - 1, yPos + this.color));
        }
        return validSquares;
    }

}
