package piece;

import java.util.HashSet;
import java.util.Set;
import board.Coordinates;

public class Queen extends ADistancePiece {

    private static Set<Coordinates> directions;

    public Queen(Coordinates coordinates, int color) {
        super(coordinates, color);
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

