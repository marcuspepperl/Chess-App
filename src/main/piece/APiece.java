package main.piece;

import main.board.BoardWrapper;
import main.board.Coordinates;

import java.util.Set;

public abstract class APiece {

    protected Coordinates coordinates;
    protected int moveCount;
    protected int color;
    protected Set<Coordinates> validMoves;

    public APiece(Coordinates coordinates, int color) {
        this.coordinates = coordinates;
        this.color = color;
        this.moveCount = 0;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
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

    public Set<Coordinates> getValidMoves() {
        return this.validMoves;
    }

    public boolean isValidMove(Coordinates newCoordinates) {
        return this.validMoves.contains(newCoordinates);
    }

    protected boolean inBounds(int xPos, int yPos, int xSize, int ySize) {
        return xPos >= 0 && xPos < xSize && yPos >= 0 && yPos < ySize;
    }

    public abstract void updateValidMoves(BoardWrapper<Integer> colorBoard);

    public abstract char getType();
}
