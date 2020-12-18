package board;

public class Coordinates {
    private int xPos;
    private int yPos;

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
}
