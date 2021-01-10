package main.board;

public class BoardWrapper<T> {

    private final T[][] board;
    private final int xSize;
    private final int ySize;

    public BoardWrapper(T[][] board) {
        this.board = board;
        this.xSize = board.length;
        this.ySize = board[0].length;
    }

    public T getValue(int xPos, int yPos) {
        return this.board[xPos][yPos];
    }

    public void setValue(int xPos, int yPos, T value) {
        this.board[xPos][yPos] = value;
    }

    public int getXSize() {
        return this.xSize;
    }

    public int getYSize() {
        return this.ySize;
    }
}
