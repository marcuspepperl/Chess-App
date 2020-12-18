package piece;

import board.BoardWrapper;
import board.Coordinates;

import java.util.Set;

public abstract class APiece {

    protected Coordinates coordinates;
    protected int moveCount;
    protected int color;

    public APiece(Coordinates coordinates, int color) {
        this.coordinates = coordinates;
        this.color = color;
        this.moveCount = 0;
    }

    public Coordinates getCoordinates() {
        return this.coordinates;
    }

    public int getMoveCount() {
        return this.moveCount;
    }

    public int getColor() {
        return this.color;
    }

    public void move(Coordinates newCoordinates) {
        this.coordinates = newCoordinates;
        this.moveCount++;
    }


    public boolean isValidMove(Coordinates newCoordinates, BoardWrapper<Integer> colorBoard) {
        return this.validMoves(colorBoard).contains(newCoordinates);
    }

    public abstract Set<Coordinates> validMoves(BoardWrapper<Integer> colorBoard);

    protected boolean inBounds(int xPos, int yPos, int xSize, int ySize) {
        return xPos > 0 && xPos < xSize && yPos > 0 && yPos < ySize;
    }
}
