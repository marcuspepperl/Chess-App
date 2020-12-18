package piece;

import board.BoardWrapper;
import board.Coordinates;

import java.util.HashSet;
import java.util.Set;

public abstract class ADistancePiece extends APiece {

    private static Set<Coordinates> directions;

    public ADistancePiece(Coordinates coordinates, int color) {
        super(coordinates, color);
    }

    public Set<Coordinates> validMoves(BoardWrapper<Integer> colorBoard) {

        Set<Coordinates> validSquares = new HashSet<>();

        for (Coordinates direction : this.getDirections()) {
            validSquares.addAll(this.movesInDirection(direction, colorBoard));
        }
        return validSquares;
    }

    public Set<Coordinates> getDirections() {
        if (directions == null) {
            directions = getDirectionsHelper();
        }
        return directions;
    }

    protected Set<Coordinates> movesInDirection(Coordinates offSet, BoardWrapper<Integer>
        colorBoard){

        Set<Coordinates> validSquares = new HashSet<>();
        int xSize = colorBoard.getXSize(), ySize = colorBoard.getYSize();
        int xOffSet = offSet.getXPos(), yOffSet = offSet.getYPos();
        int xPos = this.coordinates.getXPos(), yPos = this.coordinates.getYPos();
        int newXPos = xPos + xOffSet, newYPos = yPos + yOffSet;

        while (this.inBounds(newXPos, newYPos, xSize, ySize) && colorBoard.getValue(newXPos,
            newYPos) == 0) {
            validSquares.add(new Coordinates(newXPos, newYPos));
            newXPos += xOffSet;
            newYPos += yOffSet;
        }
        if (this.inBounds(newXPos, newYPos, xSize, ySize) && colorBoard.getValue(newXPos,
            newYPos) != this.color) {
            validSquares.add(new Coordinates(newXPos, newYPos));
        }
        return validSquares;
    }

    protected abstract Set<Coordinates> getDirectionsHelper();
}
