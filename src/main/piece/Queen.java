package main.piece;

import java.util.HashSet;
import java.util.Set;
import main.board.Coordinates;

public class Queen extends ADistancePiece {

    private static Set<Coordinates> directions;

    public Queen(Coordinates coordinates, int color) {
        super(coordinates, color);
    }

    public char getType() {
        return 'Q';
    }

    protected Set<Coordinates> getDirections() {
        if (directions == null) {
            directions = getDirectionsHelper();
        }
        return directions;
    }

    protected Set<Coordinates> getDirectionsHelper() {
        Set<Coordinates> allDirections = new HashSet<>();
        allDirections.add(new Coordinates(0, 1));
        allDirections.add(new Coordinates(0, -1));
        allDirections.add(new Coordinates(1, 0));
        allDirections.add(new Coordinates(-1, 0));
        allDirections.add(new Coordinates(1, 1));
        allDirections.add(new Coordinates(1, -1));
        allDirections.add(new Coordinates(-1, 1));
        allDirections.add(new Coordinates(-1, -1));
        return allDirections;
    }
}

