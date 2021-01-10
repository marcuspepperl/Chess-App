package test.game;

import org.junit.Test;
import org.junit.jupiter.api.*;

public class ChessGameTest {

    @BeforeAll
    static void setUp() {
    }

    @Test
    @Order(1)
    public void testRandomGame() {
        assert(true);
    }

    @Test
    @Order(2)
    public void testResign() {
        assert(true);
    }

    @Test
    @Order(3)
    public void testDraw() {
        assert(true);
    }
}
