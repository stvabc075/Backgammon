import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


public class Boardtest {
    
    private Board board;
    private InputCheck cmd;

    // setting up a board with users and specified roll before each test
    @BeforeEach
    void setUp() {
        board = new Board();
        board.setPlayer(1, new Player("Anthony", CheckerTemplate.RED));
	    board.setPlayer(2, new Player("Stephen", CheckerTemplate.WHITE));
	    board.setCurrentPlayer(1);
	    cmd = new InputCheck("ROLL12"); 
        board.setFace(cmd);
        board.initBoard();
        board.setGameNumber(2);
    }

    // test to ensure that the board object is successfully created
    @Test 
    void testBoard() {
        assertNotNull(board);
    }

    // test creation of a Board object simulating user inputs
    @Test 
    void testBoardInputStream() {
        InputStream inputStream = new ByteArrayInputStream("Ant\nSte\n5\n".getBytes());
        Board boardWithInputStream = new Board(inputStream);
        assertNotNull(boardWithInputStream);
    }

    // test initialization of players, checking their instance and types
    @Test
    void testInitPlayer() {
    	InputStream inputStream = new ByteArrayInputStream("Ant\nSte\n".getBytes(StandardCharsets.UTF_8));
		board = new Board(inputStream);
        board.initPlayer(1);
        assertNotNull(board.getPlayer(1));
        assertEquals(CheckerTemplate.RED, board.getPlayer(1).getCheckerTemp());
        board.initPlayer(2);
        assertNotNull(board.getPlayer(2));
        assertEquals(CheckerTemplate.WHITE, board.getPlayer(2).getCheckerTemp());
    }

    // test ending a turn and switching to the next player
    @Test 
	void testEndTurn() {
		board.endTurn();
		assertEquals(board.getPlayer(0), board.getPlayer(2));
	}

    // test the initial setup, verifying correct position and number of checkers
    @Test 
    void testInitBoard() {
        assertEquals(2, board.getPoint(0).size());
        assertEquals(CheckerTemplate.WHITE, board.getPoint(0).peek().getCheckerTemplate());
        assertEquals(3, board.getPoint(7).size());
        assertEquals( CheckerTemplate.RED, board.getPoint(7).peek().getCheckerTemplate());
        assertEquals(5, board.getPoint(12).size());
        assertEquals( CheckerTemplate.RED, board.getPoint(12).peek().getCheckerTemplate());
    }

    // test if move is legal based on the current state of the board and given command
    @Test 
    void testLegalMove() {
        cmd = new InputCheck("2423");
        assertTrue(board.moveisLegal(cmd));
    }

    // test the move method
    @Test 
    void testMove() {
        cmd = new InputCheck("2423");
        board.move(cmd); 
        assertEquals(1, board.getPoint(22).size());
        assertEquals(1, board.getPoint(23).size());
    }

    // test retrieval of dice face values after a roll
    @Test 
    void testGetFace() {
        assertEquals(1, board.getFace(1)); 
        assertEquals(2, board.getFace(2));
    }

    // test to check if a round is marked as over based on board state
    @Test 
    void testRoundOver() {
    	for (int i = 0; i < 24; i++)
	        board.getPoint(i).clear();
	    for (int i = 0; i < 2; i++)
	    	board.getBar(i).clear();
	    for (int i = 0; i < 2; i++)
	    	board.getEndpoint(i).clear();
		for (int i = 0; i < 15; i++)
			board.getPoint(20).push(new Checker(CheckerTemplate.WHITE));
		for (int i = 0; i < 15; i++)
			board.getEndpoint(0).push(new Checker(CheckerTemplate.RED));
		assertEquals(15, board.getEndpoint(0).size());
    }

    // test to ensure game over conditions are correct
    @Test 
    void testGameOver() {
        board.addRoundNumber();
        board.addRoundNumber();
    	assertEquals(board.getGameNumber() + 1, board.getRoundNumber());
    }

    // test to verify the correct size of specific points on the board
    @Test 
    void testGetSize() {
        assertEquals(5, board.getSize("uppoint")); 
        assertEquals(5, board.getSize("downpoint"));
    }

    // test the pip calculation
    @Test 
    void testCalcPips() {
        cmd = new InputCheck("2423");
        board.move(cmd); 
        cmd = new InputCheck("2422");
        board.move(cmd);
        board.calcPips(); 
        assertEquals(164, board.getPlayer(1).getPips()); 
        assertEquals(167, board.getPlayer(2).getPips());
    }

    // test rolling the dice
    @Test 
    void testRollDice() {
        board.rollDice();
        assertNotEquals(0, board.getTotalNumMoves());
    }

    // test to return num moves after setting dice values
    @Test
    void testgetTotalNumMoves() {
        board.setDiceStepVals(3, 4);
        assertEquals(7, board.getTotalNumMoves());
    }

    // test to set dice to 0
    @Test 
    void testSetZeroDice() {
        board.setZeroDice();
        assertEquals(0, board.getTotalNumMoves());
    }

    // test to check if return player works
    @Test 
    void testGetPlayer() {
        Player player1 = board.getPlayer(1);
        assertEquals(InterfaceColours.RED + "Anthony" + InterfaceColours.RESET, player1.dispName());
    }

    // test to check if setting current player works
    @Test 
    void testSetCurrentPlayer() {
        board.setCurrentPlayer(2);
        assertEquals(InterfaceColours.WHITE + "Stephen" + InterfaceColours.RESET, board.getPlayer(0).dispName());
    }

    // test to cehck that a point on the board is initialized
    @Test 
    void testGetPoint() {
        assertNotNull(board.getPoint(1));
    }

    // testing getter methods
    @Test 
    void testGetBar() {
        assertNotNull(board.getBar(1));
    }
    @Test 
    void testGetEndpoint() {
        assertNotNull(board.getEndpoint(1));
    }
    @Test 
    void testGetGameNumber() {
        assertEquals(2, board.getGameNumber());
    }
    @Test 
    void testSetGameNumber() {
        board.setGameNumber(5);
        assertEquals(5, board.getGameNumber());
    }
    @Test 
    void testgetRound() {
        board.setRound(2);
        assertEquals(2, board.getRound());
    }

    // testing setter and adding methods
    @Test
    void testsetRound() {
        board.setRound(3);
        assertEquals(3, board.getRound());
    }
    @Test 
    void testAddGamenumber() {
        board.setRound(2);
        board.addGamenumber();
        assertEquals(2, board.getRound());
    }
    @Test
    void testSetZeroScore() {
        board.setZeroScore();
        assertEquals(0, board.getPlayer(1).getScore());
        assertEquals(0, board.getPlayer(2).getScore());
    }
    @Test 
    void testAddScore() {
        board.addScore();
        assertEquals(10, board.getPlayer(0).getScore());
    }
    @Test 
    void testGetMoveStep() {
        board.setDiceStepVals(4, 0); 
        assertEquals(4, board.getMoveStep(1));
        assertEquals(0, board.getMoveStep(2));
    }
    @Test 
    void testSetDiceStepVals() {
        board.setDiceStepVals(4, 0); 
        assertEquals(4, board.getTotalNumMoves()); 
    }
    @Test
    void testSetPlayer() {
        Player newPlayer = new Player("Franklin", CheckerTemplate.RED); 
        board.setPlayer(1, newPlayer);
        assertEquals(InterfaceColours.RED + "Franklin" + InterfaceColours.RESET, board.getPlayer(1).dispName()); 
    }
}
