package piece;

import java.util.HashSet;
import java.util.Set;
import board.Coordinates;

public class Bishop extends ADistancePiece {

    private static Set<Coordinates> directions;

    public Bishop(Coordinates coordinates, int color) {
        super(coordinates, color);
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
