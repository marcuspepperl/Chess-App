package main.board;

public class Coordinates {
    private final int xPos;
    private final int yPos;

    public Coordinates(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public int getXPos() {
        return this.xPos;
    }
    public int getYPos() {
        return this.yPos;
    }

    @Override
    public boolean equals(Object obj) {
       if (!(obj instanceof Coordinates)) {
           return false;
       }
       Coordinates otherCoords = (Coordinates) obj;
       return this.xPos == otherCoords.getXPos() && this.yPos == otherCoords.getYPos();
    }

    @Override
    public String toString() {
        return "(" + this.xPos + ", " + this.yPos + ")";
    }

    @Override
    public int hashCode() {
        return this.xPos + this.yPos;
    }
}
