package main.piece;

import java.util.HashSet;
import java.util.Set;

import main.board.BoardWrapper;
import main.board.Coordinates;

public class Bishop extends ADistancePiece {

    private static Set<Coordinates> directions;

    public Bishop(Coordinates coordinates, int color) {
        super(coordinates, color);
    }

    public char getType() {
        return 'B';
    }

    protected Set<Coordinates> getDirections() {
        if (directions == null) {
            directions = getDirectionsHelper();
        }
        return directions;
    }

    protected Set<Coordinates> getDirectionsHelper() {
        Set<Coordinates> allDirections = new HashSet<>();
        allDirections.add(new Coordinates(1, 1));
        allDirections.add(new Coordinates(1, -1));
        allDirections.add(new Coordinates(-1, 1));
        allDirections.add(new Coordinates(-1, -1));
        return allDirections;
    }
}
