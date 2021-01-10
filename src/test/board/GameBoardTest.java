package test.board;

import main.board.GameBoard;
import main.board.InvalidSetUpException;
import main.display.ConsoleDisplay;
import main.piece.PieceSetBuilder;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameBoardTest {

    static GameBoard basicCheck;
    static GameBoard basicCheckmate;
    static GameBoard basicStalemate;
    static GameBoard standardBoard;
    static GameBoard standardFoolsBoard;

    @BeforeAll
    static void setUp() throws InvalidSetUpException {
        setUpStandard();
        setUpFools();
    }

    @Test
    @Order(1)
    void testNotCheckWhiteStandard() {
        new ConsoleDisplay(standardBoard).displayStringBoard(1);
        assertFalse(standardBoard.isCheck(1));
    }

    @Test
    @Order(2)
    void testNotCheckBlackStandard() {
        new ConsoleDisplay(standardBoard).displayStringBoard(-1);
        assertFalse(standardBoard.isCheck(-1));
    }

    @Test
    @Order(3)
    void testNotStalemateWhiteStandard() {
        new ConsoleDisplay(standardBoard).displayStringBoard(1);
        assertFalse(standardBoard.isStaleMate(1));
    }

    @Test
    @Order(4)
    void testNotStalemateBlackStandard() {
        new ConsoleDisplay(standardBoard).displayStringBoard(-1);
        assertFalse(standardBoard.isStaleMate(-1));
    }

    @Test
    @Order(5)
    void testFoolMate() {
        new ConsoleDisplay(standardFoolsBoard).displayStringBoard(1);
        assert(standardFoolsBoard.isCheckMate(1));
    }

    static void setUpStandard() throws InvalidSetUpException {
        standardBoard = new GameBoard(8, 8);
        standardBoard.setPieces(PieceSetBuilder.getStandardGame());
        standardBoard.updateValidMoves();
    }

    static void setUpFools() throws InvalidSetUpException {
        standardFoolsBoard = new GameBoard(8, 8);
        standardFoolsBoard.setPieces(PieceSetBuilder.getStandardFoolsMate());
        standardFoolsBoard.updateValidMoves();
    }
}
